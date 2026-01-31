# 📘 TodoAssistant

A full-stack TODO application with complete DevOps pipeline and Kubernetes deployment support using Helm.

## 🌟 Overview

TodoAssistant is a production-ready TODO management application that demonstrates modern DevOps practices and cloud-native deployment strategies. It combines a React frontend, Spring Boot backend, and MySQL database with comprehensive CI/CD automation and Kubernetes orchestration.

### Key Features

- **Full-Stack Application**: React frontend with Spring Boot REST API backend
- **Database Integration**: MySQL for persistent data storage
- **Containerization**: Docker and Docker Compose support for local development
- **CI/CD Pipeline**: Jenkins-based automation for building, testing, and deployment
- **Kubernetes Ready**: Helm charts for scalable cloud deployment
- **GitOps Workflow**: ArgoCD integration for continuous deployment

## 🏗️ Architecture

```
TodoAssistant/
├── Backend/todo-summary-assistant/    # Spring Boot REST API
├── Frontend/todo/                     # React application
├── Database/                          # MySQL schema and scripts
├── Jenkinsfile                        # CI/CD pipeline definition
├── docker-compose.yml                 # Local development setup
├── .env                              # Environment configuration
└── README.md                         # Project documentation
```

### Technology Stack

**Frontend:**
- React.js
- Modern JavaScript (ES6+)
- RESTful API integration

**Backend:**
- Java 17+
- Spring Boot
- Maven for dependency management
- RESTful API endpoints

**Database:**
- MySQL (relational database)
- Schema scripts for initialization

**DevOps:**
- Docker & Docker Compose
- Jenkins CI/CD
- Helm 3+ (Kubernetes package manager)
- ArgoCD (GitOps continuous delivery)

## 🚀 Getting Started

### Prerequisites

Before running the application, ensure you have the following installed:

- **Java**: Version 17 or higher
- **Maven**: For building the backend
- **Node.js**: For the frontend application
- **Docker**: Latest version
- **Docker Compose**: For orchestrating multi-container setup
- **Kubernetes Cluster**: For production deployment (e.g., Rancher Desktop, Minikube, or cloud provider)
- **Helm**: Version 3 or higher
- **Git**: For version control

