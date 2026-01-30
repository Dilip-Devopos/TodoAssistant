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

        stage('Trivy Image Scan') {
            steps {
                bat '''
                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      aquasec/trivy:latest image ^
                      --exit-code 1 ^
                      --severity HIGH,CRITICAL ^
                      %BACKEND_IMAGE%:%TAG%

                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      aquasec/trivy:latest image ^
                      --exit-code 1 ^
                      --severity HIGH,CRITICAL ^
                      %FRONTEND_IMAGE%:%TAG%

                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      aquasec/trivy:latest image ^
                      --exit-code 1 ^
                      --severity HIGH,CRITICAL ^
                      %DB_IMAGE%:%TAG%
                '''
            }
        }
    }

    post {
        success {
            echo " Backend, Frontend & Database images built and scanned successfully"
            bat 'docker images | findstr todosummary'
        }
        failure {
            echo " Build or Trivy vulnerability scan failed"
        }
    }
}