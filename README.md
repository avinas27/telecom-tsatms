# Telecom Service Assurance & Trouble Ticket Management System

## Overview

The Telecom Service Assurance & Trouble Ticket Management System is a Java-based application designed to manage telecom service issues through a structured trouble-ticket workflow.

It uses JDBC to interact with a MySQL database and supports ticket creation, engineer assignment, status tracking, ticket history, and SLA monitoring.

## Features

- **Ticket Management:** Create and view telecom trouble tickets.
- **Customer Management:** Store and retrieve customer information.
- **Engineer Assignment:** Assign tickets based on engineer specialization and workload.
- **Status Tracking:** Update ticket statuses and maintain status history.
- **SLA Monitoring:** Calculate SLA deadlines and identify breached or at-risk tickets.
- **Ticket Dashboard:** View ticket counts grouped by priority.
- **SLA Breach Report:** Identify unresolved tickets that have exceeded their deadlines.
- **Ticket History:** Review previous ticket status changes.

## Technologies Used

- Java 8
- MySQL
- JDBC
- Apache Ant
- MySQL Connector/J

## Project Structure

```text
telecom-tsatms/
├── src/
│   └── com/amdocs/telecom/
│       ├── app/
│       ├── dao/
│       ├── main/
│       ├── model/
│       ├── service/
│       └── util/
├── resources/
│   └── db.properties
├── lib/
│   └── mysql-connector-j-8.0.33.jar
├── build.xml
└── README.md
```

## Prerequisites

Before running the application, install:

- JDK 8 or a compatible Java Development Kit
- Apache Ant
- MySQL Server

## Database Configuration

1. Create a MySQL database named `telecom_tsatms`.
2. Create the required tables and configure the database schema.
3. Update `resources/db.properties` with your local MySQL connection details.

Example configuration:

```properties
db.url=jdbc:mysql://127.0.0.1:3306/telecom_tsatms
db.username=YOUR_MYSQL_USERNAME
db.password=YOUR_MYSQL_PASSWORD
```

Ensure the database schema and required test data are available before running the application.

## Build and Run

Open a terminal in the project root directory.

Compile the project:

```bash
ant clean compile
```

Run the application:

```bash
ant run
```

The application displays trouble tickets along with their details and SLA status.

## SLA Statuses

| Status | Description |
|---|---|
| WITHIN_SLA | The ticket is within its configured SLA deadline. |
| AT_RISK | The ticket is approaching its deadline. |
| BREACHED | The ticket has exceeded its deadline. |
| SLA_NOT_CONFIGURED | No SLA deadline is available for the ticket. |

## Project Objective

The project demonstrates object-oriented programming, JDBC-based database integration, SQL operations, modular application design, and basic service assurance workflows relevant to telecom operations.