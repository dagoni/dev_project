package ru.rtk.repository;

import ru.rtk.model.Order;
import ru.rtk.model.OrderInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private Connection connection;
    // TODO: Добавим конструктор для инициализации соединения с БД
    public OrderRepository(Connection connection) {
        this.connection = connection;
    }
    // TODO: Добавить метод для получения информации о заказе по id
    public void insertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (product_id, customer_id, order_date, quantity, status_id) VALUES (?, ?, CURRENT_DATE, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getProductId());
            stmt.setInt(2, order.getCustomerId());
            stmt.setInt(3, order.getQuantity());
            stmt.setInt(4, order.getStatusId());
            stmt.executeUpdate();
        }
    }
    // TODO: Добавим метод для удаления заказов по email клиента
    public int deleteOrdersByCustomerEmail(String email) throws SQLException {
        String sql = "DELETE FROM orders WHERE customer_id IN (SELECT id FROM customer WHERE email = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate();
        }
    }
    // TODO: Добавим метод для получения последних 5 заказов
    public List<OrderInfo> getLastFiveOrders() throws SQLException {
        List<OrderInfo> orders = new ArrayList<>();
        String sql = """
                SELECT o.id, o.order_date, c.first_name, c.last_name, p.description, 
                       o.quantity, p.price, os.status_name
                FROM orders o
                JOIN customer c ON o.customer_id = c.id
                JOIN product p ON o.product_id = p.id
                JOIN order_status os ON o.status_id = os.id
                ORDER BY o.order_date DESC, o.id DESC
                LIMIT 5
                """;
        // TODO: Добавим логику для получения последних 5 заказов
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(rs.getInt("id"));
                orderInfo.setOrderDate(rs.getDate("order_date").toString());
                orderInfo.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
                orderInfo.setProductDescription(rs.getString("description"));
                orderInfo.setQuantity(rs.getInt("quantity"));
                orderInfo.setTotalPrice(rs.getDouble("price") * rs.getInt("quantity"));
                orderInfo.setStatus(rs.getString("status_name"));
                orders.add(orderInfo);
            }
        }
        return orders;
    }
    // TODO: Добавим метод для получения заказа по id
    public Order getOrderById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setProductId(rs.getInt("product_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getDate("order_date").toString());
                order.setQuantity(rs.getInt("quantity"));
                order.setStatusId(rs.getInt("status_id"));
                return order;
            }
            return null;
        }
    }
    // TODO: Добавим метод для получения всех заказов
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setProductId(rs.getInt("product_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getDate("order_date").toString());
                order.setQuantity(rs.getInt("quantity"));
                order.setStatusId(rs.getInt("status_id"));
                orders.add(order);
            }
        }
        return orders;
    }
    // TODO: Добавим метод для получения заказов за последние N дней
    public List<OrderInfo> getOrdersLastNDays(int days) throws SQLException {
        List<OrderInfo> orders = new ArrayList<>();
        String sql = """
                SELECT o.id, o.order_date, c.first_name, c.last_name, p.description, 
                       o.quantity, p.price, os.status_name
                FROM orders o
                JOIN customer c ON o.customer_id = c.id
                JOIN product p ON o.product_id = p.id
                JOIN order_status os ON o.status_id = os.id
                WHERE o.order_date >= CURRENT_DATE - INTERVAL '? days'
                ORDER BY o.order_date DESC
                """;
        // TODO: Добавим логику для получения заказов за последние N дней
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(rs.getInt("id"));
                orderInfo.setOrderDate(rs.getDate("order_date").toString());
                orderInfo.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
                orderInfo.setProductDescription(rs.getString("description"));
                orderInfo.setQuantity(rs.getInt("quantity"));
                orderInfo.setTotalPrice(rs.getDouble("price") * rs.getInt("quantity"));
                orderInfo.setStatus(rs.getString("status_name"));
                orders.add(orderInfo);
            }
        }
        return orders;
    }
    // TODO: Добавим метод для обновления статуса заказа
    public int updateOrderStatus(int orderId, int statusId) throws SQLException {
        String sql = "UPDATE orders SET status_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate();
        }
    }
    // TODO: Добавим метод для получения количества отмененных заказов
    public int countCancelledOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE status_id = (SELECT id FROM order_status WHERE status_name = 'Отменен')";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

}