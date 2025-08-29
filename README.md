# Virtual Bank System

## Motivation

The *Virtual Bank System* is a microservice-based architecture aimed at simulating a virtual banking environment. The system leverages modern software design patterns such as Microservices, Backend for Frontend (BFF), and API Gateway to create a scalable, secure, and easily extensible banking solution.

## Project Goal

The primary goal of this project is to design, implement, and deploy a simplified virtual banking system to demonstrate core concepts of:

* *Java Microservices*: Independent and loosely coupled services responsible for various banking functionalities.
* *Backend for Frontend (BFF)*: A service that aggregates backend data and optimizes API consumption for frontend applications.
* *WSO2 API Gateway*: A centralized API management system handling security, routing, and request/response transformations.

This project was developed as part of a 1-month internship, where real-world concepts such as microservices communication, API Gateway, user authentication, and scalable design were implemented.

---
## Tech/Framework used

![Java](https://img.shields.io/badge/Java-11-blue?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-Microservices-success?logo=springboot)
![Maven](https://img.shields.io/badge/Build-Maven-brightgreen?logo=apachemaven)
![REST API](https://img.shields.io/badge/API-RESTful-ff69b4)
![Postman](https://img.shields.io/badge/Tested_with-Postman-orange?logo=postman)

![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?logo=postgresql)

![Kafka](https://img.shields.io/badge/Messaging-Kafka-231f20?logo=apachekafka)
![Scheduler](https://img.shields.io/badge/Scheduler-Spring@Scheduled-blue)
![OAuth2](https://img.shields.io/badge/Security-OAuth2-informational)
![WSO2](https://img.shields.io/badge/API_Gateway-WSO2-orange?logo=wso2)

![Backend for Frontend](https://img.shields.io/badge/Pattern-BFF-purple)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-green)
![Logging](https://img.shields.io/badge/Logging-Kafka--based-critical)

![Git](https://img.shields.io/badge/Version_Control-Git-orange?logo=git)
![GitHub](https://img.shields.io/badge/Repo-GitHub-black?logo=github)

---

## Code Style

The code adheres to clean and modular practices, ensuring readability and maintainability. The following conventions were applied:

- **Backend:** Spring Boot (Java 21)
- **Database:** PostgreSQL
- **Messaging:** Kafka
- **Security:** OAuth2
- **Buid Tool:** Maven
- **API Testing:** End-to-end testing and Postman

---

## Installations and How To Run

1. Clone the Project
```console
> git clone https://github.com/OmarAbdelmawgoud5/Virtual-Bank-System.git
> cd Virtual-Bank-System
```

2. Set Up the Database
Use *MySQL* or *PostgreSQL*:

```sql
-- For MySQL
CREATE DATABASE virtualbank;

-- Or PostgreSQL
CREATE DATABASE virtualbank;
```

3. Set your DB credentials in each microservice's application.yml or application.properties file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/virtualbank
spring.datasource.username=root
spring.datasource.password=yourpassword
```

4. Start Kafka and Zookeeper

Start Kafka locally:

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties
```
Create a Kafka topic for logging:

```bash
bin/kafka-topics.sh --create --topic microservice-logs --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

5. Run the Microservices

Go to each service directory and run it:

### Using Maven:

```bash
cd UserService
mvn spring-boot:run
```
Repeat for:
* AccountService
* TransactionService
* BFFService
* LoggingService


6. Setup WSO2 API Gateway

1. Download and install [WSO2 API Manager](https://wso2.com/api-management/).
2. Start the server:

```bash
cd <WSO2_HOME>/bin
./api-manager.sh
```
3. Login to Publisher (https://localhost:9443/publisher) and Dev Portal (https://localhost:9443/devportal) using:
username: admin
password: admin

4. Import or define APIs:

* /register ➝ user-service
* /login, /dashboard, /initiation, /execution ➝ bff-service

5. Create an *API Product* named vbank and include the above APIs.

6. Add *applications* for PORTAL and MOBILE in the dev portal.

7. Set security (OAuth2 + API Key) and set throttling policies.


7. Test with Postman

* Import the OpenAPI specs (or define requests manually).
* Add required headers:

```http
  Authorization: Bearer <access_token>
  APP-NAME: PORTAL or MOBILE
```

## 🧪 Sample Test Flow

1. POST /register – Register a user.
2. POST /login – Login to get the token.
3. GET /dashboard/{userId} – Get dashboard via BFF.
4. POST /transactions/transfer/initiation → followed by execution.

---

## Api Refrences 

<summary>User Service</summary>

- Endpoint: `/api/users/register`
  - Method: POST
  - Description: Registers a new user.

- Endpoint: `/api/users/login`
  - Method: POST
  - Description: Authenticates a user.
 
- Endpoint: `/api/users/{userId}/profile`
  - Method: GET
  - Description: Retrieves a user's basic profile details.

</details>

<summary>Account Service</summary>

- Endpoint: `/api/accounts/transfer`
  - Method: PUT
  - Description: Update account Balance.

- Endpoint: `/api/accounts/{accountId}`
  - Method: GET
  - Description: Retrieves details of a specific bank account.
 
- Endpoint: `/api/accounts`
  - Method: POST
  - Description: Creates a new bank account for a specified user.
 
- Endpoint: `/api/accounts/{accountId}`
  - Method: GET
  - Description: Retrieves details of a specific bank account.
 
- Endpoint: `/api/users/{userId}/accounts`
  - Method: GET
  - Description: Lists all accounts associated with a given user.

</details>

<summary>Transaction Service</summary>

- Endpoint: `/api/transactions/transfer/initiation`
  - Method: POST
  - Description: Initiates a fund transfer between two accounts.

- Endpoint: `/api/transactions/transfer/execution`
  - Method: POST
  - Description: Executes the fund transfer between two accounts.
 
- Endpoint: `/api/accounts/{accountId}/transactions`
  - Method: GET
  - Description: Retrieves the transaction history for a specific account.

</details>

<summary>BFF Service</summary>

- Endpoint: `/api/bff/dashboard/{userId}`
  - Method: GET
  - Description: Fetches user profile, all associated accounts, and recent transactions for each account.

</details>

## 📆 Scheduled Jobs

These jobs run automatically:

* *Account Service*: Marks inactive accounts every 1 hour.
* *Transaction Service*: Credits daily interest at midnight.

## 🧾 Logging

All microservices log requests/responses to *Kafka*, and LoggingService consumes and stores them in a dump table.

## Contribute
We welcome contributions to improve our project!
 
- Fork the repository
- Clone the repository
- Install dependencies
- Create a new branch
- Make your changes
- Commit and push your changes
- Create a pull request
- Wait for your pull request to be reviewed and merged

## License
Copyright 2024 Virtual Bank System
