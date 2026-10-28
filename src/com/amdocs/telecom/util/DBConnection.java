
package com.amdocs.telecom.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {

    private static final String CONFIG_FILE = "/db.properties";

    public static Connection getConnection() throws Exception {

        Properties properties = new Properties();

        try (InputStream input =
                 DBConnection.class.getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                    "db.properties not found on the classpath."
                );
            }

            properties.load(input);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Database connected successfully!");
            System.out.println(
                "Connection valid: " + connection.isValid(2)
            );
        } catch (Exception e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}
