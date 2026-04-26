package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

public class Client extends Person {
    private final List<DeliveryAddress> addresses;

    public Client(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.addresses = new ArrayList<>();
    }

    @Override
    public String getRole() { return "Client"; }

    public void addAddress(DeliveryAddress address) {
        if (address == null) throw new InvalidDataException("Adresa este obligatorie.");
        addresses.add(address);
    }

    public List<DeliveryAddress> getAddresses() {
        return new ArrayList<>(addresses); // dau o copie, nu las modificarea prin referinta
    }

    @Override
    public String toString() {
        return super.toString() + ", adrese=" + addresses.size() + "]";
        // doar nr de adrese
    }
}
