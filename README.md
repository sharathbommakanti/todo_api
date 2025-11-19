# Todo API - Spring Boot REST API

A comprehensive Spring Boot REST API for Todo Management with OAuth 2.0 authentication, JPA, and full CRUD operations.

## Features

- **Spring Boot 3.2** with Java 17
- **JPA/Hibernate** for database operations
- **OAuth 2.0** with JWT token authentication
- **RESTful API** with proper HTTP methods
- **Service Layer** with business logic
- **Repository Layer** with Spring Data JPA
- **DTO Pattern** for data transfer
- **Exception Handling** with global exception handler
- **Validation** with Bean Validation
- **CORS Configuration** for frontend integration
- **H2 Database** (can be switched to PostgreSQL/MySQL)
- **Security** with Spring Security

## Tech Stack

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** with OAuth 2.0 Resource Server
- **JWT** (JSON Web Tokens)
- **H2 Database** (In-memory, can be changed to PostgreSQL/MySQL)
- **Lombok** for reducing boilerplate
- **Maven** for dependency management

## Project Structure

```
todo-api/
├── src/
│   ├── main/
│   │   ├── java/com/todo/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── security/        # Security configuration
│   │   │   └── service/         # Business logic layer
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## Getting Started

### 1. Clone and Navigate

```bash
cd todo-api
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

Or run the `TodoApiApplication` class from your IDE.

The API will be available at: `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "userId": 1,
    "username": "john_doe",
    "email": "john_doe@example.com"
  }
}
```

### Todo Endpoints

All todo endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-token>
```

#### Get All Todos
```http
GET /api/todos
Authorization: Bearer <token>
```

#### Get Todos by Status
```http
GET /api/todos?completed=true
GET /api/todos?completed=false
Authorization: Bearer <token>
```

#### Get Todo by ID
```http
GET /api/todos/{id}
Authorization: Bearer <token>
```

#### Create Todo
```http
POST /api/todos
Authorization: Bearer <token>
Content-Type: application/json

{
  "text": "Complete the project",
  "completed": false
}
```

#### Update Todo
```http
PUT /api/todos/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "text": "Complete the project - Updated",
  "completed": true
}
```

#### Delete Todo
```http
DELETE /api/todos/{id}
Authorization: Bearer <token>
```

#### Delete All Completed Todos
```http
DELETE /api/todos/completed
Authorization: Bearer <token>
```

#### Get Completed Todos Count
```http
GET /api/todos/stats/completed-count
Authorization: Bearer <token>
```

### User Endpoints

#### Register User (Alternative)
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer <token>
```

#### Update User
```http
PUT /api/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "email": "newemail@example.com",
  "password": "newpassword123"
}
```

#### Delete User
```http
DELETE /api/users/{id}
Authorization: Bearer <token>
```

## Database

### H2 Console (Development)

Access H2 Console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:tododb`
- Username: `sa`
- Password: (empty)

### Switch to PostgreSQL

1. Uncomment PostgreSQL dependency in `pom.xml`
2. Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tododb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Configuration

### JWT Configuration

Edit `application.properties` to change JWT settings:

```properties
jwt.secret=your-secret-key-minimum-256-bits
jwt.expiration=86400000  # 24 hours in milliseconds
```

### CORS Configuration

Update allowed origins in `application.properties`:

```properties
cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

Or modify `SecurityConfig.java` for more complex CORS rules.

## Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'
```

### Create Todo (replace TOKEN with actual token)
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"text":"My first todo","completed":false}'
```

### Get All Todos
```bash
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer TOKEN"
```

## Security Features

- **JWT-based Authentication**: Stateless authentication using JSON Web Tokens
- **Password Encryption**: BCrypt password hashing
- **Role-based Access**: User roles (ROLE_USER, ROLE_ADMIN)
- **CORS Support**: Configured for frontend integration
- **Input Validation**: Bean Validation on DTOs

## Error Handling

The API uses a global exception handler that returns consistent error responses:

```json
{
  "success": false,
  "message": "Error message",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

## Integration with Frontend Apps

This API is designed to work with:
- React Web App (`react-todo-app`)
- React Native Mobile App (`react-todo-mobile`)

Update the frontend apps to use these endpoints instead of localStorage.

## Building for Production

1. Update database configuration for production
2. Change JWT secret to a secure value
3. Configure CORS for production domains
4. Build the JAR:

```bash
mvn clean package
```

5. Run the JAR:

```bash
java -jar target/todo-api-1.0.0.jar
```

## License

MIT

