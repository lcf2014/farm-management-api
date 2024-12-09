
# Farm Management API

## Project Overview

The Farm Management API provides endpoints to manage farms and their crop productions. This API allows users to create farms with nested crop production records, delete farms, and list farms with support for pagination and filtering.

Key features:

- Create Farm with crop production details.
- Delete Farm by its ID.
- List Farms with support for filtering by crop type or land area.
- Swagger UI integration for easy API exploration.

## Setup

### Prerequisites

- Java 17 (or higher)
- Maven
- Docker (optional, for deployment)





## Steps to Build, Run, and Test the Application

1. Clone the Repository


```bash
git clone REPOSITORY_URL
```

2. Build the Project

```bash
mvn clean install
```

3. Run the Application Locally

```bash
mvn spring-boot:run
```

This will start the application on http://localhost:8080.

4. Run Tests
To run unit and integration tests:

```bash
mvn test
```

5. API Documentation
Swagger is integrated into the application, and you can access it by running the application locally and navigating to:

```bash
http://localhost:8080/swagger-ui.html
```

## Docker Usage
This project includes a Dockerfile and Docker Compose configuration to make it easy to deploy the application.

### Steps to Deploy with Docker

1. Build the Docker Image after building project
Make sure Docker is installed and running. Then, build the Docker image using the following command:

```bash
docker build -t farm-management-api .
```

2. Run the Docker Container
Run the application in a Docker container:

```bash
docker run -p 8080:8080 farm-management-api
```

This will make the API available at http://localhost:8080 inside your browser or API client.

3. Using Docker Compose

For easier deployment with a PostgreSQL database, you can use Docker Compose. This will spin up both the application and the database.

Run the Application with Docker Compose:

```bash
docker-compose up
```

This will start both the application and the PostgreSQL container. The application will be available at http://localhost:8080.

## Authentication
The API uses JSON Web Tokens (JWT) for securing endpoints. JWT is used to authenticate users by generating a token that must be included in the header of requests requiring authentication.

### JWT Generation

Endpoint for generating JWT:

- URL: POST /api/auth/login
- Description: This endpoint is used to authenticate the user and generate a JWT token.
- Request Body (JSON):

```bash
{
  "username": "your_username",
  "password": "your_password"
}
```
- Response (JSON):

```bash
{
  "token": "your_jwt_token_here"
}
```

- Example using cURL:

```bash
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username": "your_username", "password": "your_password"}'
```
The response will contain the JWT token, which you need to include in the Authorization header of subsequent requests that require authentication.

## Usage of JWT in Requests
Once you have your JWT token, you need to include it in the Authorization header of your requests. The token should be prefixed with Bearer.

Example Usage in Postman

1) Open Postman and select the desired request (e.g., GET, POST, DELETE).
2) Under the Headers section, add the key Authorization and the value Bearer your_jwt_token_here.
3) Send the request, and the server will validate the JWT before processing the request. 

## How the JWT is validated
The server uses the JWT to authenticate requests:

1) The JWT is extracted from the Authorization header of each incoming request. 
2) The token is then validated to ensure it is not expired and is properly signed with the correct secret.
3) If the token is valid, the request proceeds; otherwise, a 401 Unauthorized response is returned.

## JWT Expiry

JWT tokens typically have an expiration time (exp), and you will need to log in again to generate a new token if your token expires. You can configure the expiration time when generating the JWT.



## Endpoints

Here are the API endpoints available in this API:

## 1. Create Farm

- Endpoint: POST /api/farms

- Description: Creates a new farm with associated crop production records.

- Request Body:

```bash
{
  "name": "Test Farm",
  "landArea": 100.0,
  "unitOfMeasure": "acre",
  "address": "123 Farm Lane",
  "productions": [
    {
      "cropType": "RICE",
      "isIrrigated": true,
      "isInsured": true
    }
  ]
}
```

- Response

```bash
{
  "id": 1,
  "name": "Test Farm",
  "landArea": 100.0,
  "unitOfMeasure": "acre",
  "address": "123 Farm Lane",
  "productions": [
    {
      "cropType": "RICE",
      "isIrrigated": true,
      "isInsured": true
    }
  ]
}
```

- Response Status: 201 Created

## 2. Delete Farm

- Endpoint: DELETE /api/farms/{id}
- Description: Deletes a farm by its ID.
- Path Parameter: id (The ID of the farm to delete)
- Response Status: 204 No Content

## 3. List Farms

- Endpoint: GET /api/farms
- Description: Lists all farms, supporting pagination and filtering.
- Request Parameters:
  - page (optional): Page number for pagination
  - size (optional): Page size for pagination
  - cropType (optional): Filter farms by crop type (RICE, BEANS, etc.)
  - landAreaMin (optional): Filter farms with land area greater than or equal to this value
  - landAreaMax (optional): Filter farms with land area less than or equal to this value

- Example Request:

```bash
GET /api/farms?page=0&size=10&cropType=RICE
```

- Response

```bash
{
  "content": [
    {
      "id": 1,
      "name": "Test Farm",
      "landArea": 100.0,
      "unitOfMeasure": "acre",
      "address": "123 Farm Lane",
      "productions": []
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 3,
    "totalPages": 1
  }
}

```

- Response Status: 200 OK

## Swagger Documentation

Swagger provides a web-based interface to explore and interact with the API. You can access the Swagger UI after running the application locally or in Docker.

1) Open a browser and navigate to:

```bash
http://localhost:8080/swagger-ui.html
```

2) The Swagger UI will show the available endpoints and allow you to try out the API by sending requests directly from the browser.

## Troubleshooting

- Database Connection Issues: Make sure the PostgreSQL database is running and accessible. If using Docker, check the logs with docker-compose logs db.
- Invalid Requests: Ensure all required fields are included in the request body when creating a farm or its production records.










