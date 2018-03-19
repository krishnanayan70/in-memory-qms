#queueing-system
In Memory Queueing Stystem

Problem statement: *Design an efficient in-memory queueing system with low latency requirements Functional specification:*

	1. Queue holds JSON messages
	2. Allow subscription of Consumers to messages that match a particular expression
	3. Consumers register callbacks that will be invoked whenever there is a new message
	4. Queue will have one producer and multiple consumers
	5. Consumers might have dependency relationships between them. For ex, if there are three consumers A, B and C. One dependency relationship can be that C cannot consume a particular message before A and B have consumed it. C -> (A,B) (-> means must process after)
	6. Queue is bounded in size and completely held in-memory. Size is configurable.
	7. Handle concurrent writes and reads consistently between producer and consumers.
	8. Provide retry mechanism to handle failures in message processing.
	
**Assumptions**
	
	1. Dependencies can be added only for consumers having same topic (consuming on same pattern).
	2. Dependencies can only be added upon the consumers which are already defined. For e.g If we are defining C -> (A,B) then A and B have been already defined before adding this dependency or before defining C.
	3. Dependencies are acyclic.
	2. Consumer's callback are callback urls with POST method.
	
**Set Up:**

	1. Install MongoDB
	2. Start mongoDB and create db "qms" and collection "consumers".
	3. Install and start Redis
	
**APIs**

	1. /consumer/subscribe
		POST
		url : /consumer/subscrib
		body:
		{
			"name":"C",
			"topic":"sql",
			"callbackUrl":"/consume",
			"dependencies":["A","B"]
		}
	2. /consumer/{consumer}/add_dependency
		POST
		url : /consumer/C/add_dependency
		body : ["A","B"]
	3. /config/{name}/{value}
		POST
		url : /config/size/10
		POST
		url : /config/retry/5
	4. /publish
		POST
		body : {
					"topic":"sql",
					"content":"mysql-content"
				}


	