pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
        SONAR_URL      = "http://localhost:9000"
    }

    tools {
        sonarScanner 'sonar-scanner'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        stage('Static Code Analysis - SonarQube') {
            steps {
                withCredentials([string(credentialsId: 'SonarQube', variable: 'SONAR_TOKEN')]) {
                    dir('Backend/todo-summary-assistant') {
                        bat """
                            sonar-scanner ^
                            -Dsonar.projectKey=Guvi-Project-1-prod ^
                            -Dsonar.sources=. ^
                            -Dsonar.host.url=%SONAR_URL% ^
                            -Dsonar.login=%SONAR_TOKEN%
                        """
                    }
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    bat """
                        docker build -t %BACKEND_IMAGE%:%TAG% .
                    """
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('Frontend/todo') {
                    bat """
                        docker build -t %FRONTEND_IMAGE%:%TAG% .
                    """
                }
            }
        }

        stage('Build Database Docker Image') {
            steps {
                dir('Database') {
                    bat """
                        docker build -t %DB_IMAGE%:%TAG% .
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✅ SonarQube scan completed and Docker images built successfully"
            bat "docker images | findstr todosummary"
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}