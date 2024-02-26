# Launching locally or in production requires Docker Compose and editing host file.

If you want to run this program, then the easiest way is to use docker-compose file in the localhost_only folder.

Then add all you need to do is add an entry to your hosts file:

    Windows: C:\Windows\System32\drivers\etc\hosts
    Linux: /etc/hosts

Append this to the end of the file: 127.0.0.1 keycloak

This is a **KNOWN** Keycloak issue and there seems to be no better way. [Stackoverflow thread for more info.](https://stackoverflow.com/questions/57213611/keycloak-and-spring-boot-web-app-in-dockerized-environment).

# I don't want to use Docker Compose. Localhost only. Is there another way?

Well you still need MySQL, RabbitMQ, Keycloak up and running. Afterwards change the URL's to your exposed ports e.g. localhost:8080 
instead of keycloak:8080 in the Chess_Gateway, Chess_Backend,Chess_Manager and Chess_Frontend URL's.

