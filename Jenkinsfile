pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        git branch: 'develop', credentialsId: 'github-ssh', url: 'git@github.com:beyond-sw-camp/be18-4th-3team-project.git'
      }
    }

    stage('Build Backend') {
      steps {
        dir('Backend') {
          sh './gradlew clean build -x test'
        }
      }
    }

    stage('Build Frontend') {
      steps {
        dir('Frontend') {
          sh 'npm install && npm run build'
        }
      }
    }

    stage('Success') {
      steps {
        echo 'Build completed successfully!'
      }
    }
  }
}
