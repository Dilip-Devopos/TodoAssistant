pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        /* ================= BACKEND SONAR ================= */
        stage('SonarQube - Backend') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    withSonarQubeEnv('SonarQube') {
                        bat '''
                        sonar-scanner ^
                        -Dsonar.projectKey=todo-backend ^
                        -Dsonar.projectName=Todo Backend ^
                        -Dsonar.sources=. ^
                        -Dsonar.java.binaries=target
                        '''
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

        /* ================= FRONTEND SONAR ================= */
        stage('SonarQube - Frontend') {
            steps {
                dir('Frontend/todo') {
                    withSonarQubeEnv('SonarQube') {
                        bat '''
                        sonar-scanner ^
                        -Dsonar.projectKey=todo-frontend ^
                        -Dsonar.projectName=Todo Frontend ^
                        -Dsonar.sources=. ^
                        -Dsonar.exclusions=node_modules/**
                        '''
                    }
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
            echo "✅ SonarQube analysis + Docker images built successfully"
            echo "🔍 Check reports at http://localhost:9000"
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}