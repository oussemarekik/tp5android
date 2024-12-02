package com.example.itcompanies;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
public class Compines implements Parcelable {
    private Bitmap image;  // Changer de int à Bitmap
    private String name;
    private ArrayList<String> services;
    private String website;
    private String phone;
    private String email;
    private String latitude;
    private String longitude;
    private int id;

    // CREATOR pour Parcelable
    public static final Creator<Compines> CREATOR = new Creator<Compines>() {
        @Override
        public Compines createFromParcel(Parcel in) {
            return new Compines(in);
        }

        @Override
        public Compines[] newArray(int size) {
            return new Compines[size];
        }
    };


    // Méthode pour convertir le Bitmap en byte[]
    public byte[] getImageBytes() {
        if (image == null) {
            Log.e("Compines", "Le Bitmap est null. Impossible de le convertir en bytes.");
            return null; // Retournez null ou une valeur par défaut
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


    // Constructor
    public Compines(Bitmap image, String name, ArrayList<String> services, String website, String phone, String email, String latitude, String longitude) {
        this.image = image;
        this.name = name;
        this.services = services;
        this.website = website;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public void setId(int id){
        this.id=id;
    }
    // Constructor with id
    public Compines(int id, String companyName, ArrayList<String> serviceList, String website, String phone, String email, String latitude, String longitude, Bitmap image) {
        this.id = id;  // Initialisation de l'ID
        this.name = companyName;  // Nom de l'entreprise
        this.services = serviceList;  // Liste des services
        this.website = website;  // Site web
        this.phone = phone;  // Numéro de téléphone
        this.email = email;  // Email
        this.latitude = latitude;  // Latitude
        this.longitude = longitude;  // Longitude
        this.image = image;  // Image de l'entreprise
    }

    // Constructor from Parcel
    protected Compines(Parcel in) {
        byte[] imageBytes = in.createByteArray();
        if (imageBytes != null) {
            image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);  // Conversion du byte[] en Bitmap
        }
        name = in.readString();
        services = in.createStringArrayList();
        website = in.readString();
        phone = in.readString();
        email = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        id = in.readInt();
    }

    // Getter pour l'ID
    public int getId() {
        return id;
    }

    // Getters
    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    // toString method for debugging or display purposes
    @Override
    public String toString() {
        return "Compines{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", services=" + services +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeStringList(services);
        dest.writeString(website);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(latitude);
        dest.writeString(longitude);
        // Convert Bitmap to byte[] and write to Parcel
        dest.writeByteArray(getImageBytes());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

