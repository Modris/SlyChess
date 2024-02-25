**Production YAML is not Plug and Play**. Use Localhost for that. This is production level deployment in Linux VPS.  It won't run as easily as localhost version. First i won't share my private 
secret .env variables for my VPS. So you would have to launch keycloak locally, export realm.json file with your configurations
and so on for other variables.

It requires .env file. **Change** dummy values in .env file to your configured values.

The first time initialization will be long due Keycloak Authorization Server creating lots of tables in MySQL.
It takes around 1-2 minutes. Afterwards it should launch in about 20-30 seconds.

All images are on my docker hub so you will be able to pull them. 

