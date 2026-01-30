pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
    }

    stages {

        // --------------------------------------
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        // --------------------------------------
        stage('SonarQube Analysis - Backend') {
            steps {
                withSonarQubeEnv('SonarQube') { // Must match Jenkins Configure System
                    dir('Backend/todo-summary-assistant') {
                        bat '''
                            sonar-scanner ^
                              -Dsonar.projectKey=TodoAssistantBackend ^
                              -Dsonar.projectName=TodoAssistant Backend ^
                              -Dsonar.sources=. ^
                              -Dsonar.language=java ^
                              -Dsonar.java.binaries=target/classes
                        '''
                    }
                }
            }
        }

        // Optional: Quality Gate check for Backend
        stage('Quality Gate - Backend') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // --------------------------------------
        stage('SonarQube Analysis - Frontend') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    dir('Frontend/todo') {
                        bat '''
                            sonar-scanner ^
                              -Dsonar.projectKey=TodoAssistantFrontend ^
                              -Dsonar.projectName=TodoAssistant Frontend ^
                              -Dsonar.sources=.
                        '''
                    }
                }
            }
        }

        stage('Quality Gate - Frontend') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // --------------------------------------
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

    } // stages

    post {
        success {
            echo "✅ Backend, Frontend & Database Docker images built successfully"
            bat 'docker images | findstr todosummary'
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}