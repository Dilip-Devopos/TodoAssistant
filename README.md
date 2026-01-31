📘 TodoAssistant + Helm Documentation
🛠 Project Overview

TodoAssistant is a full-stack TODO application with a complete DevOps pipeline and Kubernetes deployment support using Helm.

It includes:
Backend API
Frontend UI
Database schema
DevOps automation (CI/CD)
Helm chart for Kubernetes deployment

🗂 Repository Structure

Main App — TodoAssistant
TodoAssistant/
├── Backend/todo-summary-assistant
├── Frontend/todo
├── Database/
├── Jenkinsfile
├── docker-compose.yml
├── .env
├── README.md

Architecture Summary:

Frontend
Built with modern JavaScript (React)

Backend
Java application (Spring Boot)

Database
MySQL  relational database

DevOps
Docker + Docker Compose
Jenkins pipeline for CI/CD

Kubernetes
Helm chart supports automated deployments

📄 1. TodoAssistant — Application Documentation
🧾 Requirements

Java 17+
Maven
Node.js (frontend)
Docker & Docker-Compose
Kubernetes cluster (production or testing)
Helm 3+ (Kubernetes deployment)

 Running Locally with Docker-Compose
Clone the repo:
git clone https://github.com/Dilip-Devopos/TodoAssistant.git
cd TodoAssistant

Create a .env (environment variables) file:
cp .env.sample .env

Start services:
docker compose up -d

Access the app (default local ports):
Frontend: http://localhost:3000

Backend — todo-summary-assistant:

Spring Boot REST API application
Handles TODO list CRUD operations
Connects todatabase
Configured via .env file values

Frontend — todo:

Built in React
UI for managing todos
Connects to backend using REST
Configurable base API URL via environment file

📦 Jenkins CI/CD
The Jenkinsfile defines the pipeline:

Typical stages:

Checkout code
Build Backend (Maven)
Build Frontend
Docker Image Build
Push to Registry
Upadte Tag Dynamically
Deploy use Argocd with helm chart

📦 2. Deploy with Kubernetes & Helm

Prerequisites:

Kubernetes cluster (Rancker Desktop)
Helm 3+ CLI
Docker registry (Docker Hub, Github)
Kubernetes namespace (todo-app)
