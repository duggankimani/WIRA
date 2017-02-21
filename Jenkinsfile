pipeline{
	agent any
	stages{
		tools{
			maven 'apache-maven-3.0.1'
		}
		stage('Build'){
			steps{
				sh 'mvn clean install'
			}
		}
		
		stage('Deploy'){
			steps{
				sh '/DATA/installations/tomcat_8/bin/shutdown.sh'
				sh 'rm -r /DATA/installations/tomcat_8/bin/wira*'
				sh 'cp target/wira-0.9.war /DATA/installations/tomcat_8/webapps/wira.war'
				sh '/DATA/installations/tomcat_8/bin/start.sh'
			}
		}
	}
}
