# CareFlow — Hospital Management System (HMS)

CareFlow is a full-stack, production-quality Hospital Management System (HMS) designed for modern clinical booking and smart diagnostics. It is built using Spring Boot 3, Spring Security 6, JWT, Thymeleaf, MySQL, and Google Gemini API (gemini-1.5-flash).

---

## 🚀 Key Architectural Highlights

*   **Role-Based Security (RBAC)**: Secure access routes separating Administrators, Doctors, and Patients via Spring Security.
*   **Dual JWT Authentication**: Token validation supporting both standard REST `Authorization: Bearer <token>` headers (for API clients/mobile apps) and secure `HTTP-only cookies` (`jwt_access_token`) for server-side rendered Thymeleaf views.
*   **Gemini AI clinical Integrations**:
    *   *AI Symptom Suggester*: Uses Gemini 1.5 Flash in **Structured JSON Mode** (enforced by JSON schema generation configs) to assess symptoms, suggest specialties, determine clinical urgency (HIGH, MEDIUM, LOW), and record results to the `ai_suggestions` audit log table.
    *   *AI Consultation Summarizer*: Summarizes complex medical notes into plain English paragraphs for patient dashboards.
*   **Double-Booking Conflict Check**: A transactional collision algorithm prevents scheduling overlaps for the same doctor, date, and 30-minute time slot.
*   **Soft Deletions (Cascade Deactivation)**: Soft deletes implemented via Hibernate 6 `@SQLDelete` and `@SQLRestriction` to preserve medical logs while deactivating login permissions.

---

## 🛠️ Technology Stack

*   **Backend**: Java 17, Spring Boot 3.3.x (MVC, Data JPA, Security)
*   **Frontend**: Thymeleaf, Vanilla CSS, FontAwesome 6, JavaScript (AJAX)
*   **Database**: MySQL 8.0 with Hibernate ORM
*   **AI Engine**: Google Gemini API (`gemini-1.5-flash`) via standard Java `HttpClient`
*   **Documentation**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui/index.html`)
*   **Containerization**: Multi-stage Docker, Docker Compose

---

## 🐳 Quick Start: Run via Docker

To run the entire ecosystem (Spring Boot application + MySQL 8.0 database) using Docker Compose, execute these steps:

1.  **Configure Environment Variables**:
    Set your Gemini API Key in your local shell profile:
    ```bash
    # Windows PowerShell
    $env:GEMINI_API_KEY="your-gemini-api-key-here"

    # Linux / macOS
    export GEMINI_API_KEY="your-gemini-api-key-here"
    ```
    *(Note: If the key is not set, the application will run using built-in clinical fallback mock recommendations for safety).*

2.  **Build & Run Containers**:
    Run Docker Compose from the project root:
    ```bash
    docker-compose up --build
    ```
    This builds the multi-stage Docker image, starts the MySQL database, waits for its health-check ping to succeed, and runs the application.

3.  **Access Portals**:
    *   Web Portal: [http://localhost:8080/](http://localhost:8080/)
    *   Swagger API Docs: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 💻 Running Locally (Manual Setup)

1.  **Start MySQL Database**:
    Create a database named `hospital_db` and configure a user with the username `hms_user` and password `hms_password`.

2.  **Configure `application.properties`**:
    Edit parameters or set variables matching your MySQL server and JWT/Gemini configurations:
    ```properties
    SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/hospital_db?allowPublicKeyRetrieval=true&useSSL=false
    SPRING_DATASOURCE_USERNAME=hms_user
    SPRING_DATASOURCE_PASSWORD=hms_password
    GEMINI_API_KEY=your_key_here
    ```

3.  **Compile & Run**:
    Execute using Maven:
    ```bash
    mvn spring-boot:run
    ```

---

## 📖 API Endpoints Summary

### 1. Authentication (`/api/auth`)
*   `POST /api/auth/register` — Register a new patient or doctor.
*   `POST /api/auth/login` — Authenticate credentials. Returns tokens and sets the HTTP-only cookie.
*   `POST /api/auth/refresh` — Rotate access token.
*   `POST /api/auth/logout` — Clear auth tokens.

### 2. Patient Domain (`/api/patient`)
*   `GET /api/patient/appointments` — List personal booking history.
*   `POST /api/patient/appointments` — Schedule a new appointment.
*   `GET /api/patient/doctors` — List active doctor profiles, optional filtering by specialty.

### 3. Doctor Domain (`/api/doctor`)
*   `GET /api/doctor/appointments` — View assigned patient bookings.
*   `PUT /api/doctor/appointments/{id}/status` — Update booking status and consultation notes.

### 4. Admin Domain (`/api/admin`)
*   `GET /api/admin/doctors` / `PUT /api/admin/doctors/{id}` / `DELETE /api/admin/doctors/{id}` — Manage doctor profiles.
*   `GET /api/admin/patients` / `PUT /api/admin/patients/{id}` / `DELETE /api/admin/patients/{id}` — Manage patient profiles.
*   `GET /api/admin/appointments` — View global appointments.
*   `GET /api/admin/analytics` — Fetch cancellation rates and busiest specialty.
*   `GET /api/admin/ai-suggestions` — Fetch symptom analyzer logs.

### 5. Gemini AI Domain (`/api/ai`)
*   `POST /api/ai/suggest-doctor` — Assess clinical symptoms.
*   `POST /api/ai/summarize-appointment/{id}` — Generate plain-English summary of notes.
