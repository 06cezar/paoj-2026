package com.pao.project.fooddelivery.model;

import com.pao.project.fooddelivery.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// comenzile se sorteaza dupa data plasarii.
public class Order implements Comparable<Order> {
    private final String id;
    private final Client client;
    private final Restaurant restaurant;
    private final List<Product> products;
    private final DeliveryAddress deliveryAddress;
    private Driver driver;
    private OrderStatus status;
    private final LocalDateTime orderDate;
    private double totalPrice;

    public Order(String id, Client client, Restaurant restaurant, DeliveryAddress deliveryAddress) {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul comenzii este obligatoriu.");
        if (client == null) throw new InvalidDataException("Clientul este obligatoriu.");
        if (restaurant == null) throw new InvalidDataException("Restaurantul este obligatoriu.");
        if (deliveryAddress == null) throw new InvalidDataException("Adresa de livrare este obligatorie.");
        
        this.id = id;
        this.client = client;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.products = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = 0;
        // in realitate nu e asignat imediat la constructie soferul, se atribuie ulterior
    }

    public void addProduct(Product product) {
        if (product == null) throw new InvalidDataException("Produsul este obligatoriu.");
        products.add(product);
        totalPrice += product.getPrice();
    }

    public String getId() { return id; }
    public Client getClient() { return client; }
    public Restaurant getRestaurant() { return restaurant; }
    public List<Product> getProducts() { return new ArrayList<>(products); }
    public DeliveryAddress getDeliveryAddress() { return deliveryAddress; }
    public Driver getDriver() { return driver; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public double getTotalPrice() { return totalPrice; }

    public void setDriver(Driver driver) { this.driver = driver; }

    public void setStatus(OrderStatus status) {
        if (status == null) throw new InvalidDataException("Statusul comenzii este obligatoriu.");
        this.status = status;
    }

    @Override
    public int compareTo(Order other) {
        if (other == null) {
            throw new InvalidDataException("Comanda comparata este obligatorie.");
        }
        return this.orderDate.compareTo(other.orderDate);
    }

    @Override
    public String toString() {
        String driverText;
        if (driver == null) driverText = "neatribuit";
        else driverText = driver.getName();

        return "Order[id=" + id +
               ", client=" + client.getName() +
               ", restaurant=" + restaurant.getName() +
               ", total=" + totalPrice + " RON" +
               ", status=" + status +
               ", adresa=" + deliveryAddress +
               ", sofer=" + driverText +
               ", data=" + orderDate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order c = (Order) o;
        return id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
