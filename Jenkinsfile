pipeline {
    agent { label 'ci-agent' }  // ✅ 쿠버네티스 클라우드의 Pod Template 호출

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
        DISCORD_WEBHOOK_CREDENTIALS_ID = 'discord-webhook-lumi'

        BACKEND_IMAGE_NAME = 'amicitia/lumi-backend'
        FRONTEND_IMAGE_NAME = 'amicitia/lumi-frontend'
    }

    stages {
        stage('Gradle Build') {
            steps {
                container('gradle') {
                    sh '''
                        cd Backend
                        chmod +x gradlew
                        ./gradlew clean build -x test
                    '''
                }
            }
        }

        stage('Backend Docker Build & Push') {
            steps {
                container('docker') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: DOCKER_CREDENTIALS_ID,
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            sh '''
                                echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                                docker build -t $BACKEND_IMAGE_NAME:$BUILD_NUMBER -f Backend/Dockerfile ./Backend
                                docker push $BACKEND_IMAGE_NAME:$BUILD_NUMBER
                            '''
                        }
                    }
                }
            }
        }

        stage('Frontend Docker Build & Push') {
            steps {
                container('docker') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: DOCKER_CREDENTIALS_ID,
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            sh '''
                                echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                                docker build -t $FRONTEND_IMAGE_NAME:$BUILD_NUMBER -f Frontend/Dockerfile ./Frontend
                                docker push $FRONTEND_IMAGE_NAME:$BUILD_NUMBER
                            '''
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            withCredentials([string(
                credentialsId: DISCORD_WEBHOOK_CREDENTIALS_ID,
                variable: 'DISCORD_WEBHOOK_URL'
            )]) {
                discordSend description: """
                📦 *${env.JOB_NAME}:${currentBuild.displayName}*
                ▶️ 결과 : ${currentBuild.result}
                🕒 실행 시간 : ${(currentBuild.duration / 1000).intValue()}초
                """,
                result: currentBuild.currentResult,
                title: "Jenkins CI 알림",
                webhookURL: "${DISCORD_WEBHOOK_URL}"
            }
        }
    }
}
