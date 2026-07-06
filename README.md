# Appliance Store Platform

Appliance Store Platform is a microservices-based backend system built with Java, Spring Boot and Spring Cloud.

The project simulates an appliance store where products can be managed, carts can be created, sales can be completed, and sale notifications are processed asynchronously through Kafka using a professional event-driven architecture.

The main goal of this project is to practice real-world backend concepts such as microservices, centralized configuration, service discovery, API Gateway, database-per-service, resilience patterns, Kafka, Transactional Outbox, Debezium CDC, asynchronous notifications, retries and Dead Letter Topics.

---

## Architecture Overview

```text
Client / Postman
      |
      v
API Gateway
      |
      +--------------------+
      |                    |
      v                    v
Product Service       Cart Service
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
                  Mailpit / Gmail SMTP
```

---

## Microservices

| Service | Port | Responsibility |
|---|---:|---|
| Config Server | 8888 | Centralized configuration using Spring Cloud Config |
| Eureka Server | 8761 | Service discovery |
| API Gateway | 8088 | Single entry point for client requests |
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
- Resilience4j Retry and Circuit Breaker
- Apache Kafka
- Kafka Connect
- Debezium CDC
- Transactional Outbox pattern
- Event-driven communication
- Idempotent Kafka consumer
- Email notification service
- Mailpit for local email testing
- Gmail SMTP support for production profile
- Kafka retries
- Dead Letter Topic
- Environment-based configuration with Spring Profiles
- Docker Compose full platform setup
- Postman collection for complete flow testing

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
- Resilience4j
- MySQL 8.4
- Apache Kafka
- Kafka Connect
- Debezium
- Docker Compose
- Mailpit
- Maven
- Postman

---

## Repositories

This project uses two repositories:

```text
appliance-store-platform  -> source code, Docker infrastructure, documentation and Postman collection
appliance-store-config    -> centralized configuration files
```

The Config Server reads configuration from the external configuration repository.

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
└── README.md
```

---

## Infrastructure

The local infrastructure is defined in:

```text
infrastructure/compose.yaml
```

It includes:

```text
MySQL
phpMyAdmin
Apache Kafka
Kafka Connect with Debezium
Mailpit
Config Server
Eureka Server
API Gateway
Product Service
Cart Service
Sales Service
Notification Service
```

Useful URLs:

```text
phpMyAdmin:       http://localhost:8085
Kafka Connect:   http://localhost:8086
Mailpit:         http://localhost:8025
Eureka:          http://localhost:8761
Config Server:   http://localhost:8888
API Gateway:     http://localhost:8088
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

### 1. Create the `.env` file

Inside:

```text
appliance-store-platform/infrastructure
```

Create a `.env` file.

Example:

```env
MYSQL_ROOT_PASSWORD=your-root-password

APPLIANCE_DB_USERNAME=appliance_app
APPLIANCE_DB_PASSWORD=your-app-password

DEBEZIUM_DB_USER=debezium
DEBEZIUM_DB_PASSWORD=your-debezium-password

CONFIG_REPO_URI=https://github.com/your-user/appliance-store-config
CONFIG_REPO_BRANCH=main
```

The `.env` file must not be committed to the repository.

### 2. Start the complete platform

From CMD, go to:

```cmd
cd appliance-store-platform\infrastructure
```

Then run:

```cmd
docker compose up -d --build
```

To check the containers:

```cmd
docker compose ps
```

Expected result:

```text
appliance-config-server         Up
appliance-eureka-server         Up
appliance-api-gateway           Up
appliance-product-service       Up
appliance-cart-service          Up
appliance-sales-service         Up
appliance-notification-service  Up
appliance-mysql                 Up healthy
appliance-kafka                 Up healthy
appliance-kafka-connect         Up
appliance-mailpit               Up healthy
appliance-phpmyadmin            Up
```

### 3. Register the Debezium connector

The Debezium connector file is located at:

```text
infrastructure/connectors/sales-outbox-connector.json
```

From CMD, inside `infrastructure`, run:

```cmd
powershell -Command "Invoke-RestMethod -Method Post -Uri 'http://localhost:8086/connectors' -ContentType 'application/json' -InFile 'connectors\sales-outbox-connector.json'"
```

If the connector already exists, this is not an error.

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

### 4. Check Eureka

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

### 5. Run the Postman complete flow

Import the Postman collection and environment located in:

```text
postman/appliance-store-microservices.postman_collection.json
postman/appliance-store-local.postman_environment.json
```

Select:

```text
Appliance Store - Local
```

Run the folder:

```text
04 - Complete Flow
```

Expected result:

```text
Create Product  -> 201
Create Cart     -> 201
Add Item        -> 200
Create Sale     -> 201
Check Sale      -> 200
```

### 6. Validate the database flow

Open phpMyAdmin:

```text
http://localhost:8085
```

Run the following checks.

Latest product:

```sql
USE appliance_product_db;

SELECT *
FROM products
ORDER BY id DESC
LIMIT 1;
```

Latest cart:

```sql
USE appliance_cart_db;

SELECT *
FROM carts
ORDER BY id DESC
LIMIT 1;
```

