package com.amdocs.telecom.model;

public class Customer {

    private int customerId;
    private String fullName;
    private String email;
    private String phone;

    public Customer(int customerId, String fullName,
                    String email, String phone) {

        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId
                + ", Name: " + fullName
                + ", Email: " + email
                + ", Phone: " + phone;
    }
}