## 🚀 Spring Boot Single-User Task API

### Overview (Refinement)

This is a secure, lightweight **Spring Boot RESTful API** designed exclusively for **single-user task management**. It handles authentication via **JWT (JSON Web Tokens)** and provides **CRUD operations** for tasks. The application currently uses an **H2 in-memory database** (data resets on restart) and is run using the Maven build tool.

### Key Features (Additions)

  * **Technology Stack:** Java, Spring Boot, Maven.
  * **Authentication:** Secured using **JWT-based authentication** (tokens required for all `/api/tasks` endpoints).
  * **Authorization:** Strict **ownership enforcement** ensures users can only access their own tasks.
  * **Database:** **H2 in-memory database** is used for development/MVP.
  * **Core Entities:** Manages `User` and `Task` entities with fields like `title`, `description`, and `status` (`PENDING`/`COMPLETED`).

-----

## 🛠️ Getting Started

### Prerequisites

You will need the following installed on your machine:

  * **Java Development Kit (JDK) 17+**
  * **Apache Maven 3.8+**

### Running the Application

This is the fastest way to start the application using the Maven wrapper:

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/tapschikuniben/taskmanager-backend.git
    cd [your-project-directory]
    ```
2.  **Run the App:**
    Use the Spring Boot Maven plugin goal to compile and run the application.
    ```bash
    mvn spring-boot:run
    ```
    The application will start on the default port, usually `http://localhost:8080`.

### Database Access (Crucial Addition)

For development and debugging, the **H2 Console** is exposed:

  * **URL:** `http://localhost:8080/h2-console`
  * **Note:** You will need to check your `application.properties` for the exact JDBC URL to use for login.

-----

## 💻 API Endpoints (Detailed and Corrected)

### 1\. Authentication (Public)

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Registers a new user. | `{ "username": "string", "password": "string" }` |
| `POST` | `/auth/login` | Authenticates a user and returns a **JWT**. | `{ "username": "string", "password": "string" }` |

### 2\. Task Management (Protected)

**All endpoints below require a valid JWT in the `Authorization` header:** `Authorization: Bearer <token>`

| Method | Endpoint | Description | Request Body Example |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/tasks` | Creates a new task for the logged-in user. | `{ "title": "string", "description": "string", "status": "PENDING" }` |
| `GET` | `/api/tasks` | Retrieves **all tasks** for the logged-in user. | *(None)* |
| `GET` | `/api/tasks/{id}` | Retrieves a single task by ID (must verify ownership). | *(None)* |
| `PUT` | `/api/tasks/{id}` | Updates an existing task. | `{ "title": "string", "description": "string", "status": "COMPLETED" }` |
| `DELETE` | `/api/tasks/{id}` | Deletes the task (204 No Content on success). | *(None)* |

### Error Codes (Informative Addition)

The API uses standard HTTP response codes for clear error communication:

  * **`400 Bad Request`**: Invalid input data (e.g., missing required field).
  * **`401 Unauthorized`**: Missing or invalid JWT token.
  * **`403 Forbidden`**: Attempt to access another user's task.
  * **`404 Not Found`**: Resource (task) does not exist.

-----

## 📦 Packaging and Deployment

To create a runnable JAR file for production or external deployment:

1.  **Package the application:**
    ```bash
    mvn clean package
    ```
2.  **Run the JAR:**
    The executable JAR will be located in the `target/` folder.
    ```bash
    java -jar target/[your-app-name].jar
    ```
