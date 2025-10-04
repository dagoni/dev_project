package ru.rtk.model;

// Товары
public class Product {
    private int id;
    private String description;
    private double price;
    private int quantity;
    private String category;

    public Product() {
    }

    public Product(int id, String description, double price, int quantity, String category) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", description='" + description + "', price=" + price +
                ", quantity=" + quantity + ", category='" + category + "'}";
    }
}