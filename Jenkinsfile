pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
metadata:
  name: lumi-ci
spec:
  containers:
  - name: docker
    image: docker:28.5.1-cli-alpine3.22
    command:
    - cat
    tty: true
    volumeMounts:
    - name: docker-socket
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-socket
    hostPath:
      path: /var/run/docker.sock
'''
        }
    }

    environment {
        BACKEND_IMAGE_NAME = 'amicitia/lumi-backend'
        FRONTEND_IMAGE_NAME = 'amicitia/lumi-frontend'
        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
        DISCORD_WEBHOOK_CREDENTIALS_ID = 'discord-webhook-lumi'
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
                sh 'ls -la && echo "✅ Git checkout complete"'
            }
        }

        stage('Detect Changes') {
            steps {
                container('docker') {
                    script {
                        def changedFiles = sh(
                            script: 'git diff --name-only HEAD~1 || true',
                            returnStdout: true
                        ).trim().split("\n")

                        echo "📂 변경된 파일 목록:\n${changedFiles.join('\n')}"

                        env.SHOULD_BUILD_BACKEND = changedFiles.any { it.startsWith("Backend/") } ? "true" : "false"
                        env.SHOULD_BUILD_FRONTEND = changedFiles.any { it.startsWith("Frontend/") } ? "true" : "false"

                        echo "💡 SHOULD_BUILD_BACKEND: ${env.SHOULD_BUILD_BACKEND}"
                        echo "💡 SHOULD_BUILD_FRONTEND: ${env.SHOULD_BUILD_FRONTEND}"
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(
                        credentialsId: DOCKER_CREDENTIALS_ID,
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )]) {
                        sh '''
                            docker logout || true
                            echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        '''
                    }
                }
            }
        }

        stage('Backend Build & Push') {
            when { expression { env.SHOULD_BUILD_BACKEND == "true" } }
            steps {
                container('docker') {
                    sh '''
                        cd Backend
                        chmod +x gradlew
                        ./gradlew clean build -x test
                        docker build -t $BACKEND_IMAGE_NAME:${BUILD_NUMBER} -f Dockerfile .
                        docker push $BACKEND_IMAGE_NAME:${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('Frontend Build & Push') {
            when { expression { env.SHOULD_BUILD_FRONTEND == "true" } }
            steps {
                container('docker') {
                    sh '''
                        cd Frontend
                        docker build -t $FRONTEND_IMAGE_NAME:${BUILD_NUMBER} -f Dockerfile .
                        docker push $FRONTEND_IMAGE_NAME:${BUILD_NUMBER}
                    '''
                }
            }
        }

        stage('Trigger ArgoCD Sync') {
            steps {
                script {
                    build job: 'lumi-manifests',
                        parameters: [
                            string(name: 'DOCKER_IMAGE_VERSION', value: "${BUILD_NUMBER}"),
                            string(name: 'DID_BUILD_APP', value: "${env.SHOULD_BUILD_FRONTEND}"),
                            string(name: 'DID_BUILD_API', value: "${env.SHOULD_BUILD_BACKEND}")
                        ],
                        wait: true
                }
            }
        }
    }

    post {
        always {
            withCredentials([string(credentialsId: DISCORD_WEBHOOK_CREDENTIALS_ID, variable: 'DISCORD_WEBHOOK_URL')]) {
                script {
                    def emoji = currentBuild.result == 'SUCCESS' ? '✅' : '❌'
                    discordSend description: """
                    ${emoji} *${env.JOB_NAME}:${currentBuild.displayName}*
                    ▶️ 결과 : ${currentBuild.result}
                    🕒 실행 시간 : ${(currentBuild.duration / 1000).intValue()}초
                    """,
                    result: currentBuild.currentResult,
                    title: "Lumi CI Pipeline",
                    webhookURL: "${DISCORD_WEBHOOK_URL}"
                }
            }
        }
    }
}
