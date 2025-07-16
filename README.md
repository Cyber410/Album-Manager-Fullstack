# 📷 Album Manager – Full Stack Web App

This project is a full-featured photo album management application built with a Spring Boot backend and a React frontend. It allows users to sign up, log in securely, and perform complete CRUD operations on albums and photos.

---

## 🧰 Tech Stack

### 🔒 Backend (AlbumApi/)
- **Spring Boot** – REST API development
- **Spring Security** – OAuth2 & JWT-based authentication
- **Swagger UI** – API documentation and testing
- **JPA/Hibernate** – Database interaction
- **MySQL / H2** – Relational database

### 🎨 Frontend (Frontend/)
- **React** – Component-based UI
- **Axios** – API communication
- **React Router** – Navigation
- **Bootstrap / CSS Modules** – Styling

---

## ✨ Features

- 🔐 User Authentication using **OAuth2 and JWT**
- 📁 Create, update, and delete **Albums**
- 🖼️ Upload, manage, and delete **Photos**
- 🔎 View and browse albums/photos per user
- 📃 Fully documented REST APIs with **Swagger UI**
- 🌍 Clean separation of frontend and backend (monorepo-style)
- 📦 Ready for deployment

---

## 🛠 Run Locally

### 🚀 Backend:
```bash
cd AlbumApi
./mvnw spring-boot:run
