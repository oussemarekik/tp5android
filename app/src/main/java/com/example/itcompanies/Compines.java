package com.example.itcompanies;


import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Compines implements Serializable {
    private Bitmap image;  // Changer de int à Bitmap
    private String name;
    private ArrayList<String> services;
    private String website;
    private String phone;
    private String email;
    private String latitude;
    private String longitude;

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
}
