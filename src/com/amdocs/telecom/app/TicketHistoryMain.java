package com.amdocs.telecom.app;

import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class TicketHistoryMain {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Ticket ID: ");
        int ticketId = scanner.nextInt();

        String sql =
            "SELECT old_status, new_status, remarks, changed_at " +
            "FROM ticket_status_history " +
            "WHERE ticket_id = ? " +
            "ORDER BY changed_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, ticketId);

            try (ResultSet rs = statement.executeQuery()) {

                boolean found = false;

                System.out.println("\nTicket Status History");
                System.out.println("------------------------------");

                while (rs.next()) {
                    found = true;

                    System.out.println(
                        "Previous Status: " + rs.getString("old_status")
                    );
                    System.out.println(
                        "New Status: " + rs.getString("new_status")
                    );
                    System.out.println(
                        "Remarks: " + rs.getString("remarks")
                    );
                    System.out.println(
                        "Changed At: " + rs.getTimestamp("changed_at")
                    );
                    System.out.println("------------------------------");
                }

                if (!found) {
                    System.out.println(
                        "No status history found for Ticket ID " + ticketId
                    );
                }
            }

        } catch (Exception e) {
            System.err.println(
                "Failed to retrieve ticket history: " + e.getMessage()
            );
        }

        scanner.close();
    }
}