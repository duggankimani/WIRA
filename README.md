JBPMHT
======

#JBPM Human Task View Project

Pre-requisites
--------------

##1. JDK 7
http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html

##2. Tomcat 7
http://tomcat.apache.org/download-70.cgi

##3. Postgresql 9
http://www.postgresql.org/download/

##4. BPMN2 Editor
* Eclipse Plugin
* Guvnor installation
* Designer installation

Build War
---------
1. Run build target
* ant war

Setup DB
--------
1. Login to your postgres instance
2. Create Database workflowmgr

Configure Apache Tomcat 7
-------------------------
* Configure Datasource: BTM - http://docs.codehaus.org/display/BTM/Tomcat
* Additional Jars

- annotations-api.jar
- antlr-2.7.7.jar
- antlr-3.3.jar
- btm-2.1.3.jar
- btm-tomcat55-lifecycle-2.1.3.jar
- commons-collections-3.2.1.jar
- dom4j-1.6.1.jar
- ecj-3.7.2.jar
- ejb3-persistence-1.0.2.GA.jar
- geronimo-jta_1.1_spec-1.1.1.jar
- hibernate-annotations-3.4.0.GA.jar
- hibernate-commons-annotations-3.1.0.GA.jar
- hibernate-core-3.3.2.GA.jar
- hibernate-entitymanager-3.4.0.GA.jar
- hibernate-jpa-2.0-api-1.0.1.Final.jar
- javassist-3.14.0-GA.jar
- javassist-3.4.GA.jar
- jcl-over-slf4j-1.6.4.jar
- jta-1.1.jar
- mysql-connector-java-5.0.8-bin.jar
- servlet-api.jar
- slf4j-api-1.6.4.jar
- slf4j-jdk14-1.6.4.jar

BPMN Editor
-----------

Approach 1
----------
https://community.jboss.org/message/715223
* Install Guvnor
* Install Designer

Approach 2
----------
* Eclipse Plugin - http://download.jboss.org/drools/release/6.0.0.Final/org.drools.updatesite/

BPMN Configuration
------------------
Special process variable names

subject -
description - 
document - 
docDate - 

ADDITIONAL Maven Libraries - To be addded to local repository
-------------------------------------------------------------
mvn install:install-file -DgroupId=org.matheclipse -DartifactId=matheclipse -Dversion=0.0.10_1 -Dpackaging=jar -Dfile=./src/main/webapps/WEB-INF/lib/matheclipse-parser-0.0.10.jar 


