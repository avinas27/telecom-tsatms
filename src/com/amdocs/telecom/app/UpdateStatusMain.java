package com.amdocs.telecom.app;

import com.amdocs.telecom.dao.TicketDAO;

public class UpdateStatusMain {

    public static void main(String[] args) {
        try {
            TicketDAO ticketDAO = new TicketDAO();

            boolean updated = ticketDAO.updateStatus(
                1,
                "IN_PROGRESS",
                "Engineer started investigating the issue."
            );

            if (updated) {
                System.out.println("Ticket status updated successfully!");
                System.out.println("Status history recorded.");
            } else {
                System.out.println("Ticket not found.");
            }

        } catch (Exception e) {
            System.err.println("Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}