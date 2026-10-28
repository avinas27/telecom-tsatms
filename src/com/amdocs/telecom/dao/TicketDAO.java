package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.TroubleTicket;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    // Fetch all tickets from the database
    public List<TroubleTicket> findAll() throws Exception {

        List<TroubleTicket> tickets = new ArrayList<>();

        String sql =
            "SELECT ticket_id, customer_id, service_id, " +
            "assigned_engineer_id, title, description, category, " +
            "priority, status, created_at, due_at " +
            "FROM trouble_tickets ORDER BY created_at DESC";

        try (Connection connection =
                 DBConnection.getConnection();

             PreparedStatement statement =
                 connection.prepareStatement(sql);

             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int engineerValue =
                    resultSet.getInt("assigned_engineer_id");

                Integer engineerId =
                    resultSet.wasNull() ? null : engineerValue;

                tickets.add(new TroubleTicket(
                    resultSet.getInt("ticket_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getInt("service_id"),
                    engineerId,
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getString("priority"),
                    resultSet.getString("status"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("due_at")
                ));
            }
        }

        return tickets;
    }
//ticket status updates
  public boolean updateStatus(int ticketId, String status)
        throws Exception {

    String sql =
        "UPDATE trouble_tickets SET status = ?, " +
        "updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, status);
        statement.setInt(2, ticketId);

        return statement.executeUpdate() == 1;
    }
}
    // Create a new trouble ticket
    public int createTicket(TroubleTicket ticket)
            throws Exception {

        String sql =
    "INSERT INTO trouble_tickets " +
    "(customer_id, service_id, title, description, " +
    "category, priority, status, due_at) " +
    "VALUES (?, ?, ?, ?, ?, ?, ?, " +
    "(SELECT DATE_ADD(NOW(), INTERVAL resolution_minutes MINUTE) " +
    " FROM sla_configurations WHERE priority = ?))";

        try (Connection connection =
                 DBConnection.getConnection();

             PreparedStatement statement =
                 connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, ticket.getCustomerId());
            statement.setInt(2, ticket.getServiceId());
            statement.setString(3, ticket.getTitle());
            statement.setString(4, ticket.getDescription());
            statement.setString(5, ticket.getCategory());
            statement.setString(6, ticket.getPriority());
            statement.setString(7, ticket.getStatus());
            statement.setString(8, ticket.getPriority());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted != 1) {
                throw new Exception(
                    "Ticket creation failed."
                );
            }

            try (ResultSet generatedKeys =
                     statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    int ticketId =
                        generatedKeys.getInt(1);

                    ticket.setTicketId(ticketId);

                    return ticketId;
                }

                throw new Exception(
                    "Ticket was inserted, but its ID " +
                    "could not be retrieved."
                );
            }
        }
    }
    public TroubleTicket findById(int ticketId) throws Exception {

    String sql =
        "SELECT ticket_id, customer_id, service_id, " +
        "assigned_engineer_id, title, description, category, " +
        "priority, status, created_at, due_at " +
        "FROM trouble_tickets WHERE ticket_id = ?";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, ticketId);

        try (ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                int engineerValue = rs.getInt("assigned_engineer_id");
                Integer engineerId = rs.wasNull() ? null : engineerValue;

                return new TroubleTicket(
                    rs.getInt("ticket_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("service_id"),
                    engineerId,
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("category"),
                    rs.getString("priority"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("due_at")
                );
            }
        }
    }

    return null;
}
//sla deadline calculation
public boolean setTicketDeadline(int ticketId, String priority)
        throws Exception {

    String sql =
        "UPDATE trouble_tickets " +
        "SET due_at = DATE_ADD(created_at, INTERVAL ? MINUTE) " +
        "WHERE ticket_id = ?";

    int resolutionMinutes;

    switch (priority.toUpperCase()) {
        case "CRITICAL":
            resolutionMinutes = 120;
            break;
        case "HIGH":
            resolutionMinutes = 240;
            break;
        case "MEDIUM":
            resolutionMinutes = 720;
            break;
        case "LOW":
            resolutionMinutes = 2880;
            break;
        default:
            throw new IllegalArgumentException("Invalid priority");
    }

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, resolutionMinutes);
        statement.setInt(2, ticketId);

        return statement.executeUpdate() == 1;
    }
}
public boolean assignTicket(int ticketId) throws Exception {

    String selectSql =
        "SELECT ne.engineer_id " +
        "FROM network_engineers ne " +
        "JOIN trouble_tickets tt ON tt.category = ne.specialization " +
        "WHERE tt.ticket_id = ? " +
        "AND tt.status = 'OPEN' " +
        "AND (SELECT COUNT(*) FROM trouble_tickets t " +
        "     WHERE t.assigned_engineer_id = ne.engineer_id " +
        "     AND t.status IN ('ASSIGNED', 'IN_PROGRESS', 'ESCALATED')) " +
        "< ne.max_open_tickets " +
        "ORDER BY ne.engineer_id LIMIT 1";

    String updateSql =
        "UPDATE trouble_tickets " +
        "SET assigned_engineer_id = ?, status = 'ASSIGNED' " +
        "WHERE ticket_id = ? AND status = 'OPEN'";

    try (Connection connection = DBConnection.getConnection()) {

        int engineerId;

        try (PreparedStatement selectStatement =
                 connection.prepareStatement(selectSql)) {

            selectStatement.setInt(1, ticketId);

            try (ResultSet rs = selectStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
                engineerId = rs.getInt("engineer_id");
            }
        }

        try (PreparedStatement updateStatement =
                 connection.prepareStatement(updateSql)) {

            updateStatement.setInt(1, engineerId);
            updateStatement.setInt(2, ticketId);

            return updateStatement.executeUpdate() == 1;
        }
    }
}
//status history
public boolean updateStatus(int ticketId, String newStatus, String remarks)
        throws Exception {

    String selectSql =
        "SELECT status FROM trouble_tickets WHERE ticket_id = ?";

    String updateSql =
        "UPDATE trouble_tickets SET status = ?, " +
        "updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";

    String historySql =
        "INSERT INTO ticket_status_history " +
        "(ticket_id, old_status, new_status, remarks) " +
        "VALUES (?, ?, ?, ?)";

    try (Connection connection = DBConnection.getConnection()) {
        connection.setAutoCommit(false);

        try {
            String oldStatus;

            try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
                ps.setInt(1, ticketId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        connection.rollback();
                        return false;
                    }
                    oldStatus = rs.getString("status");
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
                ps.setString(1, newStatus);
                ps.setInt(2, ticketId);

                if (ps.executeUpdate() != 1) {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(historySql)) {
                ps.setInt(1, ticketId);
                ps.setString(2, oldStatus);
                ps.setString(3, newStatus);
                ps.setString(4, remarks);
                ps.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
//Fix existing tickets and implement SLA status
public String getSLAStatus(TroubleTicket ticket) {
    if (ticket.getDueAt() == null) {
        return "SLA_NOT_CONFIGURED";
    }

    long remainingMillis =
        ticket.getDueAt().getTime() - System.currentTimeMillis();

    if (remainingMillis <= 0) {
        return "BREACHED";
    }

    // At risk when less than 25% of the SLA time remains.
    long totalSlaMillis = 0;

    try {
        String sql =
            "SELECT resolution_minutes FROM sla_configurations " +
            "WHERE priority = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, ticket.getPriority());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    totalSlaMillis =
                        rs.getLong("resolution_minutes") * 60_000L;
                }
            }
        }
    } catch (Exception e) {
        return "SLA_CHECK_FAILED";
    }

    if (totalSlaMillis > 0 &&
        remainingMillis <= totalSlaMillis * 0.25) {
        return "AT_RISK";
    }

    return "WITHIN_SLA";
}
}