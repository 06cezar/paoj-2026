package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;
import com.pao.project.fooddelivery.exception.DuplicateIdException;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final List<Product> products;

    public Menu() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product == null) throw new InvalidDataException("Produsul este obligatoriu.");
        if (products.contains(product)) throw new DuplicateIdException("Exista deja un produs cu ID-ul " + product.getId() + ".");
        // metoda contains foloseste metoda equals din cls => dupa ID
        products.add(product);
    }

    public boolean removeProduct(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new InvalidDataException("ID-ul produsului este obligatoriu.");
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) {
                products.remove(i);
                return true;
            }
        }

        return false;
    }

    public Product findProductById(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidDataException("ID-ul produsului este obligatoriu.");
        }

        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }

        return null;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public List<Product> getProductsSortedByPrice() {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(new PriceComparator());
        return sorted;
    }


    @Override
    public String toString() {
        return "Menu[" + products.size() + " produse]";
    }
}
