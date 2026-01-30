pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
        SONAR_HOST_URL = "http://host.docker.internal:9000"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        stage('SonarQube Scan - Backend') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_TOKEN')]) {
                        bat """
                            docker run --rm ^
                              -v %CD%:/usr/src ^
                              -e SONAR_HOST_URL=%SONAR_HOST_URL% ^
                              -e SONAR_LOGIN=%SONAR_TOKEN% ^
                              sonarsource/sonar-scanner-cli ^
                              -Dsonar.projectKey=TodoAssistantBackend ^
                              -Dsonar.projectName=TodoAssistant Backend ^
                              -Dsonar.sources=. ^
                              -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    bat 'docker build -t %BACKEND_IMAGE%:%TAG% .'
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('Frontend/todo') {
                    bat 'docker build -t %FRONTEND_IMAGE%:%TAG% .'
                }
            }
        }

        stage('Build Database Image') {
            steps {
                dir('Database') {
                    bat 'docker build -t %DB_IMAGE%:%TAG% .'
                }
            }
        }
    }

    post {
        success {
            echo "✅ SonarQube scan & Docker images built successfully"
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}