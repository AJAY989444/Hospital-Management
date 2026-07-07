# CareFlow HMS - Deployment Guide

This guide details how to host your CareFlow Hospital Management System (HMS) on **Render** using a cloud-hosted MySQL database.

---

## Step 1: Provision a Free Cloud MySQL Database

Since Render's free tier does not include a persistent MySQL database, you can use a free cloud database provider. 

### Option A: Aiven (Recommended)
1. Sign up at [aiven.io](https://aiven.io/).
2. Create a new project and select **MySQL** as the service.
3. Choose the **Free Plan** (available in select regions like AWS `eu-west-1` or `us-east-1`).
4. Once the service is running, find the **Connection Information** panel:
   - **Host**: `mysql-xxx-xxx.aivencloud.com`
   - **Port**: `12345`
   - **User**: `avnadmin`
   - **Password**: *[Generate/Copy password]*
   - **Database Name**: `defaultdb` (or create a new one named `hospital_db`)
5. Construct your JDBC URL:
   ```text
   jdbc:mysql://<HOST>:<PORT>/defaultdb?useSSL=true&requireSSL=true&verifyServerCertificate=false
   ```
   > [!NOTE]
   > Adding `&verifyServerCertificate=false` prevents SSL handshake errors inside the cloud container.

### Option B: Clever Cloud
1. Sign up at [clever-cloud.com](https://www.clever-cloud.com/).
2. Click **Create** > **An add-on** > select **MySQL**.
3. Choose the **Shared / Free Plan** (up to 10MB).
4. Retrieve your connection details:
   - **Host**, **Database Name**, **User**, **Password**, and **Port**.
5. Construct your JDBC URL:
   ```text
   jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>?useSSL=true&verifyServerCertificate=false
   ```

---

## Step 2: Push Your Code to GitHub

1. Initialize git in your local project folder (if not done already):
   ```bash
   git init
   git add .
   git commit -m "Configure for Render cloud deployment"
   ```
2. Create a new repository on GitHub.
3. Push your repository:
   ```bash
   git remote add origin https://github.com/<your-username>/<your-repo-name>.git
   git branch -M main
   git push -u origin main
   ```

---

## Step 3: Deploy on Render

1. Log in to [Render](https://render.com/).
2. Click **New +** > **Web Service**.
3. Connect your GitHub repository.
4. Configure the service settings:
   - **Name**: `careflow-hms`
   - **Environment**: `Docker` (Render will automatically detect your `Dockerfile`)
   - **Instance Type**: `Free`
5. Click **Advanced** to add **Environment Variables**:

| Key | Value | Description |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://<HOST>:<PORT>/<DB_NAME>?useSSL=true&requireSSL=true&verifyServerCertificate=false` | Full cloud database URL |
| `SPRING_DATASOURCE_USERNAME` | `<YOUR_DATABASE_USER>` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `<YOUR_DATABASE_PASSWORD>` | Database password |
| `JWT_SECRET` | *[Generate a secure 256-bit base64 key]* | Secret key for JWT auth |
| `GEMINI_API_KEY` | `<YOUR_GEMINI_API_KEY>` | API key for symptom suggestion & summaries |

6. Click **Deploy Web Service**.

---

## Step 4: Access Your App

Render will build the Docker container and start your Spring Boot application. Once the build completes and the logs display `Started ManagementApplication`, your app will be accessible at:
`https://careflow-hms.onrender.com`
