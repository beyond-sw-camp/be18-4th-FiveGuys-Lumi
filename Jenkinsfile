pipeline {
    agent {
        kubernetes {
            inheritFrom ''
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:8.10.2-jdk21-alpine
    command: ['cat']
    tty: true
  - name: docker
    image: docker:28.5.1-cli-alpine3.22
    command: ['cat']
    tty: true
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
        DISCORD_WEBHOOK_CREDENTIALS_ID = 'discord-webhook-lumi'
        BACKEND_IMAGE_NAME = 'amicitia/lumi-backend'
        FRONTEND_IMAGE_NAME = 'amicitia/lumi-frontend'
    }

    stages {
        stage('Detect Changes') {
            steps {
                container('gradle') {
                    script {
                        // 최근 커밋 비교로 변경된 파일 목록 확인
                        def changedFiles = sh(
                            script: 'git diff --name-only HEAD~1',
                            returnStdout: true
                        ).trim().split("\n")

                        echo "📂 변경된 파일 목록:\n${changedFiles.join('\n')}"

                        // 변경된 경로에 따라 빌드 대상 결정
                        env.SHOULD_BUILD_BACKEND = changedFiles.any { it.startsWith("Backend/") } ? "true" : "false"
                        env.SHOULD_BUILD_FRONTEND = changedFiles.any { it.startsWith("Frontend/") } ? "true" : "false"

                        echo "💡 SHOULD_BUILD_BACKEND: ${env.SHOULD_BUILD_BACKEND}"
                        echo "💡 SHOULD_BUILD_FRONTEND: ${env.SHOULD_BUILD_FRONTEND}"
                    }
                }
            }
        }

        stage('Gradle Build') {
            when { expression { env.SHOULD_BUILD_BACKEND == "true" } }
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

        stage('Docker Login') {
            steps {
                container('docker') {
                    withCredentials([usernamePassword(
                        credentialsId: DOCKER_CREDENTIALS_ID,
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )]) {
                        sh '''
                            echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        '''
                    }
                }
            }
        }

        stage('Backend Docker Build & Push') {
            when { expression { env.SHOULD_BUILD_BACKEND == "true" } }
            steps {
                container('docker') {
                    script {
                        def version = "${env.BUILD_NUMBER}"
                        withEnv(["IMAGE_VERSION=${version}"]) {
                            sh '''
                                docker build -t $BACKEND_IMAGE_NAME:$IMAGE_VERSION -f Backend/Dockerfile ./Backend
                                docker push $BACKEND_IMAGE_NAME:$IMAGE_VERSION
                            '''
                        }
                    }
                }
            }
        }

        stage('Frontend Docker Build & Push') {
            when { expression { env.SHOULD_BUILD_FRONTEND == "true" } }
            steps {
                container('docker') {
                    script {
                        def version = "${env.BUILD_NUMBER}"
                        withEnv(["IMAGE_VERSION=${version}"]) {
                            sh '''
                                docker build -t $FRONTEND_IMAGE_NAME:$IMAGE_VERSION -f Frontend/Dockerfile ./Frontend
                                docker push $FRONTEND_IMAGE_NAME:$IMAGE_VERSION
                            '''
                        }
                    }
                }
            }
        }

        stage('Trigger k8s-manifests (ArgoCD 연동)') {
            steps {
                script {
                    def version = "${env.BUILD_NUMBER}"
                    def buildApp = "${env.SHOULD_BUILD_FRONTEND}"
                    def buildApi = "${env.SHOULD_BUILD_BACKEND}"

                    echo "🚀 Triggering CD pipeline with version ${version}"
                    echo "📦 DID_BUILD_APP=${buildApp}, DID_BUILD_API=${buildApi}"

                    build job: 'lumi-manifests',
                        parameters: [
                            string(name: 'DOCKER_IMAGE_VERSION', value: version),
                            string(name: 'DID_BUILD_APP', value: buildApp),
                            string(name: 'DID_BUILD_API', value: buildApi)
                        ],
                        wait: true
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
                script {
                    def emoji = currentBuild.result == 'SUCCESS' ? '✅' : '❌'
                    discordSend description: """
                    ${emoji} *${env.JOB_NAME}:${currentBuild.displayName}*
                    ▶️ 결과 : ${currentBuild.result}
                    🕒 실행 시간 : ${(currentBuild.duration / 1000).intValue()}초
                    """,
                    result: currentBuild.currentResult,
                    title: "Lumi CI 파이프라인 알림",
                    webhookURL: "${DISCORD_WEBHOOK_URL}"
                }
            }
        }
    }
}
