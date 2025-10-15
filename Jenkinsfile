pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: 'github-ssh', url: 'git@github.com:beyond-sw-camp/be18-4th-3team-project.git'
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
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Success') {
            steps {
                echo '🎉 Build completed successfully!'
            }
        }
    }
}