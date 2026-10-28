
package com.amdocs.telecom.model;

import java.sql.Timestamp;

public class TroubleTicket {

    private int ticketId;
    private int customerId;
    private int serviceId;
    private Integer assignedEngineerId;
    private String title;
    private String description;
    private String category;
    private String priority;
    private String status;
    private Timestamp createdAt;
    private Timestamp dueAt;

    public TroubleTicket(
            int ticketId,
            int customerId,
            int serviceId,
            Integer assignedEngineerId,
            String title,
            String description,
            String category,
            String priority,
            String status,
            Timestamp createdAt,
            Timestamp dueAt) {

        this.ticketId = ticketId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.assignedEngineerId = assignedEngineerId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public Integer getAssignedEngineerId() {
        return assignedEngineerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getDueAt() {
        return dueAt;
    }
    public void setTicketId(int ticketId) {
    this.ticketId = ticketId;
}
}
