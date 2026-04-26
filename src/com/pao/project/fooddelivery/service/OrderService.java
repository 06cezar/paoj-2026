package com.pao.project.fooddelivery.service;

import com.pao.project.fooddelivery.exception.DuplicateIdException;
import com.pao.project.fooddelivery.exception.DriverNotFoundException;
import com.pao.project.fooddelivery.exception.DriverUnavailableException;
import com.pao.project.fooddelivery.exception.InvalidDataException;
import com.pao.project.fooddelivery.exception.InvalidOrderException;
import com.pao.project.fooddelivery.exception.OrderNotFoundException;
import com.pao.project.fooddelivery.model.Client;
import com.pao.project.fooddelivery.model.DeliveryAddress;
import com.pao.project.fooddelivery.model.Driver;
import com.pao.project.fooddelivery.model.Order;
import com.pao.project.fooddelivery.model.OrderStatus;
import com.pao.project.fooddelivery.model.Product;
import com.pao.project.fooddelivery.model.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderService {
    private static OrderService instance;

    private final List<Order> orders;     // List<Order> - sortabila prin Comparable
    private final List<Driver> drivers;
    private int counter = 1;              // contor separat - nu depinde de size()

    private OrderService() {
        this.orders = new ArrayList<>();
        this.drivers = new ArrayList<>();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    // Actiunea 3: Inregistreaza sofer
    public void registerDriver(Driver driver) {
        if (driver == null) throw new InvalidDataException("Soferul este obligatoriu.");
        for (Driver d : drivers) {
            if (d.getId().equals(driver.getId())) {
                throw new DuplicateIdException("Sofer cu id=" + driver.getId() + " exista deja.");
            }
        }
        drivers.add(driver);
        System.out.println("[OK] Sofer inregistrat: " + driver.getName());
    }

    // Actiunea 5: Plaseaza comanda
    public Order placeOrder(Client client, Restaurant restaurant,
                            List<String> productIds, DeliveryAddress address) {
        if (client == null) throw new InvalidDataException("Clientul este obligatoriu.");
        if (restaurant == null) throw new InvalidDataException("Restaurantul este obligatoriu.");
        if (address == null) throw new InvalidDataException("Adresa de livrare este obligatorie.");
        if (productIds == null || productIds.isEmpty()) {
            throw new InvalidOrderException("Comanda trebuie sa contina cel putin un produs.");
        }

        String orderId = "CMD-" + counter++;
        Order order = new Order(orderId, client, restaurant, address);

        for (String productId : productIds) {
            Product product = restaurant.getMenu().findProductById(productId);
            if (product == null) {
                throw new InvalidOrderException("Produsul cu id=" + productId + " nu exista in meniu.");
            }
            order.addProduct(product);
        }

        orders.add(order);
        System.out.println("[OK] Comanda plasata: " + orderId + " | Total: " + order.getTotalPrice() + " RON");
        return order;
    }

    // Actiunea 6: Atribuie sofer la comanda
    public void assignDriver(String orderId, String driverId) throws OrderNotFoundException, DriverNotFoundException, DriverUnavailableException {
        Order order = findOrderById(orderId);
        Driver driver = findDriverById(driverId);

        if (!driver.isAvailable()) {
            throw new DriverUnavailableException("Soferul " + driver.getName() + " nu este disponibil.");
        }

        // elibereaza soferul vechi daca exista
        if (order.getDriver() != null) {
            order.getDriver().setAvailable(true);
        }
        order.setDriver(driver);
        order.setStatus(OrderStatus.IN_DELIVERY);
        driver.setAvailable(false);
        System.out.println("[OK] Sofer " + driver.getName() + " atribuit comenzii " + orderId);
    }

    // Actiunea 7: Finalizeaza comanda
    public void completeOrder(String orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId);
        order.setStatus(OrderStatus.DELIVERED);
        if (order.getDriver() != null) {
            order.getDriver().setAvailable(true);
        }
        System.out.println("[OK] Comanda " + orderId + " livrata cu succes.");
    }

    // Actiunea 9: Listeaza comenzile unui client (sortate dupa data)
    public List<Order> getClientOrders(String clientId) {
        if (clientId == null || clientId.isEmpty()) throw new InvalidDataException("ID-ul clientului este obligatoriu.");
        List<Order> results = new ArrayList<>();
        for (Order o : orders) {
            if (o.getClient().getId().equals(clientId)) {
                results.add(o);
            }
        }
        Collections.sort(results);
        return results;
    }

    // Actiunea 10: Afiseaza comenzile active
    public List<Order> getActiveOrders() {
        List<Order> active = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() != OrderStatus.DELIVERED && o.getStatus() != OrderStatus.CANCELLED) {
                active.add(o);
            }
        }
        Collections.sort(active);
        return active;
    }

    public void deleteOrder(String orderId) throws OrderNotFoundException {
        Order order = findOrderById(orderId);
        // elibereaza soferul daca comanda era in livrare
        if (order.getDriver() != null && !order.getStatus().equals(OrderStatus.DELIVERED)) {
            order.getDriver().setAvailable(true);
        }
        orders.remove(order);
        System.out.println("[OK] Comanda stearsa: " + orderId);
    }

    public Order findOrderById(String id) throws OrderNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul comenzii este obligatoriu.");
        for (Order o : orders) {
            if (o.getId().equals(id)) return o;
        }
        throw new OrderNotFoundException("Comanda cu id=" + id + " nu a fost gasita.");
    }

    public Driver findDriverById(String id) throws DriverNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul soferului este obligatoriu.");
        for (Driver d : drivers) {
            if (d.getId().equals(id)) return d;
        }
        throw new DriverNotFoundException("Sofer cu id=" + id + " nu a fost gasit.");
    }

    public void deleteDriver(String id) throws DriverNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul soferului este obligatoriu.");
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId().equals(id)) {
                drivers.remove(i);
                System.out.println("[OK] Sofer sters: id=" + id);
                return;
            }
        }
        throw new DriverNotFoundException("Sofer cu id=" + id + " nu a fost gasit.");
    }

    public List<Order> getAllOrdersSorted() {
        List<Order> sorted = new ArrayList<>(orders);
        Collections.sort(sorted);
        return sorted;
    }

    public List<Driver> getAllDrivers() {
        return Collections.unmodifiableList(drivers);
    }
}
