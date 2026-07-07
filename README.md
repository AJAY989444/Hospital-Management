# CareFlow — Hospital Management System (HMS)

CareFlow is a full-stack, production-quality Hospital Management System (HMS) designed for modern clinical booking and smart diagnostics. It is built using Spring Boot 3, Spring Security 6, JWT, Thymeleaf, MySQL, and Google Gemini API (`gemini-2.5-flash`).

🌐 **Live Demo**: [https://hospital-management-lru0.onrender.com](https://hospital-management-lru0.onrender.com)

---

## 🔐 Demo Credentials

Use the following accounts to explore different role dashboards on the live deployment:

| Role | Email | Password | Dashboard |
|------|-------|----------|-----------|
| 👤 **Administrator** | `admin@careflow.com` | `Admin123` | `/admin/dashboard` |
| 🩺 **Doctor** (Neurologist) | `robert.chen@careflow.com` | `Admin123` | `/doctor/dashboard` |
| 🩺 **Doctor** (Cardiologist) | `sarah.jenkins@careflow.com` | `Admin123` | `/doctor/dashboard` |
| 🏥 **Patient** | Register a new account | — | `/patient/dashboard` |

> **Note**: To access the Patient dashboard, click **"Register here"** on the login page and create a new account with the role set to `PATIENT`.

---

## 🚀 Key Architectural Highlights

*   **Role-Based Security (RBAC)**: Secure access routes separating Administrators, Doctors, and Patients via Spring Security.
*   **Dual JWT Authentication**: Token validation supporting both standard REST `Authorization: Bearer <token>` headers (for API clients/mobile apps) and secure `HTTP-only cookies` (`jwt_access_token`) for server-side rendered Thymeleaf views.
*   **Gemini AI Clinical Integrations**:
    *   *AI Symptom Suggester*: Uses Gemini 2.5 Flash in **Structured JSON Mode** to assess symptoms, suggest specialties, determine clinical urgency (HIGH, MEDIUM, LOW), and record results to the `ai_suggestions` audit log table.
    *   *AI Consultation Summarizer*: Summarizes complex medical notes into plain English paragraphs for patient dashboards.
*   **Double-Booking Conflict Check**: A transactional collision algorithm prevents scheduling overlaps for the same doctor, date, and 30-minute time slot.
*   **Soft Deletions (Cascade Deactivation)**: Soft deletes implemented via Hibernate 6 `@SQLDelete` and `@SQLRestriction` to preserve medical logs while deactivating login permissions.

---

## 🛠️ Technology Stack

*   **Backend**: Java 17, Spring Boot 3.3.x (MVC, Data JPA, Security)
*   **Frontend**: Thymeleaf, Vanilla CSS, FontAwesome 6, JavaScript (AJAX)
*   **Database**: MySQL 8.0 with Hibernate ORM
*   **AI Engine**: Google Gemini API (`gemini-2.5-flash`) via standard Java `HttpClient`
*   **Documentation**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui/index.html`)
*   **Containerization**: Multi-stage Docker, Docker Compose

---

## 🌐 Accessing the Live Application

The application is deployed and live on Render:

*   **Web Portal**: [https://hospital-management-lru0.onrender.com](https://hospital-management-lru0.onrender.com)
*   **Swagger API Docs**: [https://hospital-management-lru0.onrender.com/swagger-ui/index.html](https://hospital-management-lru0.onrender.com/swagger-ui/index.html)

> **Note**: The app is hosted on Render's free tier. If it hasn't received traffic recently, the first request may take **~30–60 seconds** to wake up.

---

## 🐳 Run Locally via Docker

To run the entire ecosystem (Spring Boot application + MySQL 8.0 database) using Docker Compose:

1.  **Configure Environment Variables**:
    Set your Gemini API Key in your shell:
    ```bash
    # Windows PowerShell
    $env:GEMINI_API_KEY="your-gemini-api-key-here"

    # Linux / macOS
    export GEMINI_API_KEY="your-gemini-api-key-here"
    ```
    *(If the key is not set, the application uses built-in clinical fallback mock recommendations.)*

2.  **Build & Run Containers**:
    ```bash
    docker-compose up --build
    ```
    This builds the multi-stage Docker image, starts the MySQL database, waits for its health-check to succeed, then runs the application.

3.  **Access Locally**:
    *   Web Portal: [http://localhost:8080/](http://localhost:8080/)
    *   Swagger API Docs: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 💻 Running Locally (Manual Setup)

1.  **Start MySQL Database**:
    Create a database named `hospital_db` and configure a user with username `hms_user` and password `hms_password`.

2.  **Configure `application.properties`**:
    Edit parameters or set environment variables matching your MySQL server and JWT/Gemini configurations:
    ```properties
    SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/hospital_db?allowPublicKeyRetrieval=true&useSSL=false
    SPRING_DATASOURCE_USERNAME=hms_user
    SPRING_DATASOURCE_PASSWORD=hms_password
    GEMINI_API_KEY=your_key_here
    JWT_SECRET=your_base64_secret_here
    ```

3.  **Seed the Database** (optional):
    Run `seed.sql` against your local MySQL instance to populate sample doctors and admin accounts:
    ```bash
    mysql -u hms_user -p hospital_db < seed.sql
    ```

4.  **Compile & Run**:
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
