package com.amdocs.telecom.app;

import com.amdocs.telecom.dao.TicketDAO;
import com.amdocs.telecom.model.TroubleTicket;

public class CreateTicketMain {

    public static void main(String[] args) {
        try {
            TroubleTicket ticket = new TroubleTicket(
                0,
                1,
                1,
                null,
                "Internet connectivity issue",
                "Customer is experiencing slow internet.",
                "INTERNET",
                "HIGH",
                "OPEN",
                null,
                null
            );

            TicketDAO ticketDAO = new TicketDAO();
            int ticketId = ticketDAO.createTicket(ticket);

            System.out.println(
                "Ticket created successfully! Ticket ID: " + ticketId
            );
            //deadline portion
            ticketDAO.setTicketDeadline(ticketId, ticket.getPriority());

        } catch (Exception e) {
            System.err.println("Ticket creation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}