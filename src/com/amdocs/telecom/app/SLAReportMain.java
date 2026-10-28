package com.amdocs.telecom.app;

import com.amdocs.telecom.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SLAReportMain {

    public static void main(String[] args) {

        String sql =
            "SELECT ticket_id, title, priority, status, due_at " +
            "FROM trouble_tickets " +
            "WHERE due_at < CURRENT_TIMESTAMP " +
            "AND status NOT IN ('RESOLVED', 'CLOSED', 'CANCELLED') " +
            "ORDER BY due_at ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\nSLA Breach Report");
            System.out.println("==============================");

            while (rs.next()) {
                found = true;

                System.out.println("Ticket ID: " + rs.getInt("ticket_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Priority: " + rs.getString("priority"));
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Deadline: " + rs.getTimestamp("due_at"));
                System.out.println("------------------------------");
            }

            if (!found) {
                System.out.println("No SLA-breached tickets found.");
            }

        } catch (Exception e) {
            System.err.println("Report failed: " + e.getMessage());
        }
    }
}