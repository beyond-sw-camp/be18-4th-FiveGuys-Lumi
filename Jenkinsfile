pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
metadata:
  name: jenkins-agent
spec:
  containers:
  - name: gradle
    image: gradle:8.10.2-jdk21-alpine
    command:
    - cat
    tty: true
  - name: docker
    image: docker:28.5.1-cli-alpine3.22
    command:
    - cat
    tty: true
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
'''
        }
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-cred'
        DOCKER_IMAGE_NAME = 'amicitia/lumi'
    }

    stages {
        stage('Gradle Build') {
            steps {
                container('gradle') {
                    sh 'gradle -v'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                container('docker') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: DOCKER_CREDENTIALS_ID,
                            usernameVariable: 'DOCKER_USERNAME',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )]) {
                            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                            sh 'docker build -t $DOCKER_IMAGE_NAME:$BUILD_NUMBER .'
                            sh 'docker push $DOCKER_IMAGE_NAME:$BUILD_NUMBER'
                        }
                    }
                }
            }
        }
    }
}
