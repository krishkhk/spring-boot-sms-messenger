# spring-boot-sms-messenger
A comprehensive, containerized web application for sending SMS & WhatsApp messages via the Twilio API. This project features a secure, role-based user system with JWT authentication, dynamic template management, and a full message history log.
---

## 📸 Screenshots

| Login Page | Main Dashboard (Admin View) |
| :---: | :---: |
| https://github.com/krishkhk/spring-boot-sms-messenger/issues/1#issue-3285696098 |

---

## ✨ Key Features

- **User Authentication:** Secure user signup & login with Spring Security and JWT.
- **Role-Based Access Control (RBAC):**
    - **Admin Role:** Full access to manage users and message templates.
    - **User Role:** Simplified view for sending messages only.
- **Twilio Messaging:** Sends SMS and WhatsApp messages via the Twilio API.
- **Template Management:** Admins can create, delete, and search templates with pagination.
- **Message History:** Full, paginated log of all sent messages.
- **Data Security:**
    - Passwords are encrypted using **BCrypt**.
    - User PII (email, mobile) is **masked** before database storage.
- **Centralized Logging & Error Handling:** Uses Spring AOP for action logging and `@ControllerAdvice` for global exception handling.
- **Containerized Deployment:** Includes a multi-stage `Dockerfile` for easy deployment with Podman.

---


## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA
- **Database:** MySQL
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **API:** Twilio
- **Build Tool:** Maven
- **Containerization:** Podman

---

## 🚀 Getting Started (Local Setup)

### Prerequisites

- Java 21 (JDK)
- Apache Maven
- Podman
- A Twilio Account & Phone Number

### Instructions

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd <your-repo-name>
    ```
2.  **Setup your MySQL database:**
    ```sql
    CREATE DATABASE twill;
    ```
3.  **Configure the application:**
    - Open `src/main/resources/application.properties`.
    - Update the `spring.datasource.*` properties with your local MySQL credentials.
    - Update the `twilio.*`, `jwt.secret.key`, and other secret properties.

4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will be available at `http://localhost:8080`.

---

## 🐳 Deployment with Podman

### 1. Update Configuration
Ensure your `application.properties` datasource URL points to the container hostname:
```properties
spring.datasource.url=jdbc:mysql://mysql-db:3306/twill?createDatabaseIfNotExist=true

**2. Build the Application Image**

podman build -t message-sender-app .

**3. Create a Network**
podman network create sms-net

**4. Run the MySQL Container**
podman run -d --name mysql-db --network sms-net -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=twill -p 3306:3306 docker.io/mysql:8.0

**5. Run the Application Container**
podman run -d --name my-app --network sms-net -p 8080:8080 message-sender-app

**6. Verify and Check Logs**
Check running containers: podman ps

Check application logs: podman logs -f my-app

Check database logs: podman logs mysql-db
