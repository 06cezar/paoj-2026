package com.pao.project.fooddelivery.repository;

import com.pao.project.fooddelivery.model.*;
import com.pao.project.fooddelivery.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<Order, String> {
    private static OrderRepository instance;
    private final Connection connection;

    private OrderRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public static synchronized OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    // Salveaza comanda si produsele aferente intr-o singura tranzactie JDBC
    @Override
    public void save(Order order) {
        String sqlOrder = "INSERT INTO orders (id, client_id, restaurant_id, driver_id, " +
                          "delivery_address_id, status, order_date, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlProduct = "INSERT INTO order_products (order_id, product_id) VALUES (?, ?)";

        try {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement stmtOrder = connection.prepareStatement(sqlOrder)) {
                    stmtOrder.setString(1, order.getId());
                    stmtOrder.setString(2, order.getClient().getId());
                    stmtOrder.setString(3, order.getRestaurant().getId());
                    if (order.getDriver() != null) {
                        stmtOrder.setString(4, order.getDriver().getId());
                    } else {
                        stmtOrder.setNull(4, Types.VARCHAR);
                    }
                    stmtOrder.setString(5, order.getDeliveryAddress().getId());
                    stmtOrder.setString(6, order.getStatus().name());
                    stmtOrder.setTimestamp(7, Timestamp.valueOf(order.getOrderDate()));
                    stmtOrder.setDouble(8, order.getTotalPrice());
                    stmtOrder.executeUpdate();
                }

                try (PreparedStatement stmtProduct = connection.prepareStatement(sqlProduct)) {
                    for (Product p : order.getProducts()) {
                        stmtProduct.setString(1, order.getId());
                        stmtProduct.setString(2, p.getId());
                        stmtProduct.addBatch();
                    }
                    stmtProduct.executeBatch();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Eroare la salvarea comenzii, rollback efectuat: " + e.getMessage(), e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gestionarea tranzactiei: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findById(String id) {
        // JOIN: orders + clients + restaurants + delivery_addresses + drivers (LEFT)
        String sql = "SELECT o.id, o.status, o.order_date, o.total_price, " +
                     "c.id AS c_id, c.name AS c_name, c.email AS c_email, c.phone AS c_phone, " +
                     "r.id AS r_id, r.name AS r_name, r.street AS r_street, r.city AS r_city, " +
                     "r.postal_code AS r_zip, r.category AS r_cat, " +
                     "da.id AS da_id, da.street AS da_street, da.city AS da_city, " +
                     "da.postal_code AS da_zip, da.details AS da_details, " +
                     "d.id AS d_id, d.name AS d_name, d.email AS d_email, " +
                     "d.phone AS d_phone, d.rating AS d_rating, d.available AS d_available " +
                     "FROM orders o " +
                     "JOIN clients c ON o.client_id = c.id " +
                     "JOIN restaurants r ON o.restaurant_id = r.id " +
                     "JOIN delivery_addresses da ON o.delivery_address_id = da.id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.id " +
                     "WHERE o.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    loadProducts(order);
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea comenzii: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT o.id, o.status, o.order_date, o.total_price, " +
                     "c.id AS c_id, c.name AS c_name, c.email AS c_email, c.phone AS c_phone, " +
                     "r.id AS r_id, r.name AS r_name, r.street AS r_street, r.city AS r_city, " +
                     "r.postal_code AS r_zip, r.category AS r_cat, " +
                     "da.id AS da_id, da.street AS da_street, da.city AS da_city, " +
                     "da.postal_code AS da_zip, da.details AS da_details, " +
                     "d.id AS d_id, d.name AS d_name, d.email AS d_email, " +
                     "d.phone AS d_phone, d.rating AS d_rating, d.available AS d_available " +
                     "FROM orders o " +
                     "JOIN clients c ON o.client_id = c.id " +
                     "JOIN restaurants r ON o.restaurant_id = r.id " +
                     "JOIN delivery_addresses da ON o.delivery_address_id = da.id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.id";

        List<Order> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Order order = mapRowToOrder(rs);
                loadProducts(order);
                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea comenzilor: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET status=?, driver_id=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getStatus().name());
            if (order.getDriver() != null) {
                stmt.setString(2, order.getDriver().getId());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.setString(3, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea comenzii: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea comenzii: " + e.getMessage(), e);
        }
    }

    // JOIN 1: Toate comenzile cu informatii despre client si restaurant
    public List<String> findOrdersWithClientAndRestaurant() {
        String sql = "SELECT o.id, c.name AS client_name, r.name AS restaurant_name, " +
                     "o.status, o.total_price " +
                     "FROM orders o " +
                     "JOIN clients c ON o.client_id = c.id " +
                     "JOIN restaurants r ON o.restaurant_id = r.id";

        List<String> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(String.format("Comanda %-8s | Client: %-20s | Restaurant: %-20s | Status: %-15s | Total: %.2f RON",
                    rs.getString("id"),
                    rs.getString("client_name"),
                    rs.getString("restaurant_name"),
                    rs.getString("status"),
                    rs.getDouble("total_price")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare JOIN 1: " + e.getMessage(), e);
        }
        return results;
    }

    // JOIN 2: Istoricul comenzilor unui client cu numele restaurantului
    public List<String> findOrdersByClientId(String clientId) {
        String sql = "SELECT o.id, r.name AS restaurant_name, o.status, o.total_price, o.order_date " +
                     "FROM orders o " +
                     "JOIN restaurants r ON o.restaurant_id = r.id " +
                     "WHERE o.client_id = ? " +
                     "ORDER BY o.order_date DESC";

        List<String> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(String.format("Comanda %-8s | Restaurant: %-20s | Status: %-15s | Total: %.2f RON | Data: %s",
                        rs.getString("id"),
                        rs.getString("restaurant_name"),
                        rs.getString("status"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("order_date")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare JOIN 2: " + e.getMessage(), e);
        }
        return results;
    }

    // JOIN 3: Comenzile active cu informatii despre sofer (LEFT JOIN)
    public List<String> findActiveOrdersWithDriver() {
        String sql = "SELECT o.id, c.name AS client_name, r.name AS restaurant_name, " +
                     "d.name AS driver_name, o.status " +
                     "FROM orders o " +
                     "JOIN clients c ON o.client_id = c.id " +
                     "JOIN restaurants r ON o.restaurant_id = r.id " +
                     "LEFT JOIN drivers d ON o.driver_id = d.id " +
                     "WHERE o.status NOT IN ('DELIVERED', 'CANCELLED')";

        List<String> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String driverName = rs.getString("driver_name");
                results.add(String.format("Comanda %-8s | Client: %-20s | Restaurant: %-20s | Sofer: %-20s | Status: %s",
                    rs.getString("id"),
                    rs.getString("client_name"),
                    rs.getString("restaurant_name"),
                    driverName != null ? driverName : "neatribuit",
                    rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare JOIN 3: " + e.getMessage(), e);
        }
        return results;
    }

    // Metode private ajutatoare
    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Client client = new Client(
            rs.getString("c_id"),
            rs.getString("c_name"),
            rs.getString("c_email"),
            rs.getString("c_phone")
        );

        Address rAddr = new Address(rs.getString("r_street"), rs.getString("r_city"), rs.getString("r_zip"));
        Restaurant restaurant = new Restaurant(
            rs.getString("r_id"), rs.getString("r_name"), rAddr, rs.getString("r_cat")
        );

        DeliveryAddress deliveryAddress = new DeliveryAddress(
            rs.getString("da_id"),
            rs.getString("da_street"),
            rs.getString("da_city"),
            rs.getString("da_zip"),
            rs.getString("da_details")
        );

        Order order = new Order(
            rs.getString("id"),
            client,
            restaurant,
            deliveryAddress,
            rs.getTimestamp("order_date").toLocalDateTime()
        );
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));

        String dId = rs.getString("d_id");
        if (dId != null) {
            Driver driver = new Driver(dId, rs.getString("d_name"), rs.getString("d_email"), rs.getString("d_phone"));
            driver.setRating(rs.getDouble("d_rating"));
            driver.setAvailable(rs.getBoolean("d_available"));
            order.setDriver(driver);
        }

        return order;
    }

    private void loadProducts(Order order) throws SQLException {
        String sql = "SELECT p.id, p.name, p.price, p.description, p.category " +
                     "FROM products p " +
                     "JOIN order_products op ON p.id = op.product_id " +
                     "WHERE op.order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    order.addProduct(new Product(
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
}
