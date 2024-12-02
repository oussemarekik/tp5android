package com.example.itcompanies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditCompanyActivity extends Activity {
    private EditText nameEditText, servicesEditText, websiteEditText, phoneEditText, emailEditText, latitudeEditText, longitudeEditText;
    private ImageView companyImageView;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private String companyName;  // Nom de l'entreprise
    private Compines company;    // L'objet Compines

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        // Initialiser les vues
        nameEditText = findViewById(R.id.nameEditText);
        servicesEditText = findViewById(R.id.servicesEditText);
        websiteEditText = findViewById(R.id.websiteEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        companyImageView = findViewById(R.id.companyImageView);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DatabaseHelper(this);

        // Récupérer le nom de l'entreprise depuis l'intent
        companyName = getIntent().getStringExtra("companyName");

        // Récupérer l'objet 'Compines' basé sur le nom de l'entreprise
        company = dbHelper.getCompanyByName(companyName);  // Supposons que vous avez une méthode pour récupérer une entreprise par son nom

        if (company != null) {
            populateFields();  // Remplir les champs avec les données de l'entreprise existante
        }

        // Gérer le clic du bouton "Save"
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCompany();  // Appeler la méthode pour mettre à jour l'entreprise
            }
        });
    }

    private void populateFields() {
        // Remplir les champs avec les données de l'entreprise existante
        nameEditText.setText(company.getName());
        servicesEditText.setText(String.join(", ", company.getServices()));  // Remplir les services
        websiteEditText.setText(company.getWebsite());
        phoneEditText.setText(company.getPhone());
        emailEditText.setText(company.getEmail());
        latitudeEditText.setText(String.valueOf(company.getLatitude()));  // Utiliser 'String.valueOf' pour le cas des doubles
        longitudeEditText.setText(String.valueOf(company.getLongitude()));
        companyImageView.setImageBitmap(company.getImage());
    }

    private void updateCompany() {
        // Récupérer les données modifiées par l'utilisateur
        String name = nameEditText.getText().toString();
        ArrayList<String> services = new ArrayList<>();
        String[] servicesArray = servicesEditText.getText().toString().split(", ");
        for (String service : servicesArray) {
            services.add(service.trim());
        }
        String website = websiteEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();  // Conversion en double
        String longitude = longitudeEditText.getText().toString();  // Conversion en double

        // Garder l'image actuelle ou permettre à l'utilisateur de la changer
        Bitmap image = company.getImage();

        // Mettre à jour l'entreprise dans la base de données en utilisant l'ID
        int rowsUpdated = dbHelper.updateCompany(company.getId(), name, services, website, phone, image, email, latitude, longitude,this);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Entreprise mise à jour avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("compinesName", name);
            this.startActivity(intent);  // Fermer l'activité après la mise à jour
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour de l'entreprise", Toast.LENGTH_SHORT).show();
        }
    }
}
