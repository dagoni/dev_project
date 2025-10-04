package ru.rtk.model;

// Заказы
public class Order {
    private int id;
    private int productId;
    private int customerId;
    private String orderDate;
    private int quantity;
    private int statusId;

    public Order() {
    }

    public Order(int id, int productId, int customerId, String orderDate, int quantity, int statusId) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.statusId = statusId;
    }

    // геттеры
    public int getId() {
        return id;
    }
    public int getProductId() {
        return productId;
    }

    public int getCustomerId() {
        return customerId;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getStatusId() {
        return statusId;
    }
    // сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", productId=" + productId + ", customerId=" + customerId +
                ", orderDate='" + orderDate + "', quantity=" + quantity + ", statusId=" + statusId + "}";
    }
}