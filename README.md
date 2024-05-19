# REST API Application

This is a simple REST API application for managing users.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) installed on your machine. `java -version`

## Getting Started

To get started with this application, follow these steps:

1. Open the project in your IDE.
2. Build the project with the command in terminal: `mvn clean install`.

## Starting the Application

### From the Terminal:

1. Navigate to the target directory: `cd target`
2. Run the JAR file: `java -jar testTask-0.0.1-SNAPSHOT.jar`


### From the IDE:

1. Run the `Application` class directly from your IDE.

## Testing the API

Once the application is running, you can test the API using tools like cURL or Postman.

### Testing Endpoints with cURL

Here are some sample cURL commands to test the API endpoints:

**- Create a new user**:

`curl -X POST http://localhost:8080/api/v1/users -H "Content-Type: application/json" -d '{"email": "john.doe@example.com", "firstName": "John", "lastName": "Doe", "birthDate": "1990-05-15"}'`
- in the terminal you will see message: 
  - `Creating user: User{email='john.doe@example.com', firstName='John', lastName='Doe', birthDate=1990-05-15, address='null', phoneNumber='null'}`
  - `User created: User{email='john.doe@example.com', firstName='John', lastName='Doe', birthDate=1990-05-15, address='null', phoneNumber='null'}`

**- Update an existing user:**

`curl -X PUT http://localhost:8080/api/v1/users/john.doe@example.com -H "Content-Type: application/json" -d '{"firstName": "John", "lastName": "Doe", "birthDate": "1990-05-15"}'`
- in the terminal you will see message:
  - `User User{email='null', firstName='John', lastName='Doe', birthDate=1990-05-15, address='null', phoneNumber='null'} updated successfully.`

**- Delete a user:**
`curl -X DELETE http://localhost:8080/api/v1/users/john.doe@example.com`
- in the terminal you will see message:
  - `User with e-mail john.doe@example.com removed.`

**- Search users by birthdate range:**

`curl -X GET 'http://localhost:8080/api/v1/users/search?from=1990-01-01&to=2000-12-31'`



# rest_api_test_task
