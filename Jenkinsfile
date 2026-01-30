pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
    }

    tools {
        sonarQube 'sonar-scanner'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        stage('Static Code Analysis') {
            environment {
                SONAR_URL = "http://localhost:9000"
            }
            steps {
                withCredentials([string(credentialsId: 'SonarQube', variable: 'sonar-token')]) {
                    sh '''
                        cd Backend/todo-summary-assistant
                        sonar-scanner -Dsonar.projectKey=Guvi-Project-1-prod -Dsonar.sources=. -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${sonar-token}
                    '''
                }
            }    
        }

        stage('Build Backend Image') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    bat '''
                        docker build -t %BACKEND_IMAGE%:%TAG% .
                    '''
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('Frontend/todo') {
                    bat '''
                        docker build -t %FRONTEND_IMAGE%:%TAG% .
                    '''
                }
            }
        }

        stage('Build Database Image') {
            steps {
                dir('Database') {
                    bat '''
                        docker build -t %DB_IMAGE%:%TAG% .
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Sonar scan + Docker images built successfully"
            bat 'docker images | findstr todosummary'
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}