pipeline {
    agent any

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"
        TRIVY_CACHE    = "trivy-cache"
        REPORT_DIR     = "trivy-reports"
        DOCKER_USER    = "kdilipkumar"  // your Docker Hub username
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

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat '''
                        echo Logging into Docker Hub...
                        docker login -u %DOCKER_USER% -p %DOCKER_PASS%

                        echo Pushing backend image...
                        docker tag %BACKEND_IMAGE%:%TAG% %DOCKER_USER%/%BACKEND_IMAGE%:%TAG%
                        docker push %DOCKER_USER%/%BACKEND_IMAGE%:%TAG%

                        echo Pushing frontend image...
                        docker tag %FRONTEND_IMAGE%:%TAG% %DOCKER_USER%/%FRONTEND_IMAGE%:%TAG%
                        docker push %DOCKER_USER%/%FRONTEND_IMAGE%:%TAG%

                        echo Pushing database image...
                        docker tag %DB_IMAGE%:%TAG% %DOCKER_USER%/%DB_IMAGE%:%TAG%
                        docker push %DOCKER_USER%/%DB_IMAGE%:%TAG%

                        echo Docker push completed.
                    '''
                }
            }
        }
    }

    post {
        success {
            echo " Images built, Trivy reports generated, and pushed to Docker Hub successfully."
        }
        failure {
            echo " Something failed during build, scan, or push."
        }
    }
}