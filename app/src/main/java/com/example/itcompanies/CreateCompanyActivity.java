package com.example.itcompanies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CreateCompanyActivity extends AppCompatActivity {

    private ImageView imageViewCompanyLogo;
    private Bitmap selectedImageBitmap; // To store the selected image as Bitmap
    private EditText editTextCompanyName;
    private EditText editTextCompanyDescription;
    private EditText editTextCompanyWebsite;
    private EditText editTextCompanyPhone;
    private EditText editTextCompanyEmail;
    private EditText editTextCompanyLatitude;
    private EditText editTextCompanyLongitude;
    private Button buttonSaveCompany;

    private final ActivityResultLauncher<String> pickImageResult = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                            imageViewCompanyLogo.setImageBitmap(selectedImageBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        // Initialize views
        imageViewCompanyLogo = findViewById(R.id.imageViewCompanyLogo);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextCompanyDescription = findViewById(R.id.editTextCompanyDescription);
        editTextCompanyWebsite = findViewById(R.id.editTextCompanyWebsite);
        editTextCompanyPhone = findViewById(R.id.editTextCompanyPhone);
        editTextCompanyEmail = findViewById(R.id.editTextCompanyEmail);
        editTextCompanyLatitude = findViewById(R.id.editTextCompanyLatitude);
        editTextCompanyLongitude = findViewById(R.id.editTextCompanyLongitude);
        buttonSaveCompany = findViewById(R.id.buttonSaveCompany);

        // Check and request permissions if needed
        checkPermissions();

        // Set OnClickListener for the ImageView to open the gallery
        imageViewCompanyLogo.setOnClickListener(v -> openGallery());

        // Set OnClickListener for Save button to handle form submission
        buttonSaveCompany.setOnClickListener(v -> saveCompany());
    }

    // Open the gallery to pick an image using ActivityResultContracts.GetContent
    private void openGallery() {
        pickImageResult.launch("image/*");
    }

    // Handle Save button click and submit form data
    private void saveCompany() {
        String companyName = editTextCompanyName.getText().toString().trim();
        String companyDescription = editTextCompanyDescription.getText().toString().trim();
        String companyWebsite = editTextCompanyWebsite.getText().toString().trim();
        String companyPhone = editTextCompanyPhone.getText().toString().trim();
        String companyEmail = editTextCompanyEmail.getText().toString().trim();
        String companyLatitude = editTextCompanyLatitude.getText().toString().trim();
        String companyLongitude = editTextCompanyLongitude.getText().toString().trim();

        // Validate the inputs
        if (companyName.isEmpty() || companyDescription.isEmpty() || companyWebsite.isEmpty() ||
                companyPhone.isEmpty() || companyEmail.isEmpty() || companyLatitude.isEmpty() || companyLongitude.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the Bitmap to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] imageData = outputStream.toByteArray();

        // Insert the company data into the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<String> services = new ArrayList<>();
        services.add(companyDescription); // Here, you can replace with more services if needed

        dbHelper.insertCompany(companyName, services, companyWebsite, companyPhone, imageData, companyEmail, companyLatitude, companyLongitude,this);

        Toast.makeText(this, "Company Saved Successfully", Toast.LENGTH_SHORT).show();

        // Redirect to MainActivity
        Intent intent = new Intent(CreateCompanyActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Check if permissions are granted, if not request permissions
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
