pipeline {
    agent any

    environment {
        REGISTRY = "amicitia"                     // Docker Hub 네임스페이스
        IMAGE_NAME = "lumi"                       // 백엔드 이미지 이름
        DOCKER_CREDENTIALS_ID = "dockerhub-cred"  // 젠킨스에 추가할 Docker Hub 로그인 정보 ID
        K8S_PATH = "k8s"                          // 쿠버네티스 yaml 폴더 경로
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                echo "✅ Source code fetched successfully"
            }
        }

        stage('Build Backend') {
            steps {
                dir('Backend') {
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_CREDENTIALS_ID) {
                        def app = docker.build("${REGISTRY}/${IMAGE_NAME}:latest", "./Backend")
                        app.push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl apply -f ${K8S_PATH}/backend-deploy.yaml"
            }
        }
    }

    post {
        success {
            echo "🎉 Backend deployed successfully!"
        }
        failure {
            echo "💥 Build or Deploy failed!"
        }
    }
}
