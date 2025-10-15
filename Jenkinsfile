pipeline {
    agent { label 'master' }
    stages {
        stage('Test') {
            steps {
                echo "🧩 Workspace check"
                sh 'pwd && whoami && ls -al /var/jenkins_home/workspace'
            }
        }
    }
}
