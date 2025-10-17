pipeline {
    agent {
        kubernetes {
            label 'ci-agent'
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
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
  - name: git
    image: alpine/git:2.45.2
    command:
    - cat
    tty: true
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    parameters {
        string(name: 'DOCKER_IMAGE_VERSION', defaultValue: '', description: 'Docker Image Version')
        string(name: 'DID_BUILD_APP', defaultValue: '', description: 'Did Build Frontend')
        string(name: 'DID_BUILD_API', defaultValue: '', description: 'Did Build Backend')
    }

    environment {
        GIT_CREDENTIALS_ID = 'github-deploy-key'
        GIT_USER_NAME = 'JJJJungw'
        GIT_USER_EMAIL = 'dyungwoo3600@gmail.com'
    }

    stages {
        stage('Checkout main branch') {
            steps {
                container('git') {
                    checkout scm
                    sh '''
                        git checkout main || true
                    '''
                    echo "   Received Params:"
                    echo "   DOCKER_IMAGE_VERSION: ${params.DOCKER_IMAGE_VERSION}"
                    echo "   DID_BUILD_APP: ${params.DID_BUILD_APP}"
                    echo "   DID_BUILD_API: ${params.DID_BUILD_API}"
                }
            }
        }

        stage('Update Frontend manifest') {
            when { expression { params.DID_BUILD_APP == "true" } }
            steps {
                container('git') {
                    dir('frontend') {
                        sh '''
                            echo "🔧 Updating frontend manifest..."
                            sed -i "s|amicitia/lumi-frontend:.*|amicitia/lumi-frontend:${DOCKER_IMAGE_VERSION}|g" frontend-deploy.yaml
                            git status
                        '''
                    }
                }
            }
        }

        stage('Update Backend manifest') {
            when { expression { params.DID_BUILD_API == "true" } }
            steps {
                container('git') {
                    dir('backend') {
                        sh '''
                            echo " Updating backend manifest..."
                            sed -i "s|amicitia/lumi-backend:.*|amicitia/lumi-backend:${DOCKER_IMAGE_VERSION}|g" backend-deploy.yaml
                            git status
                        '''
                    }
                }
            }
        }

        stage('Commit & Push Changes') {
            when { expression { params.DID_BUILD_APP == "true" || params.DID_BUILD_API == "true" } }
            steps {
                container('git') {
                    script {
                        echo "📤 Committing updated manifests..."
                        sh '''
                            git config user.name "${GIT_USER_NAME}"
                            git config user.email "${GIT_USER_EMAIL}"
                            git add .
                            git commit -m "chore: update image tag ${DOCKER_IMAGE_VERSION}" || echo "No changes to commit"
                        '''

                        // ✅ sshagent 플러그인 필수
                        sshagent([GIT_CREDENTIALS_ID]) {
                            sh 'git push origin main'
                        }
                    }
                }
            }
        }

        stage('Trigger ArgoCD Sync') {
            steps {
                echo " ArgoCD will auto-sync manifests after commit"
            }
        }
    }

    post {
        success {
            echo "✅ Manifests updated successfully"
        }
        failure {
            echo "❌ Failed to update manifests"
        }
    }
}
