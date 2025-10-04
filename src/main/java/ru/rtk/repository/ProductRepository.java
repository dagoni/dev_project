package ru.rtk.repository;

import ru.rtk.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private Connection connection;
    // TODO: Добавим конструктор
    public ProductRepository(Connection connection) {
        this.connection = connection;
    }
    // TODO: Добавим методы для добавления товара
    public int insertProduct(Product product) throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getDescription());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getCategory());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new SQLException("Не удалось получить ID товара");
            }
        }
    }
    // TODO: Добавим методы для обновления и количества товара
    public void updateProductPrice(int productId, double newPrice) throws SQLException {
        String sql = "UPDATE product SET price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { // TODO: Используем try-with-resources
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
    // TODO: Добавим метод для обновления количества товара
    public void updateProductQuantity(int productId, int newQuantity) throws SQLException {
        String sql = "UPDATE product SET quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
    // TODO: Добавим метод для получения товара по ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategory(rs.getString("category"));
                return product;
            }
            return null;
        }
    }
    // TODO: Добавим метод для получения всех товаров
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategory(rs.getString("category"));
                products.add(product);
            }
        }
        return products;
    }
    // TODO: Добавим метод для получения товаров с низким количеством
    public List<Product> getProductsLowInStock(int threshold) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE quantity < ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategory(rs.getString("category"));
                products.add(product);
            }
        }
        return products;
    }
    // TODO: Добавим метод для обновления цены товаров по категории
    public int updateProductPriceForCategory(String category, double multiplier) throws SQLException {
        String sql = "UPDATE product SET price = price * ? WHERE category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, multiplier);
            stmt.setString(2, category);
            return stmt.executeUpdate();
        }
    }

}