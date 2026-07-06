# Appliance Store Platform

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Microservices-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Config%20%7C%20Gateway%20%7C%20Eureka-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Kafka](https://img.shields.io/badge/Kafka-Event%20Driven-black)
![Debezium](https://img.shields.io/badge/Debezium-CDC-purple)
![MySQL](https://img.shields.io/badge/MySQL-8.4-lightblue)
![License](https://img.shields.io/badge/License-MIT-green)

Microservices-based backend platform for an online appliance store built with **Java 21, Spring Boot, Spring Cloud, Docker, Kafka and Debezium**.

This project simulates a real-world backend architecture where products can be managed, shopping carts can be created, sales can be completed, and sale notifications are processed asynchronously through Kafka using the **Transactional Outbox pattern** and **Change Data Capture with Debezium**.

The platform demonstrates backend concepts commonly used in distributed systems:

- Service Discovery with Eureka
- API Gateway with Spring Cloud Gateway
- Centralized Configuration with Spring Cloud Config
- Synchronous communication with OpenFeign
- Resilience patterns with Resilience4J
- Event-driven communication with Apache Kafka
- Transactional Outbox pattern
- Change Data Capture with Debezium
- Idempotent Kafka consumer
- Retry and Dead Letter Topic
- Docker Compose infrastructure
- Postman collection for complete flow testing

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Repositories](#repositories)
- [Microservices](#microservices)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Running With Docker Compose](#running-with-docker-compose)
- [Postman Testing](#postman-testing)
- [Event-Driven Sale Flow](#event-driven-sale-flow)
- [Transactional Outbox](#transactional-outbox)
- [Kafka and Debezium](#kafka-and-debezium)
- [Notification Flow](#notification-flow)
- [Environment Profiles](#environment-profiles)
- [Useful URLs](#useful-urls)
- [Useful Docker Commands](#useful-docker-commands)
- [What This Project Demonstrates](#what-this-project-demonstrates)
- [Author](#author)

---

## Architecture Overview

```text
Client / Postman
      |
      v
API Gateway
      |
      +-----------------------------+
      |                             |
      v                             v
Product Service               Cart Service
                                    |
                                    v
                               Sales Service
                                    |
                                    v
                           MySQL Outbox Table
                                    |
                                    v
                                Debezium
                                    |
                                    v
                           Kafka: sale-created
                                    |
                                    v
                         Notification Service
                                    |
                                    v
                            Mailpit / SMTP
```

---

## Repositories

This platform uses two repositories:

| Repository | Purpose |
|---|---|
| `appliance-store-platform` | Source code, microservices, Docker infrastructure, documentation and Postman collection |
| `appliance-store-config` | Centralized configuration files consumed by Spring Cloud Config Server |

Configuration repository:

```text
https://github.com/JLeonelMartins/appliance-store-config
```

The Config Server reads externalized configuration from the configuration repository and provides it to the microservices according to the active profile.

---

## Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Config Server | 8888 | Centralized configuration using Spring Cloud Config |
| Eureka Server | 8761 | Service discovery |
| API Gateway | 8088 | Single entry point for external clients |
| Product Service | 8081 | Product CRUD and product information |
| Cart Service | 8082 | Shopping cart management |
| Sales Service | 8083 | Sale creation and outbox event generation |
| Notification Service | 8084 | Kafka consumer and email notification delivery |

---

## Main Features

- Microservices architecture
- Spring Cloud Config Server
- Eureka Service Discovery
- Spring Cloud Gateway
- Database per service
- OpenFeign communication between services
- Resilience4J Retry and Circuit Breaker
- Apache Kafka
- Kafka Connect
- Debezium CDC
- Transactional Outbox pattern
- Event-driven communication
- Idempotent Kafka consumer
- Email notification service
- Mailpit for local email testing
- SMTP support for production profile
- Kafka retries
- Dead Letter Topic
- Environment-based configuration with Spring Profiles
- Docker Compose full platform setup
- Postman collection for complete business flow testing

---

## Technologies

- Java 21
- Spring Boot
- Spring Cloud
- Spring Cloud Config
- Spring Cloud Gateway
- Netflix Eureka
- Spring Data JPA
- Hibernate
- OpenFeign
- Resilience4J
- MySQL 8.4
- Apache Kafka
- Kafka Connect
- Debezium
- Docker Compose
- Mailpit
- Maven
- Postman

---

## Project Structure

```text
appliance-store-platform
├── api-gateway
├── cart-service
├── config-server
├── eureka-server
├── infrastructure
│   ├── compose.yaml
│   ├── connectors
│   │   └── sales-outbox-connector.json
│   └── mysql
│       └── init
│           └── 01-init.sh
├── notification-service
├── product-service
├── sales-service
├── postman
│   ├── appliance-store-microservices.postman_collection.json
│   └── appliance-store-local.postman_environment.json
├── LICENSE
└── README.md
```

---

## Quick Start

### 1. Clone the platform repository

```cmd
git clone https://github.com/JLeonelMartins/appliance-store-platform.git
cd appliance-store-platform\infrastructure
```

### 2. Create the `.env` file

Inside the `infrastructure` folder, create a file named:

```text
.env
```

Use this example:

```env
MYSQL_ROOT_PASSWORD=your-root-password

APPLIANCE_DB_USERNAME=appliance_app
APPLIANCE_DB_PASSWORD=your-app-password

DEBEZIUM_DB_USER=debezium
DEBEZIUM_DB_PASSWORD=your-debezium-password

CONFIG_REPO_URI=https://github.com/JLeonelMartins/appliance-store-config
CONFIG_REPO_BRANCH=main
```

The `.env` file must not be committed to the repository.

### 3. Start the platform

```cmd
docker compose up -d --build
```

### 4. Check running containers

```cmd
docker compose ps
```

### 5. Open useful URLs

```text
API Gateway:     http://localhost:8088
Eureka:          http://localhost:8761
Config Server:   http://localhost:8888
Mailpit:         http://localhost:8025
phpMyAdmin:      http://localhost:8085
Kafka Connect:   http://localhost:8086
```

---

## Running With Docker Compose

The complete platform can be started using Docker Compose.

In this mode, all services run inside Docker:

```text
Config Server
Eureka Server
API Gateway
Product Service
Cart Service
Sales Service
Notification Service
MySQL
phpMyAdmin
Kafka
Kafka Connect
Mailpit
```

### 1. Go to the infrastructure folder

```cmd
cd appliance-store-platform\infrastructure
```

### 2. Start the complete platform

```cmd
docker compose up -d --build
```

### 3. Check the containers

```cmd
docker compose ps
```

Expected result:

```text
appliance-config-server          Up
appliance-eureka-server          Up
appliance-api-gateway            Up
appliance-product-service        Up
appliance-cart-service           Up
appliance-sales-service          Up
appliance-notification-service   Up
appliance-mysql                  Up healthy
appliance-kafka                  Up healthy
appliance-kafka-connect          Up
appliance-mailpit                Up healthy
appliance-phpmyadmin             Up
```

---

## Register Debezium Connector

The Debezium connector file is located at:

```text
infrastructure/connectors/sales-outbox-connector.json
```

From CMD, inside the `infrastructure` folder, run:

```cmd
powershell -Command "Invoke-RestMethod -Method Post -Uri 'http://localhost:8086/connectors' -ContentType 'application/json' -InFile 'connectors\sales-outbox-connector.json'"
```

If the connector already exists, it is not a critical error.

To check the connector list:

```cmd
powershell -Command "Invoke-RestMethod -Uri 'http://localhost:8086/connectors'"
```

To check the connector status:

```cmd
powershell -Command "Invoke-RestMethod -Uri 'http://localhost:8086/connectors/sales-outbox-connector/status'"
```

Expected state:

```text
connector.state = RUNNING
tasks.state     = RUNNING
```

---

## Check Eureka

Open:

```text
http://localhost:8761
```

Expected registered services:

```text
API-GATEWAY
PRODUCT-SERVICE
CART-SERVICE
SALES-SERVICE
NOTIFICATION-SERVICE
```

---

## Postman Testing

This project includes a Postman collection and environment to test the complete microservices flow through the API Gateway.

Location:

```text
postman/appliance-store-microservices.postman_collection.json
postman/appliance-store-local.postman_environment.json
```

### Importing the Collection

1. Open Postman
2. Import `appliance-store-microservices.postman_collection.json`
3. Import `appliance-store-local.postman_environment.json`
4. Select the environment: `Appliance Store - Local`
5. Run the folder: `04 - Complete Flow`

### Complete Flow

The folder `04 - Complete Flow` runs the complete purchase flow:

```text
1. Create Product
2. Create Cart
3. Add Item to Cart
4. Create Sale
5. Check Sale
```

Expected result:

```text
Create Product  -> 201
Create Cart     -> 201
Add Item        -> 200
Create Sale     -> 201
Check Sale      -> 200
```

### Complete Flow Endpoints

```text
POST {{gatewayUrl}}/product/api/product
POST {{gatewayUrl}}/cart/api/cart
POST {{gatewayUrl}}/cart/api/cart/{{cartId}}/items
POST {{gatewayUrl}}/sales/api/sales
GET  {{gatewayUrl}}/sales/api/sales/{{saleId}}
```

### Postman Environment Variables

```text
gatewayUrl       -> http://localhost:8088
productUrl       -> http://localhost:8081
cartUrl          -> http://localhost:8082
salesUrl         -> http://localhost:8083
productId        -> generated during the complete flow
cartId           -> generated during the complete flow
cartItemId       -> generated during the complete flow
saleId           -> generated during the complete flow
customerEmail    -> customer@example.com
```

The ID variables can be empty before running the collection. They are automatically populated by Postman test scripts during the complete flow.

---

## Validate Database Flow

Open phpMyAdmin:

```text
http://localhost:8085
```

After running the Postman complete flow, the following tables can be checked:

```text
appliance_product_db.products
appliance_cart_db.carts
appliance_cart_db.cart_items
appliance_sales_db.sales
appliance_sales_db.outbox_event
appliance_notification_db.sale_notifications
```

Expected notification state:

```text
status = SENT
sent_at != NULL
last_error = NULL
```

---

## Check Email Notification

Open Mailpit:

```text
http://localhost:8025
```

The sale notification email should be available in the Mailpit inbox.

---

## Database Setup

The project uses one database per business service:

```text
appliance_product_db
appliance_cart_db
appliance_sales_db
appliance_notification_db
```

The MySQL initialization script is located at:

```text
infrastructure/mysql/init/01-init.sh
```

It creates:

```text
Required databases
Application database user
Debezium database user
Required privileges
```

The Debezium user is used by Kafka Connect to read the MySQL binlog.

---

## Event-Driven Sale Flow

When a sale is created, the Sales Service does not call the Notification Service directly.

Instead, it stores two things in the same database transaction:

```text
1. The Sale
2. An Outbox Event
```

This is the **Transactional Outbox pattern**.

```text
Sales Service
      |
      v
sales table + outbox_event table
      |
      v
Debezium reads MySQL binlog
      |
      v
Kafka topic: sale-created
      |
      v
Notification Service
```

This approach helps prevent losing events if Kafka or the Notification Service is temporarily unavailable.

---

## Transactional Outbox

The Sales Service creates an outbox event after creating a sale.

The outbox event contains the sale data needed by other services:

```json
{
  "saleId": 1,
  "cartId": 1,
  "total": 1500000.00,
  "saleDate": "2026-07-03T15:30:00",
  "customerEmail": "customer@example.com"
}
```

Debezium reads the outbox table and publishes the event to Kafka.

---

## Kafka and Debezium

Kafka is used for asynchronous communication between services.

Debezium is used as a Change Data Capture tool. It monitors the MySQL binlog and publishes outbox events to Kafka.

Main topic:

```text
sale-created
```

Dead Letter Topic:

```text
sale-created.DLT
```

Debezium connector:

```text
infrastructure/connectors/sales-outbox-connector.json
```

The connector reads from:

```text
appliance_sales_db.outbox_event
```

And routes events to:

```text
sale-created
```

---

## Notification Flow

The Notification Service consumes `SaleCreatedEvent` messages from Kafka.

For each message, it:

```text
1. Reads the event from Kafka
2. Extracts the Debezium outbox event id from Kafka headers
3. Checks if the event was already processed
4. Saves the notification as PENDING
5. Attempts to send the email
6. Marks the notification as SENT if successful
7. Marks the notification as FAILED if delivery fails
8. Sends the message to a Dead Letter Topic after retries are exhausted
```

Notification states:

```text
PENDING
SENT
FAILED
```

---

## Idempotency

The Notification Service stores the Kafka event id in the database.

This prevents duplicate email delivery if the same Kafka message is processed more than once.

```text
event_id -> unique identifier of the processed event
```

If the event was already successfully processed, the Notification Service skips it.

---

## Email Delivery

In development and Docker profiles, the project uses Mailpit to capture emails locally.

Mailpit inbox:

```text
http://localhost:8025
```

In the production profile, the Notification Service can be configured to use Gmail SMTP or another SMTP provider through environment variables.

Example production SMTP variables:

```text
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
NOTIFICATION_EMAIL_FROM=your-email@gmail.com
```

No email credentials should be committed to the repository.

---

## Retry and Dead Letter Topic

The Notification Service is configured with Kafka retries.

If email delivery fails, the consumer retries the message.

If all retries fail, the message is published to:

```text
sale-created.DLT
```

This prevents the consumer from being blocked forever by a failing message.

---

## Example Sale Request

Through the API Gateway:

```http
POST http://localhost:8088/sales/api/sales
Content-Type: application/json
```

Directly to Sales Service:

```http
POST http://localhost:8083/api/sales
Content-Type: application/json
```

Request body:

```json
{
  "cartId": 1,
  "customerEmail": "customer@example.com"
}
```

Expected flow:

```text
Sale is created
Outbox event is stored
Debezium publishes event to Kafka
Notification Service consumes the event
Email notification is sent
Notification status becomes SENT
```

---

## Config Server Endpoints

Configuration can be checked through Config Server.

### Development profile

```text
http://localhost:8888/product/dev
http://localhost:8888/cart/dev
http://localhost:8888/sales/dev
http://localhost:8888/notification/dev
http://localhost:8888/api-gateway/dev
```

### Docker profile

```text
http://localhost:8888/product/docker
http://localhost:8888/cart/docker
http://localhost:8888/sales/docker
http://localhost:8888/notification/docker
http://localhost:8888/api-gateway/docker
```

### Production profile

```text
http://localhost:8888/product/prod
http://localhost:8888/cart/prod
http://localhost:8888/sales/prod
http://localhost:8888/notification/prod
http://localhost:8888/api-gateway/prod
```

---

## Environment Profiles

The project uses Spring Profiles to separate environments.

```text
dev     -> local development from IntelliJ
docker  -> complete Docker Compose environment
prod    -> production-like configuration
```

Each service has common, development, docker and production configuration files in the configuration repository.

Example:

```text
product.yaml
product-dev.yaml
product-docker.yaml
product-prod.yaml

cart.yaml
cart-dev.yaml
cart-docker.yaml
cart-prod.yaml

sales.yaml
sales-dev.yaml
sales-docker.yaml
sales-prod.yaml

notification.yaml
notification-dev.yaml
notification-docker.yaml
notification-prod.yaml

api-gateway.yaml
api-gateway-docker.yaml
```

### Development Profile

The `dev` profile is used when running services locally from IntelliJ.

It uses:

```text
Local Docker MySQL exposed to the host
Local Kafka exposed to the host
Mailpit exposed to the host
Hibernate ddl-auto update
SQL logs enabled
```

### Docker Profile

The `docker` profile is used when running the full system with Docker Compose.

Inside Docker, services do not communicate through `localhost`. They communicate through Docker service names.

Examples:

```text
mysql:3306
kafka:19092
mailpit:1025
config-server:8888
eureka-server:8761
```

### Production Profile

The `prod` profile is production-oriented.

It uses:

```text
Database URL from environment variables
Database credentials from environment variables
Kafka bootstrap servers from environment variables
SMTP credentials from environment variables
Hibernate ddl-auto validate
SQL logs disabled
```

No real credentials are stored in the repository.

---

## Local Startup Order With IntelliJ

When running services manually from IntelliJ, use this startup order:

```text
1. Docker infrastructure
2. Config Server
3. Eureka Server
4. Product Service
5. Cart Service
6. Sales Service
7. Notification Service
8. API Gateway
```

Each main service should run with the `dev` profile when working locally from IntelliJ.

Example environment variables:

```text
SPRING_PROFILES_ACTIVE=dev
APPLIANCE_DB_USERNAME=appliance_app
APPLIANCE_DB_PASSWORD=your-local-password
```

---

## Production-Like Configuration

The `prod` profile is designed to receive sensitive values from environment variables.

Example for Product Service:

```text
SPRING_PROFILES_ACTIVE=prod
PRODUCT_DB_URL=jdbc:mysql://host:3306/appliance_product_db
PRODUCT_DB_USERNAME=product_user
PRODUCT_DB_PASSWORD=secret
```

Example for Notification Service:

```text
SPRING_PROFILES_ACTIVE=prod
NOTIFICATION_DB_URL=jdbc:mysql://host:3306/appliance_notification_db
NOTIFICATION_DB_USERNAME=notification_user
NOTIFICATION_DB_PASSWORD=secret
KAFKA_BOOTSTRAP_SERVERS=kafka-host:9092
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=email@gmail.com
MAIL_PASSWORD=app-password
NOTIFICATION_EMAIL_FROM=email@gmail.com
```

These values are examples only. Real credentials must be provided through environment variables and must not be committed to the repository.

---

## Useful URLs

```text
API Gateway:     http://localhost:8088
Eureka:          http://localhost:8761
Config Server:   http://localhost:8888
Mailpit:         http://localhost:8025
phpMyAdmin:      http://localhost:8085
Kafka Connect:   http://localhost:8086
```

---

## Useful Docker Commands

From CMD, inside:

```text
appliance-store-platform\infrastructure
```

Start the platform:

```cmd
docker compose up -d --build
```

Stop the platform:

```cmd
docker compose down
```

Stop and remove volumes:

```cmd
docker compose down -v
```

Check running containers:

```cmd
docker compose ps
```

Check logs:

```cmd
docker compose logs --tail=100 config-server
docker compose logs --tail=100 eureka-server
docker compose logs --tail=100 api-gateway
docker compose logs --tail=100 product-service
docker compose logs --tail=100 cart-service
docker compose logs --tail=100 sales-service
docker compose logs --tail=100 notification-service
```

Register Debezium connector:

```cmd
powershell -Command "Invoke-RestMethod -Method Post -Uri 'http://localhost:8086/connectors' -ContentType 'application/json' -InFile 'connectors\sales-outbox-connector.json'"
```

Check Debezium connector:

```cmd
powershell -Command "Invoke-RestMethod -Uri 'http://localhost:8086/connectors/sales-outbox-connector/status'"
```

---

## Key Learning Goals

This project was built to practice and demonstrate:

- Spring Boot microservices
- Spring Cloud ecosystem
- Service discovery
- Centralized configuration
- API Gateway routing
- Inter-service communication with OpenFeign
- Resilience patterns
- Kafka-based asynchronous communication
- Transactional Outbox pattern
- Change Data Capture with Debezium
- Idempotent consumers
- Email notification processing
- Retries and Dead Letter Topics
- Environment-specific configuration
- Dockerized microservices
- Postman automated flow testing

---

## What This Project Demonstrates

This project demonstrates how to build a backend system close to a real-world microservices architecture.

It includes synchronous communication with OpenFeign, asynchronous communication with Kafka, service discovery with Eureka, centralized configuration with Spring Cloud Config, API Gateway routing, resilience patterns, Docker-based infrastructure, event-driven processing with Debezium and the Transactional Outbox pattern, email delivery through Mailpit or SMTP, and a Postman collection to validate the complete business flow.

---

## Author

**Jonathan Leonel Martins**  
Java Backend Developer  
Buenos Aires, Argentina

LinkedIn:

```text
https://www.linkedin.com/in/jonathan-leonel-martins-530309193/
```