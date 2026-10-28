
package com.amdocs.telecom.app;

import com.amdocs.telecom.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TicketDashboardMain {

    public static void main(String[] args) {

            String sql =
    "SELECT priority, COUNT(*) AS total " +
    "FROM trouble_tickets " +
    "GROUP BY priority " +
    "ORDER BY FIELD(priority, 'CRITICAL', 'HIGH', 'MEDIUM', 'LOW')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            System.out.println("\nTICKET DASHBOARD");
            System.out.println("==============================");

            while (rs.next()) {
                System.out.println(
    rs.getString("priority") + ": " +
    rs.getInt("total")
);
            }

        } catch (Exception e) {
            System.err.println("Dashboard failed: " + e.getMessage());
        }
    }
}