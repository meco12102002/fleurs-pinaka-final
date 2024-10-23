package com.example.fleursonthego.Models;

public class Product {
    private String name;
    private String description;
    private String image; // Change to match Firebase field
    private double price;

    // No-argument constructor required for Firebase
    public Product() {
    }

    // Parameterized constructor
    public Product(String name, String description, String image, double price) {
        this.name = name;
        this.description = description;
        this.image = image; // Update parameter name
        this.price = price;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() { // Update method name
        return image;
    }

    public void setImage(String image) { // Update method name
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
