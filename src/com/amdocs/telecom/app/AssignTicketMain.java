package com.amdocs.telecom.app;

import com.amdocs.telecom.service.TicketService;

public class AssignTicketMain {

    public static void main(String[] args) {
        try {
            TicketService service = new TicketService();

            boolean assigned = service.assignTicket(1);

            if (assigned) {
                System.out.println("Ticket assigned successfully!");
            } else {
                System.out.println(
                    "Ticket not assigned. Check its status, engineer specialization, and workload."
                );
            }
        } catch (Exception e) {
            System.err.println("Assignment failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}