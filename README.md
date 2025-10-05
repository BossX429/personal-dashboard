# Personal Dashboard - Full Stack Architecture

A modern, enterprise-grade personal dashboard built with **Node.js frontend** and **Java Spring Boot microservices**, deployed on **Azure Cloud**.

## 🏗️ **Architecture Overview**

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│   Node.js Web   │ ◄──────────────► │ Java Spring API │
│   Frontend      │                 │   Microservice  │
│  (Port 3000)    │                 │   (Port 8080)   │
└─────────────────┘                 └─────────────────┘
         │                                   │
         │                                   │
         ▼                                   ▼
┌─────────────────┐                 ┌─────────────────┐
│  Azure App      │                 │ PostgreSQL DB   │
│   Service       │                 │   Database      │
│   (Web Tier)    │                 │  (Data Tier)    │
└─────────────────┘                 └─────────────────┘
```

## 🚀 **Tech Stack**

### **Frontend (Node.js)**
- **Express.js** - Web server and API routing
- **Vanilla JavaScript** - Interactive dashboard UI
- **CSS3** - Modern responsive design
- **Real-time Updates** - Live system monitoring

### **Backend (Java Spring Boot)**
- **Spring Boot 3.1.5** - Enterprise application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Actuator** - Health checks and metrics
- **PostgreSQL** - Production database

### **DevOps & Deployment**
- **Docker** - Containerization
- **Azure Container Registry** - Container image storage
- **Azure App Service** - Hosting platform
- **GitHub Actions** - CI/CD pipeline
- **Azure Bicep** - Infrastructure as Code
