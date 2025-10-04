package ru.rtk.repository;

import ru.rtk.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private Connection connection;
    //TODO: 1. Создадим метод insertCustomer, который принимает объект Customer и сохраняет его в БД
    public CustomerRepository(Connection connection) {
        this.connection = connection;
    }
    //TODO: 2. Создадим метод deleteCustomerByEmail, который принимает email и удаляет клиента из БД
    public int insertCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new SQLException("Не удалось получить ID клиента");
            }
        }
    }
    //TODO: 3. Создадим метод deleteCustomerByEmail, который принимает email и удаляет клиента из БД
    public int deleteCustomerByEmail(String email) throws SQLException {
        String sql = "DELETE FROM customer WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate();
        }
    }
    //TODO: 4. Создадим метод getCustomerById, который принимает id и возвращает клиента из БД
    public Customer getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                return customer;
            }
            return null;
        }
    }
    //TODO: 5. Создадим метод getAllCustomers, который возвращает всех клиентов из БД
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
        }
        return customers;
    }
    //TODO: 6. Создадим метод getCustomersWithoutOrders, который возвращает всех клиентов, у которых нет заказов
    public List<Customer> getCustomersWithoutOrders() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE id NOT IN (SELECT DISTINCT customer_id FROM orders)";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
        }
        return customers;
    }
}