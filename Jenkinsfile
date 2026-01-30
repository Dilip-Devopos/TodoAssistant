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

        /* ================= QUALITY GATE ================= */
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        /* ================= BUILD STAGES (ONLY IF SONAR PASSES) ================= */
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
            echo "✅ SonarQube passed → Docker images built successfully"
            echo "🔍 SonarQube Dashboard: http://localhost:9000"
        }
        failure {
            echo "❌ Pipeline stopped due to SonarQube Quality Gate failure"
        }
    }
}