# SlyChess Project Overview.

<div align="center">
  <img src="/project_images/slychess_technical_overview.png" alt="overview">
</div>

* Play Chess against Stockfish engine in realtime with the help of WebSockets

* The project is deployed on Virtual Private Server (VPS) with Docker-Compose. Launch locally with docker-compose file in Deployment folder.

* RabbitMQ Message Broker for Message durability, reliability and acknowledgements.

* OpenID Connect (OIDC) which extends Oauth2 authorization flow with Keycloak Authorization Server.

* User Management, Registration by Keycloak. SMPT email to reset password coming soon.

* Saving games into MySQL Database for authenticated users with Chess_Manager who is an Oauth2 Resource Server.

* Backend-For-Frontend pattern to serve the frontend with Authorization without exposing JWT (Json Web Token). More about that below.

# WebSockets with STOMP and RabbiTMQ Message Broker.
<div align="center">
  <img src="/project_images/main2.png" alt="main image">
</div>

* Enables real time communication between the Server and The Client. Although the best chess move response time is limited by the response time of Chess_Backend.

* Personally i wanted to know why WebSockets are used in the real world. What are the benefits and downsides to using them. And since most ideas were already executed like
  a chatbot or stock market i decided a low latency in a chess game against computer would be a nice scenario for WebSockets.

* Simple Text Oriented Messaging Protocol (STOMP) is used as the messaging protocol so i don't have to invent a way which messages are valid or invalid in the frontend or server side.

* Chess_Frontend: The WebSockets + STOMP protocol is achieved from using webstomp-client library. We connect to the Chess_Manager with function connect().
We can close the connection with function called close() in WebSocket.vue.

* Chess_Manager is responsible for configuring the WebSocket with STOMP connections and adding RabbitMQ message Broker to the mix.

# Viewing history (saved games)

<div align="center">
   <img src="/project_images/history2.png" alt="history">
</div>

* You can view Game History if you are an authenticated user.

* Chess_Manager saves games into MySQL database if a user is authenticated. It's an Oauth2 Resource Server and he checks for a proper Authorization Header
and asks Keycloak if everything is correct. Chess_Gateway sends the Authorization Header converted from a Session Cookie with TokenRelay in Spring Cloud Gateway.

* We can fetch and save games by using Chess_Manager with @RestController methods.

# Keycloak. Authorization Sever and User Management.

<div align="center">
   <img src="/project_images/login.png" alt="login">
</div>

Since nowadays backend and frontend are seperate applications with their own build tools we have to change the configuration so the default 
answer is 401 Unauthorized instead of 302 redirect to Authorization Server. The main reason for not using redirects with SPAs is that you would run into Cross-
Origin Request Sharing (CORS) issues.

The Frontend is responsible for initiating the Login and Logout flow. Login and Logout is done with window.location.href so we have a full page reload and flush client side cache. For Logout we receive 202 status code and a Location Header from Chess_Manager to follow through for the Authorization Server to complete the logout completely.


# Backend-For-Frontend (BFF) pattern

All solutions stem from a problem. What is the problem that BFF pattern tries to solve? A security expert Philippe De Ryck says: 
["From a security perspective, it is virtually impossible to secure tokens in a frontend web application." And he recommends 
that developers rely on BFF pattern instead.](https://www.pingidentity.com/en/resources/blog/post/refresh-token-rotation-spa.html)

The BFF pattern requires that the a server-side application takes care of authorizing requests from the frontend. It will be an Oauth2 Client 
who can actually keep a secret (secret string code so the authorization server can make sure it's an Oauth2 Client). What differs from Regular Authorization 
Flow where the server exchanges the auhtorization code for access token is that we bind the access token (or ID token if OIDC) to a session cookie 
and all further requests to protected endpoints will be with this session cookie. Here is the flow:

<div align="center">
  <img src="/project_images/oidc_flow.png" alt="oidc">
</div>

More details about BFF are detailed in Chess_Gateway README.

# Tech Stack in more details

Frontend:
   * Vue.js

Backend:

   * MySQL database. Used to save Chess Games and Keycloak tables for user management and other things.
   
   * RabbitMQ Message Broker.
   
   * Keycloak for Authorization Server and User Management.
  
   * Spring: 1) Chess_Gateway. This has Spring Cloud Gateway, Oauth2 Client, Spring Security depenendcies. This is required for BFF pattern which we discussed before. It protects endpoints and serves frontend and chess_manager service. It's similar to a reverse proxy in that scenario but with the addition of Spring Security and Spring Cloud Gateway filters and features.
  
   * Spring; 2) Chess_Manager. This is responsible for WebSocket with STOMP protocol management and RabbitMQ configuration. It is also connected to MySQL database with Spring Data JPA (service + repository layer) to save chess games into the database. It has @RestController with protected endpoints and 1 public endpoint. It is an OAuth2 resource server so it can check if the user can access the protected endpoints. It also has @RestControllerAdvice to return custom status codes based on exceptions thrown.
  
   * Spring: 3) Chess_Backend. This has a ProcessBuilder to connect to the locally installed Stockfish engine and ask the engine for the best move. It is connected to a RabbitMQ queue and is consuming messages. It takes 100 ms to calculate the best move so it can support limited amount of users (i'd estimate 50-100 concurrent users) but since it's consumes RabbitMQ messages from a queue it's easy to horizontally scale the backend service to accomodate more users.

Testing:

* JUnit5 unit tests for validator classes in Chess_Manager.
    
* Mock Tests for @RestControllers and @Valid constraint checks. Also used for @RestControllerAdvice response status tests.

* Integration Tests between JPA with MySQL done with TestContainers which is a real MySQL Database in a container. Which means no incompatibilities between production database and tested one which avoids the pit falls of H2 in memory testing.

**For more detailed information for a particular Spring Project or Frontend please check the corresponding folders.**

# What's next?

* Adding SMPT support so Keycloak can send emails for email verification and password resets.

* Adding comprehensive logging capabilities to the corresponding Spring Projects.

* Adding Prometheus to save the log files into a database.

* Adding Grafana to visualize the data that Prometheus has gathered.

* With Logging, Prometheus and Grafana enabled I can write Selenium tests to check the performance and limitations of Chess_Backend Stockfish engine and in general end-to-end testing for confirmation of feature availability.

# End 
