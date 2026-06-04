package com.pao.project.fooddelivery.repository;

import com.pao.project.fooddelivery.model.Address;
import com.pao.project.fooddelivery.model.Product;
import com.pao.project.fooddelivery.model.Restaurant;
import com.pao.project.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantRepository implements Repository<Restaurant, String> {
    private static RestaurantRepository instance;
    private final Connection connection;

    private RestaurantRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public static synchronized RestaurantRepository getInstance() {
        if (instance == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    @Override
    public void save(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (id, name, street, city, postal_code, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getId());
            stmt.setString(2, restaurant.getName());
            stmt.setString(3, restaurant.getAddress().getStreet());
            stmt.setString(4, restaurant.getAddress().getCity());
            stmt.setString(5, restaurant.getAddress().getPostalCode());
            stmt.setString(6, restaurant.getCategory());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea restaurantului: " + e.getMessage(), e);
        }
    }

    public void saveProduct(Product product, String restaurantId) {
        String sql = "INSERT INTO products (id, name, price, description, category, restaurant_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getCategory());
            stmt.setString(6, restaurantId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea produsului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Restaurant> findById(String id) {
        String sql = "SELECT * FROM restaurants WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Restaurant r = mapRow(rs);
                    loadMenu(r);
                    return Optional.of(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea restaurantului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Restaurant> findAll() {
        String sql = "SELECT * FROM restaurants";
        List<Restaurant> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Restaurant r = mapRow(rs);
                loadMenu(r);
                list.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea restaurantelor: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Restaurant restaurant) {
        String sql = "UPDATE restaurants SET name=?, street=?, city=?, postal_code=?, category=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getAddress().getStreet());
            stmt.setString(3, restaurant.getAddress().getCity());
            stmt.setString(4, restaurant.getAddress().getPostalCode());
            stmt.setString(5, restaurant.getCategory());
            stmt.setString(6, restaurant.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea restaurantului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM restaurants WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea restaurantului: " + e.getMessage(), e);
        }
    }

    private void loadMenu(Restaurant restaurant) throws SQLException {
        String sql = "SELECT * FROM products WHERE restaurant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    restaurant.getMenu().addProduct(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("category")
                    ));
                }
            }
        }
    }

    private Restaurant mapRow(ResultSet rs) throws SQLException {
        Address address = new Address(
            rs.getString("street"),
            rs.getString("city"),
            rs.getString("postal_code")
        );
        return new Restaurant(
            rs.getString("id"),
            rs.getString("name"),
            address,
            rs.getString("category")
        );
    }
}
