pipeline {
    agent any

    environment {
        DOCKER_USER    = "kdilipkumar"
        TAG            = "${BUILD_NUMBER}"
        BACKEND_IMAGE  = "${DOCKER_USER}/todosummary-backend"
        FRONTEND_IMAGE = "${DOCKER_USER}/todosummary-frontend"
        DB_IMAGE       = "${DOCKER_USER}/todosummary-database"
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
        
        stage('Clean Previous Reports') {
            steps {
                bat '''
                    if exist %REPORT_DIR% rmdir /s /q %REPORT_DIR%
                '''
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
                        docker push %BACKEND_IMAGE%:%TAG%

                        echo Pushing frontend image...
                        docker push %FRONTEND_IMAGE%:%TAG%

                        echo Pushing database image...
                        docker push %DB_IMAGE%:%TAG%

                        echo Docker push completed.
                    '''
                }
            }
        }

        stage('Update Helm Image Tags') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-cred',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {
                    bat '''
                        echo Cloning Helm repository...
                        rmdir /s /q TodoAssistant-helm 2>nul
                        git clone https://%GIT_USER%:%GIT_PASS%@github.com/Dilip-Devopos/TodoAssistant-helm.git

                        cd TodoAssistant-helm\\todo-summary-assistant

                        echo Updating image tags in values.yaml...

                        powershell -Command "(Get-Content 'values.yaml') -replace 'todosummary-backend:\\d+', 'todosummary-backend:%TAG%' -replace 'todosummary-frontend:\\d+', 'todosummary-frontend:%TAG%' -replace 'todosummary-database:\\d+', 'todosummary-database:%TAG%' | Set-Content 'values.yaml'"

                        git config user.email "jenkins@local"
                        git config user.name "jenkins"

                        git add values.yaml
                        git commit -m "Update image tags to %TAG%"
                        git push origin main
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Docker images pushed and Helm chart updated with tag ${TAG}"
        }
    }
}