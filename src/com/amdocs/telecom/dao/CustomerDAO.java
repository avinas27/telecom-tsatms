package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAllCustomers() throws Exception {

        List<Customer> customers = new ArrayList<>();

        String sql =
            "SELECT customer_id, full_name, email, phone "
            + "FROM customers ORDER BY customer_id";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("full_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone")
                );

                customers.add(customer);
            }
        }

        return customers;
    }
}