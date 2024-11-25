package com.example.itcompanies;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Clock;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ImageView www=findViewById(R.id.iconWebsite);
        ImageView tel=findViewById(R.id.iconContact);
        Compines compines = (Compines) getIntent().getSerializableExtra("compines");
        ImageView email=findViewById(R.id.iconEmail);
        TextView nameView = findViewById(R.id.textViewServices);
        ImageView location=findViewById(R.id.iconmap);
        ImageView imageView = findViewById(R.id.imageView8);
        ListView servicesListView = findViewById(R.id.servicesListView);
        ImageView agenda=findViewById(R.id.iconagenda);

        nameView.setText(compines.getName());
        imageView.setImageBitmap(compines.getImage());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, compines.getServices());
        servicesListView.setAdapter(adapter);
        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, CalanderActivity.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+compines.getLatitude()+","+compines.getLongitude()));
                startActivity(mapIntent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmailDialog(compines.getEmail());


            }
        });
        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String website = compines.getWebsite();
                if (!website.startsWith("http://") && !website.startsWith("https://")) {
                    website = "http://" + website;
                }
                Uri link = Uri.parse(website);
                Intent web = new Intent(Intent.ACTION_VIEW, link);
                startActivity(web);
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri number=Uri.parse("tel:"+compines.getPhone());
                Intent callIntent=new Intent(Intent.ACTION_DIAL,number);
                startActivity(callIntent);
            }
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

        // Create EditText for subject
        final EditText subjectInput = new EditText(this);
        subjectInput.setHint("Objet");
        layout.addView(subjectInput);

        // Create EditText for message
        final EditText messageInput = new EditText(this);
        messageInput.setHint("Message");
        layout.addView(messageInput);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envoyer un e-mail")
                .setView(layout)
                .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subject = subjectInput.getText().toString();
                        String message = messageInput.getText().toString();
                        sendEmail(email, subject, message);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showRatingDialog();
                            }
                        }, 3000); // Délai de 3 secondes
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the dialog
        builder.show();
    }
    private void showRatingDialog() {
        // Create a LinearLayout for the RatingBar
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create the RatingBar
        RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5); // Set the number of stars to 5
        ratingBar.setStepSize(1);  // Set step size to 1
        ratingBar.setRating(0);    // Initial rating value

        // Optional: Set layout params for the RatingBar
        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(ratingParams);

        // Add the RatingBar to the layout
        layout.addView(ratingBar);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Évaluez l'entreprise")
                .setView(layout)
                .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Process the rating here if needed
                        float rating = ratingBar.getRating();
                        Toast.makeText(DetailActivity.this, "Vous avez noté : " + rating, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Show the dialog
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
            // Utiliser Handler pour afficher le RatingBar après 3 secondes


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Aucune application d'e-mail n'est installée.", Toast.LENGTH_SHORT).show();
        }
    }
}