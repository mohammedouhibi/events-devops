pipeline {
        agent any

        stages {
            stage('Chekout GIT'){
                steps {
                    echo 'Pulling...'
                        git branch: 'master' ,
                        url : 'https://github.com/mohamed-ali-fradj/DevopsProject.git'
                }
            }

            stage('MVN CLEAN') {
                steps{
                    sh 'mvn clean install';

                }

            }

             stage('MVN COMPILE') {
                steps{
                    sh 'mvn compile';

                }

            }

            stage('SONARQUBE') {
                 steps {
                     sh 'mvn sonar:sonar -Dsonar.login=admin -Dsonar.password=sonar -Dmaven.test.skip=true';
                 }
            }
            stage('JUnit / Mockito') {
                 steps {
                       sh 'mvn test'
                       junit '**/target/surefire-reports/TEST-*.xml'
                        }
                 }

            stage('Nexus deploy') {
                 steps {
                 sh 'mvn deploy -Dmaven.test.skip=true'
                        }
                 }
            stage('Docker Build images') {
                            steps {
                                script {
                                    sh 'docker build -t medalifradj/medalifradj-5twin3-devops:1.0.0 .'
                                    sh 'docker pull mysql:5.7'


                                }
                            }
                        }
                        stage('Push image Dockerhub') {
                            steps {
                                script {
                                    withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerpwd')]) {
                                        sh 'docker login -u medalifradj -p ${dockerpwd}'
                                        sh 'docker push medalifradj/medalifradj-5twin3-devops:1.0.0'


                                    }
                                }
                            }
                        }
                        stage('Docker Compose') {
                            steps {
                                script {
                                    sh 'docker compose up -d '
                                }
                            }
                        }

        }
}
