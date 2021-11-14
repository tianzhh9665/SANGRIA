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

...

Service-operation APIs:

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
