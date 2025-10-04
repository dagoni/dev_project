package ru.rtk.model;

// Клиенты
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public Customer() {
    }

    public Customer(int id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

   // геттеры
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }

    // сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName +
                "', phone='" + phone + "', email='" + email + "'}";
    }
}