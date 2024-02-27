# This will not work without empty/incorrect KEYCLOAK_SECRET in .env file. Follow setup guide.

This has been tested to work for Windows 10. 

# Set up guide.

1) Open a terminal inside this folder and run the command 
"docker-compose up -d". 
If you changed passwords after launching it you might have to remove your volume 
with docker-compose down -v.
	
2) Add "127.0.0.1 keycloak" in your hosts folder.
	Windows: C:\Windows\System32\drivers\etc\hosts

	Linux: /etc/hosts

This is a **KNOWN** Keycloak issue and there seems to be no better way. [Stackoverflow thread for more info.](https://stackoverflow.com/questions/57213611/keycloak-and-spring-boot-web-app-in-dockerized-environment)

3) <div align="center"> <img src="/Deployment/localhost_only/steps/step1.png" alt="Step 1"> </div>
	
4) <div align="center"> <img src="/Deployment/localhost_only/steps/step2.png" alt="Step 2"> </div>
	
5) <div align="center"> <img src="/Deployment/localhost_only/steps/step3.png" alt="Step 3"> </div>
	
6) <div align="center"> <img src="/Deployment/localhost_only/steps/step4.png" alt="Step 4"> </div>
	
7) <div align="center"> <img src="/Deployment/localhost_only/steps/step5.png" alt="Step 5"> </div>
	
8) <div align="center"> <img src="/Deployment/localhost_only/steps/step6.png" alt="Step 6"> </div>
	
9) Change your .env file with your client_secret. Then run docker-compose up -d from your terminal.
This will relaunch Chess_Gateway with the correct Keycloak Client Secret.
Now you can check that everything works with in "localhost:8888/"
	
# Extra notes

The keycloak image is a custom one where I edited the registration.ftl, login.ftl, template.ftl 
files to customize the registration fields, login output when you are redirected to Keycloak Authorization server Login.

If you want your own realm then after making some changes you can export it from keycloak:8080 or from the terminal
Here are the steps:

	1) docker ps
	
	2) docker exec -it "container_name" bash
	
	3) cd /opt/keycloak/bin 
	
	4) ./kc-sh export --file /tmp/my-realm.json
	
	5) docker cp "containername":/tmp/my-realm.json ./keycloak
		
	Now you have exported realm.json and copied it inside .keycloak folder.
	But before you can import it you have to follow step 6.
	6) You have to remove Policy or change Authorization Policy Enforcement Mode to Permissive.
		
	If you choose to remove it then these are the fields to remove:
	
		"authorizationSettings" : {
			  "allowRemoteResourceManagement" : true,
			  "policyEnforcementMode" : "ENFORCING",
			  "resources" : [ {
				"name" : "Default Resource",
				"type" : "urn:chess_manager:resources:default",
				"ownerManagedAccess" : false,
				"attributes" : { },
				"_id" : "72e17570-16c9-4067-b8b9-61bdde21b3b3",
				"uris" : [ "/*" ]
			  } ],
			  "policies" : [ {
				"id" : "13e6939f-0c8b-4cd0-998e-8a0db7e9cd21",
				"name" : "Default Policy",
				"description" : "A policy that grants access only for users within this realm",
				"type" : "js",
				"logic" : "POSITIVE",
				"decisionStrategy" : "AFFIRMATIVE",
				"config" : {
				  "code" : "// by default, grants any permission associated with this policy\n$evaluation.grant();\n"
				}
			  }, {
				"id" : "279e466c-dcdd-4eda-b376-98d0fcec3733",
				"name" : "Default Permission",
				"description" : "A permission that applies to the default resource type",
				"type" : "resource",
				"logic" : "POSITIVE",
				"decisionStrategy" : "UNANIMOUS",
				"config" : {
				  "defaultResourceType" : "urn:chess_manager:resources:default",
				  "applyPolicies" : "[\"Default Policy\"]"
				}
			  } ],
			  "scopes" : [ ],
			  "decisionStrategy" : "UNANIMOUS"
			}
			
		
		
