# Auth0 Integration API
This project provides an API to integrate with Auth0 for managing users and organizations. The API allows creating
users, creating organizations, adding users to organizations, and verifying tokens.

## Getting Started

### Prerequisites

- Java 11 or later
- Spring Boot
- Auth0 account

### Installation

1. Clone the repository:
   ```sh
   https://github.com/akhter-rakib/spring-security-master.git
   cd auth0

2. Update application properties
   Update the application.properties file with your Auth0 credentials:

```
    domain: ${AUTH0_DOMAIN}

    clientId: ${AUTH0_CLIENT_ID}

    clientSecret: ${AUTH0_CLIENT_SECRET}

    issuer: ${AUTH0_ISSUER}
```

3. Build and run the application:
```
    ./mvnw clean install
    ./mvnw spring-boot:run
```

**API Documentation**

**Create User**
* URL: /auth0/users
* Method: POST
* Request Body:

  ```
  {
   "email": "user@example.com",
   "name": "User",
   "password": "your-password-here"
  }
  ```
    
Response:
*   200 OK: User created successfully
*   500 Internal Server Error: Failed to create user

**Create Organization**
* URL: /auth0/organizations
* Method: POST
* Request Body:
```
{
  "name": "example-organization",
  "displayName": "Example Organization"
}
```
Response:
* 200 OK: Organization created successfully
* 500 Internal Server Error: Failed to create organization

**Get User Info by Token**
* URL: /auth0/user/info
* Method: GET
* Request Body:
```
{
  "token": "your-access-token-here"
}
```
**Response:**

* 200 OK: Returns user info
* 500 Internal Server Error: Failed to get user info

**Verify Token**

* URL: /auth0/verify
* Method: POST
* Request Body:
```
{
  "token": "your-token-here"
}
```
Response:
* 200 OK: Token is valid
* 401 Unauthorized: Token is invalid

**Add User to Organization**
* URL: /auth0/organizations/{organizationId}/users/{userId}
* Method: POST

Response:
* 200 OK: User added to organization successfully
* 500 Internal Server Error: Failed to add user to organization

**Create Organization and User**

* URL: /auth0/organizations-and-users
* Method: POST
* Request Body:
```
  {
    "organizationName": "example-organization",
    "organizationDisplayName": "Example Organization",
    "userEmail": "user@example.com",
    "userName": "Example User",
    "password": "your-password-here"
  }
```
Response:
* 200 OK: Organization and user created successfully
