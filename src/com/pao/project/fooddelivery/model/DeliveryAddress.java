package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

// clasa imutabila: toate campurile sunt final, nu exista setteri
public final class DeliveryAddress extends Address {
    private final String id;
    private final String details; // optional

    public DeliveryAddress(String id, String street, String city, String postalCode, String details) {
        super(street, city, postalCode); // validarile pentru strada, oras, cod postal sunt in Address
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul este obligatoriu.");

        this.id = id;
        this.details = details;
    }

    public String getId() { return id; }
    public String getDetails() { return details; }

    @Override
    public String toString() {
        if (details == null || details.isEmpty()) return super.toString();
        else return super.toString() + " (" + details + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryAddress)) return false;
        DeliveryAddress a = (DeliveryAddress) o;
        return id.equals(a.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
