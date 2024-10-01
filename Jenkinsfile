pipeline {
    agent {
        label 'vm01'
    }
    tools {
        maven 'apache-maven-3.9.8'
    }
    environment {
        APP_NAME = "dev-api"
        APP_HOME = "/home/angela/service/dev-api"
        GIT_CREDENTIALS_ID = '5486ab12-f4dc-43a7-9d7a-384505b067f1'
        DOCKER_IMAGE = 'dev-local-api-image:latest'
        JAVA_HOME = '/home/angela/graalvm-jdk-22'
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }
    stages {
        stage('Checkout from GitHub') {
            steps {
                git credentialsId: "${GIT_CREDENTIALS_ID}", url: 'https://github.com/youngitn/topkey_api.git', branch: "dev"
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Prepare Docker Image') {
            steps {
                script {
                    // 复制生成的 JAR 文件到 Dockerfile 所在目录
                    sh 'cp target/*.jar ${APP_HOME}/api.jar'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // 进入工作目录
                    dir("${APP_HOME}") {
                        // 构建 Docker 镜像
                        sh 'docker build -t ${DOCKER_IMAGE} .'
                    }
                }
            }
        }
         stage('Deploy Docker Stack to Swarm') {
		    steps {
		        script {
		            dir("${APP_HOME}") {
		                try {
		                    // 停止並移除現有的 Stack（如果存在）
		                    echo 'Shutting down existing stack...'
		                    sh 'docker stack rm ${APP_NAME} || true'  // 用 `docker stack rm` 來移除 Stack
		
		                    // 部署新的 Stack
		                    echo 'Deploying new stack...'
		                    sh 'docker stack deploy --compose-file stack.yml ${APP_NAME}'
		
		                    echo 'Deployment completed successfully!'
		                } catch (Exception e) {
		                    echo "Deployment failed: ${e.getMessage()}"
		                    currentBuild.result = 'FAILURE'
		                }
		            }
		        }
		    }
	}
}
}
