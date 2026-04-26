package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

public class Driver extends Person {
    private double rating;
    private boolean available;

    public Driver(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.rating = 5.0; // initial rating perfect
        this.available = true; // si disponibil
    }

    @Override
    public String getRole() { return "Driver"; }

    public double getRating() { return rating; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setRating(double rating) { 
        if (rating < 0 || rating > 5) {
            throw new InvalidDataException("Rating-ul trebuie sa fie intre 0 si 5.");
        }
        this.rating = rating; 
    }

    @Override
    public String toString() {
        String status;
        if(available) status = "disponibil";
        else status = "indisponibil";
        return super.toString() + ", rating=" + rating + ", status=" + status + "]";
    }
}
