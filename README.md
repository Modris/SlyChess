# SlyChess Project Overview.

<div align="center">
  <img src="/project_images/slychess_technical_overview.png" alt="overview">
</div>

* Play Chess against Stockfish engine in realtime with WebSockets

* The project is deployed on Virtual Private Server (VPS) with Docker-Compose. Launch locally with docker-compose file in Deployment folder.

* RabbitMQ Message Broker for Message durability, reliability and acknowledgements.

* OpenID Connect (OIDC) which extends Oauth2 authorization flow with Keycloak Authorization Server.

* User Management, Registration by Keycloak. SMPT email to reset password coming soon.

* Saving games into MySQL Database for logged users. Oauth2 Resource Server Chess_Manager decides that.

* Backend-For-Frontend pattern to serve the frontend with Authorization without exposing JWT (Json Web Token). More about that below.

# Project Images

<div align="center">
  <img src="/project_images/main.png" alt="main">
  <img src="/project_images/history.png" alt="history">
  <img src="/project_images/login.png" alt="login">
</div>

# Tech Stack in more details

Frontend:
   * Vue.js

Backend:

   * MySQL database. Used to save Chess Games and Keycloak tables for user management and other things.
   
   * RabbitMQ message broker.
   
   * Keycloak Authorization Server (Also used for User management, registration besides Oauth2 flow).
  
   * Spring: 1) chess_gateway. This has Spring Cloud Gateway, Oauth2 Client, Spring Security depenendcies. This is required for BFF pattern in Oauth2 (more about that below the tech stack). It protects endpoints and serves frontend and chess_manager service. It's similar to a reverse proxy in that scenario. 
  
   * Spring; 2) chess_manager. This is responsible for WebSocket with STOMP protocol management and RabbitMQ configuration. It is also connected to MySQL database with Spring Data JPA (service + repository layer) to save chess games into the database. It has @RestController with protected endpoints and 1 public endpoint. It is an OAuth2 resource server so it can check if the user can access the protected endpoints. It also has @RestControllerAdvice to return custom status codes based on exceptions thrown.
  
   * Spring: 3) chess_backend. This has a ProcessBuilder to connect to the locally installed Stockfish engine and ask the engine for the best move. It is connected to a RabbitMQ queue and is consuming messages. It takes 100 ms to calculate the best move so it can support limited amount of users (i'd estimate 50-100 concurrent users) but since it's consumes RabbitMQ messages from a queue it's easy to horizontally scale the backend service to accomodate more users.

Testing:

    JUnit5 unit tests for validator classes in Chess_Manager.
    Mock Tests for @RestControllers and @Valid constraint checks. Also used for @RestControllerAdvice response status tests.
    Integration Tests between JPA with MySQL done with TestContainers (real MySQL Database in a container. Which means no incompatibilities between production database and tested one (pit falls of H2 in memory testing)).

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

More details in Chess_Gateway Folder README.

# End
