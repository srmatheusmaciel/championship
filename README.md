# 🏆 Championship API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![JPA](https://img.shields.io/badge/JPA-Hibernate-blue.svg)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

REST API for managing football championships, including team registration, match results, scores, and attendance. Built with Spring Boot and JPA.

## 📋 Table of Contents

- [Features](#-features)
- [Technologies](#-technologies)
- [Architecture](#-architecture)
- [Installation & Setup](#-installation--setup)
- [API Documentation](#-api-documentation)
- [Usage Examples](#-usage-examples)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Roadmap](#-roadmap)
- [License](#-license)

## ✨ Features

- Team registration and management
- Match results recording and tracking
- Automatic standings calculation
- Championship attendance tracking
- Random match generation for testing
- Comprehensive API endpoints for all operations

## 🛠️ Technologies

- **Java 21**: Core programming language
- **Spring Boot 3.4.4**: Application framework
- **Spring Data JPA**: Database operations and ORM
- **H2 Database**: In-memory database for development
- **SpringDoc OpenAPI**: API documentation
- **Lombok**: Boilerplate code reduction
- **Spring Validation**: Input validation
- **JUnit & Mockito**: Testing frameworks

## 🏛️ Architecture

The project follows a standard Spring Boot architecture with the following components:

```
com.matheusmaciel.championship/
├── config/          # Configuration classes
├── controller/      # REST controllers (Match, Standing, Team)
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities 
├── exception/       # Custom exceptions
├── repository/      # Spring Data repositories
├── service/         # Business logic
└── ChampionshipApplication.java  # Main application class
```

### Domain Model

The application manages the following core entities:
- `Team`: Football teams participating in championships
- `Match`: Records of games between teams with scores
- `Standing`: Current championship positions and statistics

## 🚀 Installation & Setup

### Prerequisites

- Java 21 JDK
- Maven 3.8+
- Git
- JUnit 5
- Swagger

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/srmatheusmaciel/championship.git
   cd championship
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:3031`

### Configuration

The application uses the following properties that can be customized in `application.properties`:

```properties
server.port=3031
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=12345
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create
```

## 📚 API Documentation

The API documentation is automatically generated using SpringDoc OpenAPI. After starting the application, you can access the documentation at:

- **Swagger UI**: `http://localhost:3031/swagger-ui.html`
- **OpenAPI Specification**: `http://localhost:3031/v3/api-docs`

## 🔍 Usage Examples

### Team Management

#### Create a new team:

```bash
curl -X POST http://localhost:3031/teams/register \
  -H "Content-Type: application/json" \
  -d '{
    
    "name": "Fluminense",
    "code": "FLU",
    "state": "RJ",
    "stadium": "Maracanã"
  
  }'
```

#### Get all teams:

```bash
curl -X GET http://localhost:3031/teams
```

#### Get all matches:

```bash
curl -X GET http://localhost:3031/matches
```

### Championship Standings

#### Get current standings:

```bash
curl -X GET http://localhost:3031/standings
```

## 🧪 Testing

The project includes unit tests for controllers, services, and repositories. Run the tests with:

```bash
mvn test
```

To run specific test classes:

```bash
mvn test -Dtest=MatchControllerTest
```

## 📦 Deployment

### Local Deployment

For local development and testing, the embedded Tomcat server is used. The application starts automatically when running:

```bash
mvn spring-boot:run
```

### Production Deployment

For production environments, you can build a JAR file and run it:

```bash
mvn clean package
java -jar target/championship-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

A Dockerfile is provided for containerized deployment:

```dockerfile
FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run the Docker container:

```bash
docker build -t championship-api .
docker run -p 8080:8080 championship-api
```

## 🔮 Roadmap

Future planned enhancements:

- User authentication and authorization with Spring Security
- Get audience ranking
- Create database with docker-compose
- Real-time updates with WebSockets
- Postgre/MySQL database integration for production



Please ensure your code passes all tests and follows the project's coding style.

## 📄 License

This project is licensed under the MIT License

---

Made with ❤️ by [Matheus Maciel](https://github.com/srmatheusmaciel)
