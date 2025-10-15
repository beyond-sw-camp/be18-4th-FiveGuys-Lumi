pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // 현재 파이프라인이 읽고 있는 저장소 전체를 실제 workspace로 체크아웃
                checkout scm
                echo "✅ Repository checked out successfully."
                sh 'ls -al'
            }
        }

        stage('Backend Build') {
            steps {
                dir('Backend') {
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir('Frontend') {
                    sh 'npm install && npm run build'
                }
            }
        }
    }
}
