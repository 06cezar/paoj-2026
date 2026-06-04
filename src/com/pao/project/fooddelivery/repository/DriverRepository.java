package com.pao.project.fooddelivery.repository;

import com.pao.project.fooddelivery.model.Driver;
import com.pao.project.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DriverRepository implements Repository<Driver, String> {
    private static DriverRepository instance;
    private final Connection connection;

    private DriverRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public static synchronized DriverRepository getInstance() {
        if (instance == null) {
            instance = new DriverRepository();
        }
        return instance;
    }

    @Override
    public void save(Driver driver) {
        String sql = "INSERT INTO drivers (id, name, email, phone, rating, available) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, driver.getId());
            stmt.setString(2, driver.getName());
            stmt.setString(3, driver.getEmail());
            stmt.setString(4, driver.getPhone());
            stmt.setDouble(5, driver.getRating());
            stmt.setBoolean(6, driver.isAvailable());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea soferului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Driver> findById(String id) {
        String sql = "SELECT * FROM drivers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea soferului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Driver> findAll() {
        String sql = "SELECT * FROM drivers";
        List<Driver> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea soferilor: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Driver driver) {
        String sql = "UPDATE drivers SET name=?, email=?, phone=?, rating=?, available=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getEmail());
            stmt.setString(3, driver.getPhone());
            stmt.setDouble(4, driver.getRating());
            stmt.setBoolean(5, driver.isAvailable());
            stmt.setString(6, driver.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea soferului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM drivers WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea soferului: " + e.getMessage(), e);
        }
    }

    private Driver mapRow(ResultSet rs) throws SQLException {
        Driver driver = new Driver(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone")
        );
        driver.setRating(rs.getDouble("rating"));
        driver.setAvailable(rs.getBoolean("available"));
        return driver;
    }
}
