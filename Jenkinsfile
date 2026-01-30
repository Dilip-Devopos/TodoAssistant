pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
        TRIVY_CACHE    = "trivy-cache"
        REPORT_DIR     = "trivy-reports"
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

        stage('Prepare Trivy') {
            steps {
                bat '''
                    docker pull aquasec/trivy:latest
                    docker volume inspect %TRIVY_CACHE% >nul 2>&1 || docker volume create %TRIVY_CACHE%
                    if not exist %REPORT_DIR% mkdir %REPORT_DIR%
                '''
            }
        }

        stage('Trivy Scan and Generate HTML Reports') {
            steps {
                bat '''
                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      -v %TRIVY_CACHE%:/root/.cache/ ^
                      -v %CD%\\%REPORT_DIR%:/reports ^
                      aquasec/trivy:latest image ^
                      --format template --template "@contrib/html.tpl" ^
                      -o /reports/backend_%TAG%.html ^
                      %BACKEND_IMAGE%:%TAG% || echo "Trivy scan completed for backend"

                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      -v %TRIVY_CACHE%:/root/.cache/ ^
                      -v %CD%\\%REPORT_DIR%:/reports ^
                      aquasec/trivy:latest image ^
                      --format template --template "@contrib/html.tpl" ^
                      -o /reports/frontend_%TAG%.html ^
                      %FRONTEND_IMAGE%:%TAG% || echo "Trivy scan completed for frontend"

                    docker run --rm ^
                      -v //var/run/docker.sock:/var/run/docker.sock ^
                      -v %TRIVY_CACHE%:/root/.cache/ ^
                      -v %CD%\\%REPORT_DIR%:/reports ^
                      aquasec/trivy:latest image ^
                      --format template --template "@contrib/html.tpl" ^
                      -o /reports/database_%TAG%.html ^
                      %DB_IMAGE%:%TAG% || echo "Trivy scan completed for database"
                '''
            }
        }

        stage('Publish Trivy Reports') {
            steps {
                archiveArtifacts artifacts: "${REPORT_DIR}/*.html", fingerprint: true
            }
        }
    }

    post {
        success {
            echo "Images built & Trivy HTML reports generated. Download from Jenkins artifacts."
        }
    }
}