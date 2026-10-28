package com.amdocs.telecom.app;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.model.Customer;

import java.util.List;

public class CustomerMain {

    public static void main(String[] args) {

        CustomerDAO customerDAO = new CustomerDAO();

        try {

            List<Customer> customers =
                customerDAO.getAllCustomers();

            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }

            System.out.println("Customer Records");
            System.out.println("------------------------------");

            for (Customer customer : customers) {
                System.out.println(customer);
            }

            System.out.println("------------------------------");
            System.out.println(
                "Total customers: " + customers.size()
            );

        } catch (Exception e) {
            System.err.println(
                "Failed to retrieve customers: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}