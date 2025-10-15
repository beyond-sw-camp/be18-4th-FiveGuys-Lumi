pipeline {
    agent any
    stages {
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
