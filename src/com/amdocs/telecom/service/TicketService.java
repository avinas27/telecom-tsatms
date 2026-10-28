package com.amdocs.telecom.service;

import com.amdocs.telecom.dao.TicketDAO;
import com.amdocs.telecom.model.TroubleTicket;

import java.util.List;
import java.util.Arrays;

public class TicketService {

    private final TicketDAO ticketDAO = new TicketDAO();

    // Retrieve all tickets
    public List<TroubleTicket> getAllTickets()
            throws Exception {

        return ticketDAO.findAll();
    }
    //test runner
    public boolean assignTicket(int ticketId) throws Exception {
    if (ticketId <= 0) {
        throw new IllegalArgumentException("Ticket ID must be positive.");
    }

    return ticketDAO.assignTicket(ticketId);
}
public TroubleTicket getTicketById(int ticketId) throws Exception {

    if (ticketId <= 0) {
        throw new IllegalArgumentException("Invalid ticket ID.");
    }

    return ticketDAO.findById(ticketId);
}
public boolean updateTicketStatus(int ticketId, String status)
        throws Exception {

    if (ticketId <= 0) {
        throw new IllegalArgumentException("Invalid ticket ID.");
    }

    if (status == null ||
        !java.util.Arrays.asList(
            "OPEN", "ASSIGNED", "IN_PROGRESS",
            "PENDING", "ESCALATED", "RESOLVED",
            "CLOSED", "CANCELLED"
        ).contains(status)) {

        throw new IllegalArgumentException("Invalid ticket status.");
    }

    return ticketDAO.updateStatus(ticketId, status);
}
    // Create a ticket after validating its details
    public int createTicket(TroubleTicket ticket)
            throws Exception {

        if (ticket == null) {
            throw new IllegalArgumentException(
                "Ticket cannot be null."
            );
        }

        if (ticket.getCustomerId() <= 0) {
            throw new IllegalArgumentException(
                "Customer ID must be positive."
            );
        }

        if (ticket.getServiceId() <= 0) {
            throw new IllegalArgumentException(
                "Service ID must be positive."
            );
        }

        if (ticket.getTitle() == null ||
            ticket.getTitle().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "Ticket title cannot be empty."
            );
        }

        if (ticket.getDescription() == null ||
            ticket.getDescription().trim().isEmpty()) {

            throw new IllegalArgumentException(
                "Ticket description cannot be empty."
            );
        }

        if (ticket.getCategory() == null ||
            !Arrays.asList(
                "NETWORK",
                "BILLING",
                "VOICE",
                "INTERNET",
                "HARDWARE",
                "OTHER"
            ).contains(ticket.getCategory())) {

            throw new IllegalArgumentException(
                "Invalid ticket category."
            );
        }

        if (ticket.getPriority() == null ||
            !Arrays.asList(
                "LOW",
                "MEDIUM",
                "HIGH",
                "CRITICAL"
            ).contains(ticket.getPriority())) {

            throw new IllegalArgumentException(
                "Invalid ticket priority."
            );
        }

        return ticketDAO.createTicket(ticket);
    }
}