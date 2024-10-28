package com.example.itcompanies;

import java.io.Serializable;
import java.util.ArrayList;

public class Compines implements Serializable {
    private int image;
    private String name;
    private ArrayList<String> services;
    private String website;
    private String phone;

    public Compines(int image, String name, ArrayList<String> services, String website, String phone) {
        this.image = image;
        this.name = name;
        this.services = services;
        this.website = website;
        this.phone = phone;
    }

    public int getImage() {
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
    @Override
    public String toString() {
        return "Compines{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", services=" + services +
                ", website='" + website + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
