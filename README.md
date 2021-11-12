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

...