### Local Development with Docker Compose

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Dilip-Devopos/TodoAssistant.git
   cd TodoAssistant
   ```

2. **Configure Environment Variables**
   
   Create or update the `.env` file with your configuration:
   ```bash
   cp .env.sample .env
   # Edit .env file with your specific settings
   ```

3. **Start the Application**
   ```bash
   docker compose up -d
   ```

4. **Access the Application**
   - **Frontend**: http://localhost:3000
   - **Backend API**: Check the docker-compose.yml for the backend port
   - **Database**: MySQL running on configured port

5. **Stop the Application**
   ```bash
   docker compose down
   ```

## 📦 Components

### Backend (todo-summary-assistant)

The backend is a Spring Boot application that provides:
- RESTful API endpoints for TODO operations
- CRUD (Create, Read, Update, Delete) functionality
- Database connectivity and ORM
- Business logic and data validation
- Environment-based configuration

**Key Endpoints:**
- `GET /api/todos` - List all todos
- `POST /api/todos` - Create a new todo
- `PUT /api/todos/{id}` - Update a todo
- `DELETE /api/todos/{id}` - Delete a todo

### Frontend (todo)

A modern React application featuring:
- Interactive TODO list interface
- Real-time updates
- Responsive design
- API integration with the backend
- Environment-based API URL configuration

### Database

MySQL database with:
- Schema initialization scripts
- Data persistence configuration
- Volume mounting for data retention
- Environment-based connection settings

## 🔄 CI/CD Pipeline

The Jenkinsfile defines a comprehensive CI/CD pipeline with the following stages:

1. **Checkout Code**: Pull the latest code from the repository
2. **Build Backend**: Compile Java application using Maven
3. **Build Frontend**: Build React application
4. **Docker Image Build**: Create container images for both services
5. **Push to Registry**: Upload images to Docker Hub or GitHub Container Registry
6. **Update Tag Dynamically**: Update deployment manifests with new image tags
7. **Deploy with ArgoCD**: Trigger deployment using Helm charts and ArgoCD

### Pipeline Configuration

The Jenkins pipeline automates the entire deployment workflow, ensuring:
- Consistent builds across environments
- Automated testing and validation
- Version-controlled deployments
- Rollback capabilities
- Integration with container registries

## ☸️ Kubernetes Deployment with Helm

### Prerequisites for Kubernetes Deployment

- Kubernetes cluster (Rancher Desktop)
- Helm 3+ CLI installed
- Docker registry access (Docker Hub, GitHub Container Registry)
- Kubernetes namespace created (e.g., `todo-app`)
- kubectl configured to access your cluster

### Helm Chart Deployment

1. **Create Namespace**
   ```bash
   kubectl create namespace todo-app
   ```

2. **Install Helm Chart**
   ```bash
   helm install todoassistant ./helm-chart \
     --namespace todo-app \
     --set image.tag=latest \
     --set database.password=yourpassword
   ```

3. **Verify Deployment**
   ```bash
   kubectl get pods -n todo-app
   kubectl get services -n todo-app
   ```

4. **Upgrade Deployment**
   ```bash
   helm upgrade todoassistant ./helm-chart \
     --namespace todo-app \
     --set image.tag=v1.2.0
   ```

5. **Uninstall**
   ```bash
   helm uninstall todoassistant --namespace todo-app
   ```

### ArgoCD Integration

The project supports GitOps workflows through ArgoCD:
- Automatic synchronization of Kubernetes resources
- Declarative deployment management
- Rollback and versioning capabilities
- Health monitoring and status tracking

## 🛠️ Development

### Building from Source

**Backend:**
```bash
cd Backend/todo-summary-assistant
mvn clean install
mvn spring-boot:run
```

**Frontend:**
```bash
cd Frontend/todo
npm install
npm start
```

### Running Tests

**Backend Tests:**
```bash
cd Backend/todo-summary-assistant
mvn test
```

**Frontend Tests:**
```bash
cd Frontend/todo
npm test
```

## 📝 Configuration

### Environment Variables

Key environment variables used in the application:

- `SPRING_DATASOURCE_URL`: MySQL host address
- `DATABASE_PORT`: MySQL port (default: 3306)
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

### Docker Compose Configuration

The `docker-compose.yml` file orchestrates:
- Frontend service
- Backend service
- MySQL database service
- Network configuration for inter-service communication
- Volume mounting for data persistence

## 🔒 Security Considerations

- Environment variables for sensitive data (passwords, API keys)
- Network isolation using Docker networks
- Database credentials management
- HTTPS/TLS for production deployments
- Regular security updates and dependency scanning

## 📊 Monitoring and Logging

Consider implementing:
- Application logging (Logback/SLF4J for backend)
- Performance monitoring (Prometheus, Grafana)
- Kubernetes liveness and readiness probes

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 🐛 Troubleshooting

### Common Issues

**Docker Compose fails to start:**
- Ensure Docker is running
- Check port conflicts (3000, 3306, etc.)
- Verify `.env` file exists and is configured correctly

**Database connection errors:**
- Confirm MySQL container is running: `docker ps`
- Check database credentials in `.env`
- Verify network connectivity between services

**Frontend cannot reach backend:**
- Ensure `BACKEND_API_URL` is correctly set
- Check if backend service is running
- Verify CORS configuration in Spring Boot

**Kubernetes deployment issues:**
- Verify kubectl context: `kubectl config current-context`
- Check namespace exists: `kubectl get namespaces`
- Review pod logs: `kubectl logs <pod-name> -n todo-app`
- Check Helm release status: `helm list -n todo-app`

## 📞 Support

For issues, questions, or contributions:
- Open an issue on GitHub
- Review existing documentation
- Check the Troubleshooting section

## 🎯 Roadmap

Future enhancements may include:
- User authentication and authorization
- Advanced filtering and search
- Data export/import functionality
- Mobile application
- Real-time collaboration features
- Performance optimizations
- Enhanced monitoring and observability

### Screenchort 
localhost:3000 

<img width="1920" height="932" alt="image" src="https://github.com/user-attachments/assets/d38d0dd0-4819-4572-99dc-d7e06ea8dcdc" />

<img width="1920" height="741" alt="image" src="https://github.com/user-attachments/assets/c70689a4-5165-4e13-aada-357970d93a0c" />

<img width="1915" height="812" alt="image" src="https://github.com/user-attachments/assets/024bba6a-b878-4073-bbb1-7533ea8ebb15" />

<img width="1920" height="554" alt="image" src="https://github.com/user-attachments/assets/2bdfd540-885c-4ef2-9f66-f4af04cd940a" />

<img width="1920" height="820" alt="image" src="https://github.com/user-attachments/assets/fc465115-1f25-4a11-a30e-16059b47e55f" />

### Kubernetes screenshort

<img width="1917" height="993" alt="image" src="https://github.com/user-attachments/assets/cccc3df2-bd6e-4232-b59b-171395090cce" />

<img width="1916" height="812" alt="image" src="https://github.com/user-attachments/assets/b6e84900-c59d-4253-85a8-be6913cc88a0" />

<img width="1920" height="799" alt="image" src="https://github.com/user-attachments/assets/a001f9ca-f990-4c2d-a04b-cbded342fa4f" />

### Slack 

<img width="1920" height="851" alt="image" src="https://github.com/user-attachments/assets/d4b7bd2e-8c13-4664-82e5-af92ac6a1428" />

### Jenkins

<img width="1920" height="997" alt="image" src="https://github.com/user-attachments/assets/700f2626-f356-4650-951c-0bdc034fdd54" />

### Argocd

<img width="1920" height="896" alt="image" src="https://github.com/user-attachments/assets/4a5d81a1-cd8e-4c5b-aadb-cd0f74edac28" />

### Dockerhub

<img width="1920" height="955" alt="image" src="https://github.com/user-attachments/assets/0a1ba2ac-54ad-4e7e-91b1-cc85010f1c57" />












