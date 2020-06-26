pipeline {
	agent any
	options {
        timeout(time: 1, unit: 'HOURS')
    }
    parameters {
        booleanParam(
            name: 'AWS_DEPLOY_TO_STAGING',
            defaultValue: false,
            description: 'Deploy to AWS STAGING?'
        )
        booleanParam(
            name: 'AWS_DEPLOY_TO_PROD',
            defaultValue: false,
            description: 'Deploy to AWS PROD? For `master` branch only'
        )
    }
	stages {
		stage("Build") {
            agent {
                label 'master'
            }

			steps {
				script {
					echo "-----------------------------------------------------------------------------------------------"
					echo "Build"
					sh "./gradlew --version"
					sh "./gradlew clean buildUI build -x check"
				}
			}
		}

        // TODO: SCA step

		stage("Unit tests") {
            agent {
                label 'master'
            }

            steps {
                script {
                    echo "-----------------------------------------------------------------------------------------------"
                    echo "Unit tests"
                    sh "./gradlew test"
                }
            }
            post {
                always {
                    junit "**/test-results/test/*.xml"
                }
            }
        }

 		/* stage('Getting Terraform') {
            agent {
                label 'master'
            }
            steps {
                sh "rm -f terraform_0.12.20_linux_amd64.zip"
                sh "rm -R -f terraform"
                sh "rm -R -f terra"
                sh "wget https://releases.hashicorp.com/terraform/0.12.20/terraform_0.12.20_linux_amd64.zip"
                sh "unzip terraform_0.12.20_linux_amd64.zip"
                sh "chmod +x terraform"
                sh "cp -f terraform /usr/local/bin"
            }
		} */

        stage('Deploying to AWS STAGING') {
            agent {
                label 'master'
            }
            when {
                expression { AWS_DEPLOY_TO_STAGING == "true" }
            }

            steps {
                sh "echo Deploying to AWS STAGING"
                withCredentials([
                        string(credentialsId: 'AWS_STAGE_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'AWS_STAGE_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY'),
                        string(credentialsId: 'AWS_STAGE_DEFAULT_REGION', variable: 'AWS_DEFAULT_REGION')
                ]) {
                    dir('terraform/webservice') {
                        sh "chmod +x tf_apply.sh"
                        sh './tf_apply.sh staging'
                    }
                }
            }
        }

        stage('Deploying to AWS PROD (declarative yet)') {
            agent {
                label 'master'
            }
            when {
                branch 'master'
                expression { AWS_DEPLOY_TO_PROD == "true" }
            }

            steps {
                sh "echo Deploying to AWS PROD"
                // TODO: deploy to PROD
            }
        }
	}

	post {
	    always {
          deleteDir()
        }

		failure {
			echo "-----------------------------------------------------------------------------------------------"
			echo "-                                       FAILED                                                -"
			echo "-----------------------------------------------------------------------------------------------"
		}
		success {
			echo "-----------------------------------------------------------------------------------------------"
            echo "-                                      SUCCESS                                                -"
            echo "-----------------------------------------------------------------------------------------------"
		}
	}
}
