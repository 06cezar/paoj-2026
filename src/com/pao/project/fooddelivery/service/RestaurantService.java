package com.pao.project.fooddelivery.service;

import com.pao.project.fooddelivery.exception.ClientNotFoundException;
import com.pao.project.fooddelivery.exception.DuplicateIdException;
import com.pao.project.fooddelivery.exception.InvalidDataException;
import com.pao.project.fooddelivery.exception.RestaurantNotFoundException;
import com.pao.project.fooddelivery.model.Address;
import com.pao.project.fooddelivery.model.Client;
import com.pao.project.fooddelivery.model.Product;
import com.pao.project.fooddelivery.model.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestaurantService {
    private static RestaurantService instance;

    private final Map<String, Restaurant> restaurants;
    private final List<Client> clients;

    private RestaurantService() {
        this.restaurants = new LinkedHashMap<>();
        this.clients = new ArrayList<>();
    }

    public static synchronized RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    // Actiunea 1
    public void addRestaurant(Restaurant restaurant) {
        if (restaurant == null) throw new InvalidDataException("Restaurantul este obligatoriu.");
        if (restaurants.containsKey(restaurant.getId())) {
            throw new DuplicateIdException("Restaurant cu id=" + restaurant.getId() + " exista deja.");
        }
        restaurants.put(restaurant.getId(), restaurant);
        AuditService.getInstance().log("adauga_restaurant");
        System.out.println("[OK] Restaurant adaugat: " + restaurant.getName());
    }

    // Actiunea 2
    public void addClient(Client client) {
        if (client == null) throw new InvalidDataException("Clientul este obligatoriu.");
        for (Client c : clients) {
            if (c.getId().equals(client.getId())) {
                throw new DuplicateIdException("Client cu id=" + client.getId() + " exista deja.");
            }
        }
        clients.add(client);
        AuditService.getInstance().log("adauga_client");
        System.out.println("[OK] Client inregistrat: " + client.getName());
    }

    // Actiunea 4
    public void addProductToMenu(String restaurantId, Product product) throws RestaurantNotFoundException {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.getMenu().addProduct(product);
        AuditService.getInstance().log("adauga_produs_la_meniu");
        System.out.println("[OK] Produs adaugat: " + product.getName() + " -> " + restaurant.getName());
    }

    // Actiunea 8
    public List<Restaurant> findByCategory(String category) {
        if (category == null || category.isEmpty()) throw new InvalidDataException("Categoria este obligatorie.");
        List<Restaurant> results = new ArrayList<>();
        for (Restaurant r : restaurants.values()) {
            if (r.getCategory().equalsIgnoreCase(category)) {
                results.add(r);
            }
        }
        AuditService.getInstance().log("cauta_restaurante_dupa_categorie");
        return results;
    }

    public void updateRestaurant(String id, String newName, Address newAddress, String newCategory)
            throws RestaurantNotFoundException {
        Restaurant restaurant = findRestaurantById(id);
        if (newName != null && !newName.isEmpty()) restaurant.setName(newName);
        if (newAddress != null) restaurant.setAddress(newAddress);
        if (newCategory != null && !newCategory.isEmpty()) restaurant.setCategory(newCategory);
        System.out.println("[OK] Restaurant actualizat: " + restaurant.getName());
    }

    public void deleteRestaurant(String id) throws RestaurantNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul restaurantului este obligatoriu.");
        if (!restaurants.containsKey(id)) {
            throw new RestaurantNotFoundException("Restaurant cu id=" + id + " nu a fost gasit.");
        }
        restaurants.remove(id);
        System.out.println("[OK] Restaurant sters: id=" + id);
    }

    public void deleteClient(String id) {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul clientului este obligatoriu.");
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(id)) {
                clients.remove(i);
                System.out.println("[OK] Client sters: id=" + id);
                return;
            }
        }
        System.out.println("[WARN] Client cu id=" + id + " nu a fost gasit.");
    }

    public Restaurant findRestaurantById(String id) throws RestaurantNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul restaurantului este obligatoriu.");
        Restaurant r = restaurants.get(id);
        if (r == null) throw new RestaurantNotFoundException("Restaurant cu id=" + id + " nu a fost gasit.");
        return r;
    }

    public Client findClientById(String id) throws ClientNotFoundException {
        if (id == null || id.isEmpty()) throw new InvalidDataException("ID-ul clientului este obligatoriu.");
        for (Client c : clients) {
            if (c.getId().equals(id)) return c;
        }
        throw new ClientNotFoundException("Client cu id=" + id + " nu a fost gasit.");
    }

    public Map<String, Restaurant> getAllRestaurants() {
        return Collections.unmodifiableMap(restaurants);
    }

    public List<Client> getAllClients() {
        return Collections.unmodifiableList(clients);
    }
}
