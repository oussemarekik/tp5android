package com.example.itcompanies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2; // Version incrémentée

    public static final String TABLE_COMPANIES = "companies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SERVICES = "services";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IMAGE = "image"; // Stockera le chemin
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_COMPANIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_SERVICES + " TEXT, " +
                    COLUMN_WEBSITE + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " + // Changement en TEXT
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_LATITUDE + " TEXT, " +
                    COLUMN_LONGITUDE + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_COMPANIES + " ADD COLUMN " + COLUMN_IMAGE + " TEXT");
        }
    }
    void addSampleData(Context context) {
        // Convertir les ressources drawable en Bitmap

        Bitmap actiaImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.actia);
        Bitmap fodImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.fod);
        Bitmap sofrecomImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sofrecom);
        Bitmap sparkImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.spark);
        Bitmap udiniImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.udini);
        Bitmap primatecImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.primatec);

        // Insérer les entreprises avec les images sous forme de Bitmap
        ArrayList<String> services1 = new ArrayList<>(Arrays.asList("Web Development", "Mobile Development"));
        insertCompany("Actia", services1, "www.actia.com", "+123456789", actiaImage, "info@actia.com", "34.8343499806865", "10.751750171336644", context);

        ArrayList<String> services2 = new ArrayList<>(Arrays.asList("Expertise", "Consulting"));
        insertCompany("FOD", services2, "www.fod.com", "+987654321", fodImage, "contact@fod.com", "48.8566", "2.3522", context);

        ArrayList<String> services3 = new ArrayList<>(Arrays.asList("Telecommunications", "Cloud Solutions"));
        insertCompany("Sofrecom", services3, "www.sofrecom.com", "+5647382910", sofrecomImage, "support@sofrecom.com", "40.7128", "-74.0060", context);

        ArrayList<String> services4 = new ArrayList<>(Arrays.asList("AI Development", "Data Science"));
        insertCompany("Spark", services4, "www.spark.com", "+1029384756", sparkImage, "hello@spark.com", "34.0522", "-118.2437", context);

        ArrayList<String> services5 = new ArrayList<>(Arrays.asList("Digital Marketing", "SEO"));
        insertCompany("Udini", services5, "www.udini.com", "+1928374650", udiniImage, "contact@udini.com", "51.5074", "-0.1278", context);

        ArrayList<String> services6 = new ArrayList<>(Arrays.asList("Embedded Systems", "Automotive"));
        insertCompany("Primatec", services6, "www.primatec.com", "+2039485761", primatecImage, "info@primatec.com", "37.7749", "-122.4194", context);
    }

    // Méthode pour sauvegarder une image dans le stockage interne
    private String saveImageToInternalStorage(Context context, Bitmap bitmap, String imageName) {
        File directory = context.getDir("images", Context.MODE_PRIVATE); // Répertoire interne
        File imageFile = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Sauvegarde l'image
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return imageFile.getAbsolutePath(); // Retourne le chemin de l'image
    }

    // Insérer une entreprise avec stockage d'image
    public void insertCompany(String name, ArrayList<String> services, String website, String phone,
                              Bitmap image, String email, String latitude, String longitude,
                              Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        String servicesString = String.join(", ", services);
        values.put(COLUMN_SERVICES, servicesString);

        values.put(COLUMN_WEBSITE, website);
        values.put(COLUMN_PHONE, phone);

        // Sauvegarder l'image et stocker son chemin
        String imagePath = saveImageToInternalStorage(context, image, name);
        values.put(COLUMN_IMAGE, imagePath);

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        db.insert(TABLE_COMPANIES, null, values);
    }
    public void insertCompany(String name, ArrayList<String> services, String website, String phone,
                              byte[] image, String email, String latitude, String longitude,
                              Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        String servicesString = String.join(", ", services);
        values.put(COLUMN_SERVICES, servicesString);

        values.put(COLUMN_WEBSITE, website);
        values.put(COLUMN_PHONE, phone);

        // Sauvegarder l'image et stocker son chemin
        String imagePath = saveImageToInternalStorage(context, image, name);
        values.put(COLUMN_IMAGE, imagePath);

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        db.insert(TABLE_COMPANIES, null, values);
    }
    public String saveImageToInternalStorage(Context context, byte[] imageData, String imageName) {
        // Convertir le byte[] en Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        // Sauvegarder le Bitmap en interne
        File directory = context.getFilesDir();
        File imageFile = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Sauvegarde du bitmap
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath(); // Retourne le chemin du fichier sauvegardé
    }
    // Méthode pour récupérer toutes les entreprises
    public ArrayList<Compines> getAllCompanies(Context context) {
        ArrayList<Compines> companies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPANIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String servicesString = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICES));
                ArrayList<String> services = new ArrayList<>(Arrays.asList(servicesString.split(", ")));
                String website = cursor.getString(cursor.getColumnIndex(COLUMN_WEBSITE));
                String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));

                // Charger l'image depuis son chemin
                String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                Bitmap image = BitmapFactory.decodeFile(imagePath);

                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE));

                Compines company = new Compines(image, name, services, website, phone, email, latitude, longitude);
                companies.add(company);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return companies;
    }
}
