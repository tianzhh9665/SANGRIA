# COMSW4156-TEAM-SANGRIA-TEAM-ASSIGNMENT-REPO
Build Status: ![Build Status](https://github.com/tianzhh9665/COMSW4156-TEAM-SANGRIA-REPO/actions/workflows/ci.yml/badge.svg)


Team members: Zhiyuan Lin(zl2989), Zoe Cui(qc2292), Linyu Li(ll3465), Tianzhi Huang(th2888)

Client and Service APIs:  

We are using Swagger to denote our implemented Client and Service APIs and following are the links:  

Swagger:    
1. Client:  
	Service-client: http://35.196.112.19:8093/client/swagger-ui.html  
2. Service:  
	Service-auth: http://35.196.112.19:8091/auth/swagger-ui.html  
	Service-operation: http://35.196.112.19:8092/operation/swagger-ui.html  
	
Client Description:  

The client we developed is a service called service-client. Because the ideal user of our service are various games connecting to our service, for simplicity, we developed this service to represent various operations done by the games. Our client does nothing but sending HTTP get/post requests to our service-auth/service-operation, and return the results that our services return.


How to build, run, and test the project:  

Build:  

	a. clone the repo to the local machine.  
	b. have a JAVA IDE, we use Eclipse so we will use it as the example IDE in this documentation.  
	c. have Java and maven installed on the computer, have lombok installed in the eclipse  
	d. have jdk, maven configured in the eclipse  
	e. File --> import --> Existing Maven Project --> choose the folder service-auth(or service-operation) --> finish  
	f. wait for the maven to auto-download all required dependencies jar  
	g. Project --> clean to clean the project first  
	h. right-click on the project folder --> maven --> Update Project to update and build the project  

Run:  

  	Run the project locally:  
		a. build the project first described above  
		b. run src/main/java/ServiceAuthApplication.java(or src/main/java/ServiceOperationApplication.java for service-operation) as java Application (right-click on the file --> run as.. --> Java Application)  

	Run the project on the VM(deployment):  
		a. right-click on the project folder --> run as --> maven clean  
		b. right-click on the project folder --> run as --> maven install  
		c. upload the service-auth-0.0.1-SNAPSHOT.jar(or service-operation-0.0.1-SNAPSHOT.jar) file in the src/target/ to the VM  
		d. have java, maven, MySQL installed on the VM  
		e. on the VM, run "nohup java -jar service-auth(operation)-0.0.1-SNAPSHOT.jar &"  
		
Test:

	Unit Test:  
		In IDE:  
			a. all test cases/code are written in src/test/java/com/sangria/auth(operation)/ServiceAuth(Operation)ApplicationTests.java  
			b. run the .java as Junit Test(right-click on the file --> run as --> Junit Test)  
		In Linux:
			javac -cp .:service-operation(auth)-0.0.1-SNAPSHOT.jar org.junit.runner.JUnitCore service-operation(auth)/src/test/java/com/sangria/operation(auth)/ServiceOperation(Auth)ApplicationTests.java  
		
	Test via Postman:  
		a. have the project deployed and run on the vm as described above  
		b. in the Postman, send requests to the endpoints: e.g. 35.196.112.19/auth/gameManager/login with parameters  
		
	Test in CI:  
		All Unit tests have been included in the Github Action and will run on every push.  
		
Style Checker and Static Bug Finder:  
	We are using the SonarCloud as the style checker and static bug finder during the process of CI using Github Action, and the report could be found here:  
	
	Service-auth: https://sonarcloud.io/summary/overall?id=jyyzlzy_COMSW4156-TEAM-SANGRIA-REPO  
	Service-Operation: https://sonarcloud.io/summary/overall?id=tianzhh9665_COMSW4156-TEAM-SANGRIA-REPO  
	
Coverage Test:  
	Coverage is computed by the Junit coverage and the report could be found here:  
	Note: Because of the design issues of the service development framework we chosen (SpringBoot), there are many parts of the codes in the project which may not be reached by the coverage test, e.g. controller(which is just the entrance of the in comming requests), unused utils.java, dtos, daos, entities(which are used during the implementation but will not be reached by the coverage test). Hence, we are focusing on enchancing our test coverage on xxServiceImpl.java, which is the place for nearly all the codes of logic implementation. Overcall, we achieved 81% on service-operation's xxServiceImpl.java files and 80.9% on service-auth's. 
	
	Service-auth: coverage/service-auth/index.html  
	Service-operation: coverage/service-operation/index.html  
	
