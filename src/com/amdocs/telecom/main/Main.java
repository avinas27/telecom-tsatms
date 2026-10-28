
package com.amdocs.telecom.main;

import com.amdocs.telecom.model.TroubleTicket;
import com.amdocs.telecom.service.TicketService;
import com.amdocs.telecom.dao.TicketDAO;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        TicketService service = new TicketService();
        TicketDAO ticketDAO = new TicketDAO();

        try {
            List<TroubleTicket> tickets =
                service.getAllTickets();

            if (tickets.isEmpty()) {
                System.out.println("No tickets found.");
                return;
            }

            for (TroubleTicket ticket : tickets) {

                System.out.println("\n--------------------------");
                System.out.println(
                    "Ticket ID: " + ticket.getTicketId());
                System.out.println(
                    "Title: " + ticket.getTitle());
                System.out.println(
                    "Category: " + ticket.getCategory());
                System.out.println(
                    "Priority: " + ticket.getPriority());
                System.out.println(
                    "Status: " + ticket.getStatus());
                System.out.println(
                    "Customer ID: " + ticket.getCustomerId());
                System.out.println(
                    "Service ID: " + ticket.getServiceId());

                System.out.println(
                    "Engineer ID: " +
                    (ticket.getAssignedEngineerId() == null
                        ? "Not assigned"
                        : ticket.getAssignedEngineerId()));

                System.out.println(
                    "Created: " + ticket.getCreatedAt());
                System.out.println(
                    "Deadline: " + ticket.getDueAt());
                    System.out.println("SLA Status: " + ticketDAO.getSLAStatus(ticket));
            }

        } catch (Exception e) {
            System.out.println("Unable to retrieve tickets.");
            e.printStackTrace();
        }
    }
}
