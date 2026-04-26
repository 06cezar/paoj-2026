package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

import java.util.Objects;

public class Address {
    private final String street;
    private final String city;
    private final String postalCode;

    public Address(String street, String city, String postalCode) {
        if (street == null || street.isEmpty()) throw new InvalidDataException("Strada este obligatorie.");
        if (city == null || city.isEmpty()) throw new InvalidDataException("Orasul este obligatoriu.");
        if (postalCode == null || postalCode.isEmpty()) throw new InvalidDataException("Codul postal este obligatoriu.");

        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getPostalCode() { return postalCode; }

    @Override
    public String toString() {
        return street + ", " + city + " " + postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address a = (Address) o;
        return street.equals(a.street) && city.equals(a.city) && postalCode.equals(a.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode);
    }
}