Latest cart item:

```sql
USE appliance_cart_db;

SELECT *
FROM cart_items
ORDER BY id DESC
LIMIT 1;
```

Latest sale:

```sql
USE appliance_sales_db;

SELECT *
FROM sales
ORDER BY id DESC
LIMIT 1;
```

Latest outbox event:

```sql
USE appliance_sales_db;

SELECT *
FROM outbox_event
ORDER BY id DESC
LIMIT 1;
```

Latest sale notification:

```sql
USE appliance_notification_db;

SELECT *
FROM sale_notifications
ORDER BY id DESC
LIMIT 1;
```

Expected notification state:

```text
status = SENT
sent_at != NULL
last_error = NULL
```

### 7. Check the email

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

This is the Transactional Outbox pattern.

```text
Sales Service
      |
      v
sale table + outbox_event table
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

This prevents losing events if Kafka or the Notification Service is temporarily unavailable.

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

In development and Docker profile, the project uses Mailpit to capture emails locally.

Mailpit inbox:

```text
http://localhost:8025
```

In production profile, the Notification Service can be configured to use Gmail SMTP or another SMTP provider through environment variables.

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

## Local Testing Flow

A complete local test can be done with the following steps:

```text
1. Create a product
2. Create a cart
3. Add products to the cart
4. Create a sale
5. Check the outbox_event table
6. Check the sale_notifications table
7. Check the email in Mailpit
```

Mailpit:

```text
http://localhost:8025
```

phpMyAdmin:

```text
http://localhost:8085
```

---

## Postman Collection

This project includes a Postman collection to test the main microservices flow through the API Gateway.

Location:

```text
postman/appliance-store-microservices.postman_collection.json
postman/appliance-store-local.postman_environment.json
```

### Importing the Collection

To use the Postman collection:

```text
1. Open Postman
2. Import appliance-store-microservices.postman_collection.json
3. Import appliance-store-local.postman_environment.json
4. Select the environment: Appliance Store - Local
5. Run the folder: 04 - Complete Flow
```

### Environment Variables

The local environment contains the following variables:

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

### Main Complete Flow

The folder `04 - Complete Flow` runs the complete purchase flow in order:

```text
1. Create Product
2. Create Cart
3. Add Item to Cart
4. Create Sale
5. Check Sale
```

This validates that the main business flow works correctly through the API Gateway.

### Complete Flow Endpoints

```text
POST {{gatewayUrl}}/product/api/product
POST {{gatewayUrl}}/cart/api/cart
POST {{gatewayUrl}}/cart/api/cart/{{cartId}}/items
POST {{gatewayUrl}}/sales/api/sales
GET  {{gatewayUrl}}/sales/api/sales/{{saleId}}
```

### What the Collection Validates

The Postman collection validates:

```text
Product Service CRUD operations
Cart Service operations
Cart item management
Sales Service operations
API Gateway routing
Complete purchase flow
Basic success scenarios
Basic exception scenarios
```

The complete flow also triggers the asynchronous notification process implemented in the backend through the Transactional Outbox pattern, Debezium, Kafka and the Notification Service.

After running the complete flow, the following tables can be checked:

```text
appliance_product_db.products
appliance_cart_db.carts
appliance_cart_db.cart_items
appliance_sales_db.sales
appliance_sales_db.outbox_event
appliance_notification_db.sale_notifications
```

The email notification can be checked in Mailpit:

```text
http://localhost:8025
```

---

## Config Server Endpoints

Configuration can be checked through Config Server.

Development profile:

```text
http://localhost:8888/product/dev
http://localhost:8888/cart/dev
http://localhost:8888/sales/dev
http://localhost:8888/notification/dev
http://localhost:8888/api-gateway/dev
```

Docker profile:

```text
http://localhost:8888/product/docker
http://localhost:8888/cart/docker
http://localhost:8888/sales/docker
http://localhost:8888/notification/docker
http://localhost:8888/api-gateway/docker
```

Production profile:

```text
http://localhost:8888/product/prod
http://localhost:8888/cart/prod
http://localhost:8888/sales/prod
http://localhost:8888/notification/prod
http://localhost:8888/api-gateway/prod
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

This project was built to practice:

```text
Spring Boot microservices
Spring Cloud ecosystem
Service discovery
Centralized configuration
API Gateway routing
Inter-service communication with Feign
Resilience patterns
Kafka-based asynchronous communication
Transactional Outbox pattern
Change Data Capture with Debezium
Idempotent consumers
Email notification processing
Retries and Dead Letter Topics
Environment-specific configuration
Dockerized microservices
Postman automated flow testing
```

---

## What This Project Demonstrates

This project demonstrates how to build a backend system that is closer to a real-world microservices architecture.

It includes synchronous communication with Feign, asynchronous communication with Kafka, service discovery with Eureka, centralized configuration with Spring Cloud Config, gateway routing, resilience patterns, local infrastructure with Docker, event-driven processing with Debezium and the Transactional Outbox pattern, email delivery through Mailpit or SMTP, and a Postman collection to validate the complete business flow.

---

## Author

Jonathan Leonel Martins