package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

// clasa imutabila: toate campurile sunt final, nu exista setteri
public final class Product {
    private final String id;
    private final String name;
    private final double price; // primitiva, nu poate fi null
    private final String description; // optionala
    private final String category;

    public Product(String id, String name, double price, String description, String category) {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul este obligatoriu.");
        if (name == null || name.isEmpty()) throw new InvalidDataException("Numele produsului este obligatoriu.");
        if (price < 0) throw new InvalidDataException("Pretul produsului nu poate fi negativ.");
        if (category == null || category.isEmpty()) throw new InvalidDataException("Categoria este obligatorie.");
        
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        String base = "Product[id=" + id + ", nume=" + name + ", pret=" + price + " RON, categorie=" + category;
        if (description == null || description.isEmpty()) return base + "]";
        else return base + ", descriere=" + description + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
