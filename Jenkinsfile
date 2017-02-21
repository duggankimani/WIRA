pipeline{
	agent any
	tools{
		maven 'apache-maven-3.3.9'
	}
		
	stages{
		stage('Build'){
			steps{
				sh 'mvn install:install-file -Dfile=matheclipse-0.0.10_1.jar -DgroupId=org.matheclipse -DartifactId=matheclipse -Dversion=0.0.10_1 -Dpackaging=jar'
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
