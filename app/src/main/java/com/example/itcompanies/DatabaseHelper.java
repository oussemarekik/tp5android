package com.example.itcompanies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.itcompanies.utils.PasswordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2; // Version incrémentée
    private static   int ajout=0;
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
    // Constantes pour la table user
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAILUSER = "email";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Script de création de la table user
    private static final String TABLE_USER_CREATE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_EMAILUSER + " TEXT, " +
                    COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";
    Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Stockage du contexte

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        // Créer la table user
        db.execSQL(TABLE_USER_CREATE);
        addSampleData(context,db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_COMPANIES + " ADD COLUMN " + COLUMN_IMAGE + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL(TABLE_USER_CREATE); // Créer la table user si la version est mise à jour
            insertUser("oussema","123456","rekik@gmail.com");
        }
    }
    //autheniticate
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Récupérer le mot de passe haché depuis la base de données
        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USER +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(0);
            cursor.close();
            db.close();

            // Hacher le mot de passe saisi et le comparer avec celui stocké
            String hashedPassword = PasswordUtils.hashPassword(password);
            return storedHashedPassword.equals(hashedPassword);
        }

        cursor.close();
        db.close();
        return false; // Utilisateur non trouvé ou mot de passe incorrect
    }
    // ajouter user
    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Hacher le mot de passe avant de l'insérer
        String hashedPassword = PasswordUtils.hashPassword(password);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_EMAILUSER, email);

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }
    // Méthode pour supprimer une entreprise par ID
    public boolean deleteCompanyById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_COMPANIES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0; // Retourne true si une ou plusieurs lignes ont été supprimées
    }
    void addSampleData(Context context,SQLiteDatabase db) {
        // Convertir les ressources drawable en Bitmap
            Bitmap actiaImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.actia);
            Bitmap fodImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.fod);
            Bitmap sofrecomImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sofrecom);
            Bitmap sparkImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.spark);
            Bitmap udiniImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.udini);
            Bitmap primatecImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.primatec);

            // Insérer les entreprises avec les images sous forme de Bitmap
            ArrayList<String> services1 = new ArrayList<>(Arrays.asList("Web Development", "Mobile Development"));
        insertCompanyAddSampleData("Actia", services1, "www.actia.com", "+123456789", actiaImage, "info@actia.com", "34.8343499806865", "10.751750171336644", db);

            ArrayList<String> services2 = new ArrayList<>(Arrays.asList("Expertise", "Consulting"));
        insertCompanyAddSampleData("FOD", services2, "www.fod.com", "+987654321", fodImage, "contact@fod.com", "48.8566", "2.3522", db);

            ArrayList<String> services3 = new ArrayList<>(Arrays.asList("Telecommunications", "Cloud Solutions"));
        insertCompanyAddSampleData("Sofrecom", services3, "www.sofrecom.com", "+5647382910", sofrecomImage, "support@sofrecom.com", "40.7128", "-74.0060", db);

            ArrayList<String> services4 = new ArrayList<>(Arrays.asList("AI Development", "Data Science"));
        insertCompanyAddSampleData("Spark", services4, "www.spark.com", "+1029384756", sparkImage, "hello@spark.com", "34.0522", "-118.2437", db);

            ArrayList<String> services5 = new ArrayList<>(Arrays.asList("Digital Marketing", "SEO"));
        insertCompanyAddSampleData("Udini", services5, "www.udini.com", "+1928374650", udiniImage, "contact@udini.com", "51.5074", "-0.1278", db);

            ArrayList<String> services6 = new ArrayList<>(Arrays.asList("Embedded Systems", "Automotive"));
        insertCompanyAddSampleData("Primatec", services6, "www.primatec.com", "+2039485761", primatecImage, "info@primatec.com", "37.7749", "-122.4194", db);




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
    // Méthode pour récupérer une entreprise par son nom
    public Compines getCompanyByName(String name, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_SERVICES, COLUMN_WEBSITE, COLUMN_PHONE, COLUMN_IMAGE, COLUMN_EMAIL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_COMPANIES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String companyName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
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

            // Créer et retourner un objet Compines
            Compines company = new Compines(image, companyName, services, website, phone, email, latitude, longitude);
            cursor.close();
            db.close();
            return company;
        } else {
            if (cursor != null) cursor.close();
            db.close();
            return null; // Aucun enregistrement trouvé
        }
    }

    // Méthode pour récupérer une entreprise par son ID
    public Compines getCompanyById(int id, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_SERVICES, COLUMN_WEBSITE, COLUMN_PHONE, COLUMN_IMAGE, COLUMN_EMAIL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_COMPANIES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
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

            // Créer et retourner un objet Compines
            Compines company = new Compines(image, name, services, website, phone, email, latitude, longitude);
            cursor.close();
            db.close();
            return company;
        } else {
            cursor.close();
            db.close();
            return null; // Aucun enregistrement trouvé
        }
    }
    public Compines getCompanyByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_SERVICES, COLUMN_WEBSITE, COLUMN_PHONE, COLUMN_IMAGE, COLUMN_EMAIL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_COMPANIES, columns, selection, selectionArgs, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                String companyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String servicesString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERVICES));
                ArrayList<String> services = new ArrayList<>(Arrays.asList(servicesString.split(", ")));
                String website = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEBSITE));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));

                // Charger l'image depuis le chemin
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                Bitmap image = BitmapFactory.decodeFile(imagePath);

                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));

                // Créer et retourner l'objet Compines
                Compines c= new Compines(image, companyName, services, website, phone, email, latitude, longitude);
                c.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                return  c;
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erreur lors de la lecture de la base : " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return null;
    }

    // Méthode pour mettre à jour une entreprise
    public int updateCompany(int id, String name, ArrayList<String> services, String website, String phone,
                             Bitmap image, String email, String latitude, String longitude, Context context) {
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

        // Sélectionner l'entreprise à mettre à jour par son ID
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // Mettre à jour l'entreprise dans la base de données
        int rowsUpdated = db.update(TABLE_COMPANIES, values, selection, selectionArgs);
        db.close();
        return rowsUpdated; // Retourner le nombre de lignes mises à jour
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
    public void insertCompanyAddSampleData(String name, ArrayList<String> services, String website, String phone,
                                           Bitmap image, String email, String latitude, String longitude,
                                           SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        // Convertir la liste de services en chaîne
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

        // Insérer les valeurs dans la table
        db.insert(TABLE_COMPANIES, null, values);
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
