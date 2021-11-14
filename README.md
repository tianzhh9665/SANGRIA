# COMSW4156-TEAM-SANGRIA-REPO
COMSW4156-TEAM-SANGRIA-REPO

Team members: Zhiyuan Lin(zl2989), Zoe Cui(qc2292), Linyu Li(ll3465), Tianzhi Huang(th2888)

Service-auth APIs:

1. /gameManager/register	-->game manager registration  
	a. request method: POST  
	b. parameters: username(required), password(required), gameName(required)  
	c. return: JSON Object with code 200 --> success, 500 --> failed with corresponding error message.  

2. /gameManager/login		-->game manager login  
	a. request method: POST  
	b. parameters: username(required), password(required)  
	c. return JSON Object with code 200 data token --> success, 500 --> failed with corresponding error message.  

3. /gameManager/token		-->token authentication  
	a. request method: GET  
	b. parameters: token(required)  
	c. return JSON Object with code 200 --> success, 500 --> failed with corresponsing error message.  
	
4. /gameManager/info -->manager info, game info and inventory info  
	a. request method: GET  
	b. parameters: token(required)  
	c. return JSON Object with code 200 --> success, 500 --> failed with corresponsing error message.
	
5. /gameManager/delete -->delete game manager and its corresponding game, inventories and items  
	a. request method: POST  
	b. parameters: token(required)  
	c. return JSON Object with code 200 --> success, 500 --> failed with corresponsing error message.
	
6. /gameManager/logout -->game manager logout  
	a. request method: POST  
	b. parameters: token(required)  
	c. return JSON Object with code 200 --> success, 500 --> failed with corresponsing error message.  


Service-operation APIs:   
(please consider only Service-auth as the official submission for Assignment T3 First Iteration, service-operation is still under development and it is here for the purpose of providing some functionalities for First Iteration Demo with the IA)  

1. /inventory/add	-->add a new inventory to the game  
	a. request method: POST  
	b. parameters: token(required)  
	c. return: JSON Object with code 200 --> success, 500 --> failed with corresponding error message.   
2. /inventory/info	-->query inventory info  
	a. request method: GET  
	b. parameters: token(required), inventoryId(optional, if presents, meaning query a specific iventory info, otherwise query all inventory info in the game)  
	c. return: JSON Object with code 200 --> success with inventory info in data, 500 --> failed with corresponding error message. 
3. /inventory/clear	-->clear a inventory  
	a. request method: POST   
	b. parameters: token(required), inventoryId(required)  
	c. return: JSON Object with code 200 --> success, 500 --> failed with corresponding error message.   
4. /item/add	-->add a item to a inventory  
	a. request method: POST   
	b. parameters: token(required), name(required), inventoryId(required), type(required), attributes(optional)  
	c. return: JSON Object with code 200 --> success, 500 --> failed with corresponding error message.   
5. /item/info	-->query a item's info  
	a. request method: GET   
	b. parameters: token(required), itemId(required)  
	c. return: JSON Object with code 200 --> success with item info in data, 500 --> failed with corresponding error message.   

... more to come

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
		e. on the VM, run "nohup java -jar service-auth-0.0.1-SNAPSHOT.jar &"  
		
Test:

	Unit Test:  
		a. all test cases/code are written in src/test/java/com/sangria/auth(operation)/ServiceAuth(Operation)ApplicationTests.java  
		b. run the .java as Junit Test(right-click on the file --> run as --> Junit Test)  
		
	Test via Postman:  
		a. have the project deployed and run on the vm as described above  
		b. in the Postman, send requests to the endpoints: e.g. 35.196.112.19/auth/gameManager/login with parameters  
		
Style Checker:  
	The style checker we used is CheckStyle Plug-in, which is a eclipse software that is built-in in the IDE. it can be activated by right-click the file --> check --> activate checkstyle, and can be downloaded in the eclipse marketplace. It generates no report and the result can not be redirected to a file since it will show the check result directly in the IDE interface.  

