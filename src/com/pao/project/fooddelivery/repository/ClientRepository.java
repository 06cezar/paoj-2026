package com.pao.project.fooddelivery.repository;

import com.pao.project.fooddelivery.model.Client;
import com.pao.project.fooddelivery.model.DeliveryAddress;
import com.pao.project.fooddelivery.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {
    private static ClientRepository instance;
    private final Connection connection;

    private ClientRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public static synchronized ClientRepository getInstance() {
        if (instance == null) {
            instance = new ClientRepository();
        }
        return instance;
    }

    @Override
    public void save(Client client) {
        String sql = "INSERT INTO clients (id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());
            stmt.executeUpdate();

            for (DeliveryAddress addr : client.getAddresses()) {
                saveAddress(addr, client.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului: " + e.getMessage(), e);
        }
    }

    public void saveAddress(DeliveryAddress addr, String clientId) {
        String sql = "INSERT INTO delivery_addresses (id, client_id, street, city, postal_code, details) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, addr.getId());
            stmt.setString(2, clientId);
            stmt.setString(3, addr.getStreet());
            stmt.setString(4, addr.getCity());
            stmt.setString(5, addr.getPostalCode());
            stmt.setString(6, addr.getDetails());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea adresei: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> findById(String id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = mapRow(rs);
                    loadAddresses(client);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM clients";
        List<Client> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = mapRow(rs);
                loadAddresses(client);
                list.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clientilor: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE clients SET name=?, email=?, phone=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM clients WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului: " + e.getMessage(), e);
        }
    }

    private void loadAddresses(Client client) throws SQLException {
        String sql = "SELECT * FROM delivery_addresses WHERE client_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    client.addAddress(new DeliveryAddress(
                        rs.getString("id"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("postal_code"),
                        rs.getString("details")
                    ));
                }
            }
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}
