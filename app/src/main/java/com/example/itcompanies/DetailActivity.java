package com.example.itcompanies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new DatabaseHelper(this);

        // Initialisation des vues
        ImageView www = findViewById(R.id.iconWebsite);
        ImageView tel = findViewById(R.id.iconContact);
        ImageView email = findViewById(R.id.iconEmail);
        ImageView location = findViewById(R.id.iconmap);
        ImageView imageView = findViewById(R.id.imageView8);
        ImageView agenda = findViewById(R.id.iconagenda);
        TextView nameView = findViewById(R.id.textViewServices);
        ListView servicesListView = findViewById(R.id.servicesListView);
        Button editButton = findViewById(R.id.btnModify);
        Button deleteButton= findViewById(R.id.btnDelete);
        // Récupération de l'objet Parcelable
        String namecompines = (String) getIntent().getStringExtra("compinesName");
        if (namecompines == null) {
            Toast.makeText(this, "Erreur : données manquantes", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Compines compines= dbHelper.getCompanyByName(namecompines);
        // Affichage du nom et des services
        nameView.setText(compines.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, compines.getServices());
        servicesListView.setAdapter(adapter);

        // Affichage de l'image
        byte[] imageBytes = compines.getImageBytes();
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageResource(R.drawable.default_logo); // Placeholder par défaut
        }
        //delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteCompanyById(compines.getId());
                finish();
            }
        });
        // Gestion des clics
        agenda.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, CalanderActivity.class); // Vérifiez le manifeste
            startActivity(intent);
        });

        editButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, EditCompanyActivity.class);
            intent.putExtra("companyName", compines.getName()); // Passer les détails nécessaires
            startActivity(intent);
        });

        location.setOnClickListener(view -> {
            String geoUri = "geo:" + compines.getLatitude() + "," + compines.getLongitude();
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapIntent);
        });

        email.setOnClickListener(view -> showEmailDialog(compines.getEmail()));

        www.setOnClickListener(view -> {
            String website = compines.getWebsite();
            if (website != null && !website.isEmpty()) {
                if (!website.startsWith("http://") && !website.startsWith("https://")) {
                    website = "http://" + website;
                }
                try {
                    Uri link = Uri.parse(website);
                    Intent web = new Intent(Intent.ACTION_VIEW, link);
                    startActivity(web);
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "URL invalide", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DetailActivity.this, "Aucun site web fourni", Toast.LENGTH_SHORT).show();
            }
        });

        tel.setOnClickListener(view -> {
            Uri number = Uri.parse("tel:" + compines.getPhone());
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showEmailDialog(String email) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Champs de texte pour l'email
        final EditText subjectInput = new EditText(this);
        subjectInput.setHint("Objet");
        layout.addView(subjectInput);

        final EditText messageInput = new EditText(this);
        messageInput.setHint("Message");
        layout.addView(messageInput);

        // Créez la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envoyer un e-mail")
                .setView(layout)
                .setPositiveButton("Envoyer", (dialog, which) -> {
                    String subject = subjectInput.getText().toString();
                    String message = messageInput.getText().toString();
                    sendEmail(email, subject, message);
                    new Handler().postDelayed(this::showRatingDialog, 3000); // Afficher le RatingBar après 3s
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendEmail(String email, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            startActivity(Intent.createChooser(emailIntent, "Envoyer l'email avec :"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Aucune application d'e-mail n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRatingDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        layout.addView(ratingBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Évaluez l'entreprise")
                .setView(layout)
                .setPositiveButton("Envoyer", (dialog, which) -> {
                    float rating = ratingBar.getRating();
                    Toast.makeText(this, "Vous avez noté : " + rating, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
