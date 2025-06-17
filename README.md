# Inventory and Order Management System

A comprehensive RESTful API for managing inventory items and orders, built with Spring Boot, Spring Security, and JWT authentication.

## Table of Contents

1. [Features](#features)
2. [Technologies](#technologies)
3. [Setup and Installation](#setup-and-installation)
   - [Traditional Setup](#traditional-setup)
   - [Docker Setup](#docker-setup)
4. [Running the Application](#running-the-application)
   - [Running with Maven](#running-with-maven)
   - [Running with Docker](#running-with-docker)
5. [API Documentation](#api-documentation)
6. [JWT Authentication](#jwt-authentication)
7. [Sample API Requests and Responses](#sample-api-requests-and-responses)
8. [Architecture Overview](#architecture-overview)

## Features

- User authentication and authorization with JWT
- Role-based access control (ADMIN and CUSTOMER roles)
- Inventory item management
- Order processing and tracking
- Refresh token support
- Centralized exception handling
- Docker support for easy deployment

## Technologies

- Java 17
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JSON Web Tokens)
- Maven
- Lombok
- Docker & Docker Compose

## Setup and Installation

### Traditional Setup

#### Prerequisites

- Java Development Kit (JDK) 17 or higher
- PostgreSQL
- Maven
- Git

#### Steps

1. **Clone the repository**

```bash
git clone https://github.com/yourusername/Inventory-And-Order-Management-System-Application.git
cd Inventory-And-Order-Management-System-Application
```

2. **Configure PostgreSQL**

Create a PostgreSQL database named `inventory`:

```sql
CREATE DATABASE inventory;
```

3. **Configure application.properties**

Update the database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. **Build the project**

```bash
mvn clean install
```

### Docker Setup

#### Prerequisites

- Docker
- Docker Compose

#### Steps

1. **Clone the repository**

```bash
git clone https://github.com/yourusername/Inventory-And-Order-Management-System-Application.git
cd Inventory-And-Order-Management-System-Application
```

2. **No additional configuration needed!** 
   The Docker setup handles all dependencies and configurations automatically.

## Running the Application

### Running with Maven

Start the application using Maven:

```bash
mvn spring-boot:run
```

The server will start on port 8080 by default: http://localhost:8080

### Running with Docker

Start the entire application stack (Spring Boot app + PostgreSQL) using Docker Compose:

```bash
docker-compose up -d
```

This will:
- Start a PostgreSQL container
- Build and start the Spring Boot application container
- Link them together
- Expose the application on port 8080

To check the logs:

```bash
docker-compose logs -f app
```

To stop the application:

```bash
docker-compose down
```

To stop and remove all data volumes:

```bash
docker-compose down -v
```

**Note**: On first run, the application will automatically create an admin user:
- Username: `admin`
- Password: `soumyadeep2025`

## API Documentation

The application exposes the following endpoints:

### Public Endpoints

- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login with username and password
- `POST /api/v1/auth/refresh-token` - Refresh an expired JWT token
- `GET /api/v1/items` - List all items
- `GET /api/v1/items/{id}` - Get item by ID
- `GET /api/v1/items/search?name={query}` - Search items by name

### Protected Endpoints

#### User Endpoints (ADMIN only)

- `GET /api/v1/users` - List all users
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/username/{username}` - Get user by username
- `POST /api/v1/users` - Create a new user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

#### Item Endpoints (Authenticated)

- `POST /api/v1/items` - Create a new item (ADMIN only)
- `PUT /api/v1/items/{id}` - Update item (ADMIN only)
- `DELETE /api/v1/items/{id}` - Delete item (ADMIN only)

#### Order Endpoints (Authenticated)

- `GET /api/v1/orders` - List all orders (ADMIN only)
- `GET /api/v1/orders/{id}` - Get order by ID (ADMIN or order owner)
- `GET /api/v1/orders/user/{userId}` - Get orders by user ID (ADMIN or user themselves)
- `POST /api/v1/orders` - Create a new order (any authenticated user)
- `DELETE /api/v1/orders/{id}` - Delete order (ADMIN only)

#### Order Item Endpoints (Authenticated)

- `GET /api/v1/order-items` - List all order items (ADMIN only)
- `GET /api/v1/order-items/item/{itemId}` - Get total orders for an item (any authenticated user)
- `GET /api/v1/order-items/item/{itemId}/user/{username}` - Get total orders for an item by user (ADMIN or user themselves)
- `POST /api/v1/order-items/item/{itemId}` - Save total orders for an item (ADMIN only)
- `POST /api/v1/order-items/item/{itemId}/user/{username}` - Save total orders for an item by user (ADMIN or user themselves)

## JWT Authentication

The application uses JWT (JSON Web Token) for authentication and authorization. Here's how to use it:

### 1. Register a new user

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "role": "CUSTOMER"
}
```

### 2. Login to get tokens

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "abc123...",
  "username": "testuser",
  "role": "CUSTOMER",
  "expiresIn": 86400
}
```

### 3. Use the access token in subsequent requests

Add the token to the Authorization header:

```http
GET /api/v1/items
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### 4. Refresh an expired token

When the access token expires, use the refresh token to get a new one:

```http
POST /api/v1/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "abc123..."
}
```

## Sample API Requests and Responses

### Authentication

#### 1. Login (Admin)

##### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "soumyadeep2025"
  }'
```

##### Sample Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxODYxMDQxNSwiZXhwIjoxNzE4Njk2ODE1fQ.LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ-WzGxSfVY",
  "refreshToken": "e37afb92-6494-4f59-8d7f-43e39e0a91c7",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

##### Postman Example

**Request**:
- Method: POST
- URL: `{{baseUrl}}/api/v1/auth/login`
- Headers: `Content-Type: application/json`
- Body:
```json
{
  "username": "admin",
  "password": "soumyadeep2025"
}
```

#### 2. Register a New User

##### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "role": "CUSTOMER"
  }'
```

##### Sample Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXd1c2VyIiwiaWF0IjoxNzE4NjEwNDE1LCJleHAiOjE3MTg2OTY4MTV9.HrB31_KeR_LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ",
  "refreshToken": "d5a9f3b8-7e1c-4f11-9d3e-c8b2e6a5f7d9",
  "username": "newuser",
  "role": "CUSTOMER",
  "expiresIn": 86400
}
```

##### Postman Example

**Request**:
- Method: POST
- URL: `{{baseUrl}}/api/v1/auth/register`
- Headers: `Content-Type: application/json`
- Body:
```json
{
  "username": "newuser",
  "password": "password123",
  "role": "CUSTOMER"
}
```

### Item Management

#### 1. Create a New Item (Admin Only)

##### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxODYxMDQxNSwiZXhwIjoxNzE4Njk2ODE1fQ.LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ-WzGxSfVY" \
  -d '{
    "name": "MacBook Pro 16",
    "quantity": 20,
    "price": 2499.99
  }'
```

##### Sample Response

```json
{
  "id": 1,
  "name": "MacBook Pro 16",
  "quantity": 20,
  "price": 2499.99
}
```

##### Postman Example

**Request**:
- Method: POST
- URL: `{{baseUrl}}/api/v1/items`
- Headers: 
  - `Content-Type: application/json`
  - `Authorization: Bearer {{jwtToken}}`
- Body:
```json
{
  "name": "MacBook Pro 16",
  "quantity": 20,
  "price": 2499.99
}
```

#### 2. Get All Items (Public)

##### cURL Example

```bash
curl -X GET http://localhost:8080/api/v1/items
```

##### Sample Response

```json
[
  {
    "id": 1,
    "name": "MacBook Pro 16",
    "quantity": 20,
    "price": 2499.99
  },
  {
    "id": 2,
    "name": "iPhone 15 Pro",
    "quantity": 50,
    "price": 999.99
  }
]
```

##### Postman Example

**Request**:
- Method: GET
- URL: `{{baseUrl}}/api/v1/items`

### Order Management

#### 1. Create a New Order

##### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcxODYxMDQxNSwiZXhwIjoxNzE4Njk2ODE1fQ.HrB31_KeR_LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ" \
  -d '{
    "userId": 2,
    "itemIds": [1, 2],
    "timestamp": "2025-06-17T16:00:00"
  }'
```

##### Sample Response

```json
{
  "id": 1,
  "userId": 2,
  "username": "testuser",
  "itemIds": [1, 2],
  "totalPrice": 3499.98,
  "timestamp": "2025-06-17T16:00:00"
}
```

##### Postman Example

**Request**:
- Method: POST
- URL: `{{baseUrl}}/api/v1/orders`
- Headers: 
  - `Content-Type: application/json`
  - `Authorization: Bearer {{jwtToken}}`
- Body:
```json
{
  "userId": 2,
  "itemIds": [1, 2],
  "timestamp": "2025-06-17T16:00:00"
}
```

#### 2. Get Orders for User

##### cURL Example

```bash
curl -X GET http://localhost:8080/api/v1/orders/user/2 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcxODYxMDQxNSwiZXhwIjoxNzE4Njk2ODE1fQ.HrB31_KeR_LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ"
```

##### Sample Response

```json
[
  {
    "id": 1,
    "userId": 2,
    "username": "testuser",
    "itemIds": [1, 2],
    "totalPrice": 3499.98,
    "timestamp": "2025-06-17T16:00:00"
  }
]
```

##### Postman Example

**Request**:
- Method: GET
- URL: `{{baseUrl}}/api/v1/orders/user/2`
- Headers: `Authorization: Bearer {{jwtToken}}`

### User Management (Admin Only)

#### 1. Get All Users

##### cURL Example

```bash
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxODYxMDQxNSwiZXhwIjoxNzE4Njk2ODE1fQ.LpMJLw9T5hO4EgFBG9Hq7K8V0Zf9Gj8RQJ-WzGxSfVY"
```

##### Sample Response

```json
[
  {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  },
  {
    "id": 2,
    "username": "testuser",
    "role": "CUSTOMER"
  }
]
```

##### Postman Example

**Request**:
- Method: GET
- URL: `{{baseUrl}}/api/v1/users`
- Headers: `Authorization: Bearer {{jwtToken}}`

## Postman Collection

A complete Postman collection for testing all endpoints is available in the repository. To import and use:

1. Open Postman
2. Click on "Import" button
3. Select the `Inventory-Order-Management-System.postman_collection.json` file from the repository
4. Create a new environment with a variable named `baseUrl` set to `http://localhost:8080`
5. Run the authentication requests first to get a valid JWT token
6. The collection will automatically save the token for subsequent requests

## Architecture Overview

The application follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic and applies security rules
- **Repository Layer**: Handles data access
- **Model Layer**: Defines entity objects
- **DTO Layer**: Data Transfer Objects for API interaction
- **Security Layer**: JWT authentication and authorization

### Security Implementation

The application uses Spring Security with JWT for authentication and authorization:

- Token-based authentication with refresh token mechanism
- Role-based access control using Spring Security's `@PreAuthorize` annotations
- Method-level security for fine-grained access control
- Cross-Origin Resource Sharing (CORS) configuration for web clients

### Additional Notes

- Access tokens expire after 24 hours (configurable in application.properties)
- Refresh tokens expire after 7 days (configurable in application.properties)
- User passwords are securely stored using BCrypt encryption
- All API responses follow a standardized format for consistency
