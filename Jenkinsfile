pipeline {
	agent any
	options {
        timeout(time: 1, unit: 'HOURS')
    }
    parameters {
        booleanParam(
            name: 'AWS_DEPLOY_TO_TEST',
            defaultValue: false,
            description: 'Deploy to AWS test?'
        )
        booleanParam(
            name: 'AWS_DEPLOY_TO_PROD',
            defaultValue: false,
            description: 'Deploy to AWS PROD? For `master` branch only'
        )
    }
	stages {
		stage("Build UI") {
            agent {
                label 'master'
            }

			steps {
				script {
					echo "-----------------------------------------------------------------------------------------------"
					echo "Build UI"
					sh "./gradlew --version"
					sh "./gradlew clean buildUI -x check"
				}
			}
		}

		stage("Build artifact") {
            agent {
                label 'master'
            }

			steps {
				script {
					echo "-----------------------------------------------------------------------------------------------"
					echo "Build artifact"
					sh "./gradlew build -x check"
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

        stage('Deploying to AWS test') {
            agent {
                label 'master'
            }
            when {
                expression { AWS_DEPLOY_TO_TEST == "true" }
            }

            steps {
                sh "echo Deploying to AWS test"
                withCredentials([
                        string(credentialsId: 'AWS_STAGE_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'AWS_STAGE_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY'),
                        string(credentialsId: 'AWS_STAGE_DEFAULT_REGION', variable: 'AWS_DEFAULT_REGION')
                ]) {
                    dir('terraform/webservice') {
                        sh "chmod +x tf_apply.sh"
                        sh './tf_apply.sh test'
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
                withCredentials([
                        string(credentialsId: 'AWS_STAGE_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'AWS_STAGE_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY'),
                        string(credentialsId: 'AWS_STAGE_DEFAULT_REGION', variable: 'AWS_DEFAULT_REGION')
                ]) {
                    dir('terraform/webservice') {
                        sh "chmod +x tf_apply.sh"
                        sh './tf_apply.sh prod'
                    }
                }
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
