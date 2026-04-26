package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

public class Restaurant {
    private final String id;
    private String name;
    private Address address;
    private String category;
    private final Menu menu;

    public Restaurant(String id, String name, Address address, String category) {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul este obligatoriu.");
        if (name == null || name.isEmpty()) throw new InvalidDataException("Numele restaurantului este obligatoriu.");
        if (address == null) throw new InvalidDataException("Adresa restaurantului este obligatorie.");
        if (category == null || category.isEmpty()) throw new InvalidDataException("Categoria este obligatorie.");

        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.menu = new Menu();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Address getAddress() { return address; }
    public String getCategory() { return category; }
    public Menu getMenu() { return menu; }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new InvalidDataException("Numele restaurantului este obligatoriu.");
        this.name = name;
    }

    public void setAddress(Address address) {
        if (address == null) throw new InvalidDataException("Adresa restaurantului este obligatorie.");
        this.address = address;
    }

    public void setCategory(String category) {
        if (category == null || category.isEmpty()) throw new InvalidDataException("Categoria este obligatorie.");
        this.category = category;
    }

    @Override
    public String toString() {
        return "Restaurant[id=" + id + ", nume=" + name + ", categorie=" + category + ", adresa=" + address + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant r = (Restaurant) o;
        return id.equals(r.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
