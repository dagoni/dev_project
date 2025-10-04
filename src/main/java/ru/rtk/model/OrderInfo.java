package ru.rtk.model;

public class OrderInfo {
    private int orderId;
    private String orderDate;
    private String customerName;
    private String productDescription;
    private int quantity;
    private double totalPrice;
    private String status;

    public OrderInfo() {
    }

    // геттеры
    public int getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    // сеттеры
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }





    @Override
    public String toString() {
        return "OrderInfo{orderId=" + orderId + ", orderDate='" + orderDate + "', customerName='" + customerName +
                "', productDescription='" + productDescription + "', quantity=" + quantity +
                ", totalPrice=" + totalPrice + ", status='" + status + "'}";
    }
}