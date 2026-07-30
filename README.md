# 🚆 QR Code Based Train Ticket Booking System

A **Full Stack Java Web Application** built using **Spring Boot** for the backend and **HTML, CSS, and JavaScript** for the frontend. The application enables users to book train tickets online, generates a unique QR code for each booking, and provides downloadable PDF tickets.

---

## 🚀 Features

- 👤 User Registration & Authentication
- 🚆 Search Available Trains
- 🎫 Book Train Tickets
- ❌ Cancel Booked Tickets
- 📱 QR Code Generation for Every Ticket
- 📄 Download Tickets as PDF
- 🗄️ Database Integration using JPA (Hibernate)
- 🌐 RESTful API Architecture
- 🏗️ Layered Architecture (Controller, Service, Repository)

---

## 🛠️ Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Data JPA (Hibernate)
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript

### Database
- MySQL (Development)
- TiDB Cloud (Production)

### Libraries
- ZXing (QR Code Generation)
- OpenPDF (PDF Generation)

---

## ⚙️ Tools & Technologies

- IntelliJ IDEA
- Postman
- Git & GitHub
- Maven

---

## 📂 Project Structure

```
src
├── controller
├── dto
├── model
├── repository
├── service
├── resources
│   ├── static
│   │   ├── html
│   │   ├── css
│   │   └── js
│   └── application.properties
```

---

## 🔐 Environment Variables

Create a `.env` file in the project root with the following variables:

```env
DB_URL=your_database_url
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
PORT=8080
```

---

## ▶️ Running the Project

1. Clone the repository.
2. Create a `.env` file with your database credentials.
3. Configure your TiDB Cloud or MySQL database.
4. Run the project using:

```bash
mvn spring-boot:run
```

5. Open your browser and visit:

```
http://localhost:8080
```

---

## 📌 Future Enhancements

- Email Ticket Confirmation
- Seat Selection
- Payment Gateway Integration
- Admin Dashboard
- Booking History
- Responsive UI Improvements

---

## 👨‍💻 Author

**Afeez S**

Java Developer | Software Developer | Spring Boot | REST APIs | MySQL | TiDB Cloud
