pipeline {
    agent {
        docker {
            image 'kdilipkumar/jenkins-agent:v19'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock -v /var/jenkins_home/dep-check-data:/usr/share/dependency-check/data'
        }
    }

    environment {
        BACKEND_IMAGE  = "todosummary/backend"
        FRONTEND_IMAGE = "todosummary/frontend"
        DB_IMAGE       = "todosummary/database"
        TAG            = "${BUILD_NUMBER}"

        SONAR_URL = "http://localhost:9000"
        DOCKER_CREDS = credentials('docker-cred')
    }

    stages {

        /* -------------------- CLONE -------------------- */
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Dilip-Devopos/TodoAssistant.git'
            }
        }

        /* -------------------- SONAR -------------------- */
        stage('Static Code Analysis (SonarQube)') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) {
                    sh '''
                        sonar-scanner \
                        -Dsonar.projectKey=TodoSummaryAssistant \
                        -Dsonar.sources=. \
                        -Dsonar.host.url=${SONAR_URL} \
                        -Dsonar.login=${SONAR_AUTH_TOKEN}
                    '''
                }
            }
        }

        /* -------------------- OWASP -------------------- */
        stage('OWASP Dependency Check') {
            steps {
                sh '''
                    mkdir -p dependency-check-reports
                    /opt/dependency-check/bin/dependency-check.sh \
                    --project "TodoSummaryAssistant" \
                    --scan . \
                    --out dependency-check-reports \
                    --format "ALL" \
                    --data /usr/share/dependency-check/data
                '''
                dependencyCheckPublisher pattern: 'dependency-check-reports/*.xml'
            }
        }

        /* -------------------- BUILD IMAGES -------------------- */
        stage('Build Backend Image') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    sh "docker build -t ${BACKEND_IMAGE}:${TAG} ."
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('Frontend/todo') {
                    sh "docker build -t ${FRONTEND_IMAGE}:${TAG} ."
                }
            }
        }

        stage('Build Database Image') {
            steps {
                dir('Database') {
                    sh "docker build -t ${DB_IMAGE}:${TAG} ."
                }
            }
        }

        /* -------------------- TRIVY -------------------- */
        stage('Security Scan with Trivy') {
            steps {
                sh '''
                    trivy image --severity HIGH,CRITICAL \
                    --format table \
                    -o trivy-backend.html ${BACKEND_IMAGE}:${TAG}

                    trivy image --severity HIGH,CRITICAL \
                    --format table \
                    -o trivy-frontend.html ${FRONTEND_IMAGE}:${TAG}
                '''
            }
        }

        /* -------------------- PUSH -------------------- */
        stage('Push Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-cred') {
                        sh"""
                            docker.image("${BACKEND_IMAGE}:${TAG}").push()
                            docker.image("${FRONTEND_IMAGE}:${TAG}").push()
                            docker.image("${DB_IMAGE}:${TAG}").push()
                        """    
                    }
                }
            }
        }
    }

    /* -------------------- POST -------------------- */
    post {
        success {
            emailext(
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                🎉 Build Successful!

                Images created & pushed:
                - ${BACKEND_IMAGE}:${TAG}
                - ${FRONTEND_IMAGE}:${TAG}
                - ${DB_IMAGE}:${TAG}

                Build URL: ${BUILD_URL}
                """,
                to: "dilipbca99@gmail.com",
                attachmentsPattern: "trivy-*.html,dependency-check-reports/*"
            )
        }

        failure {
            emailext(
                subject: "FAILURE: ${JOB_NAME} #${BUILD_NUMBER}",
                body: "❌ Build Failed\n\nCheck logs: ${BUILD_URL}",
                to: "dilipbca99@gmail.com"
            )
        }
    }
}