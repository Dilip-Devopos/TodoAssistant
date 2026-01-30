pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
    }

    tools {
        sonarScanner 'SonarScanner' // must match name in Jenkins Global Tool Configuration
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') { // must match the name in Configure System
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
            echo "✅ Backend, Frontend & Database Docker images built successfully"
            bat 'docker images | findstr todosummary'
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}