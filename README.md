# ticket_management_app

Hi,

https://github.com/user-attachments/assets/1564505e-ccb0-48bc-8d57-742dfc58eee0

IT support account.<br /> 
This video showcases the various actions an IT support specialist can perform, including creating a ticket, 
viewing all tickets, changing status, adding comments, 
and filtering tickets by ID or status.

Employee account.<br /> 
This video highlights the various actions an employee can perform, such as creating a ticket, 
viewing their own tickets, and filtering by ID or status.

under resources you can find sql script for database.

# client jar
the jar file of swing client application is contains in path swing-api-client/target/swing-api-client-1.0.jar.<br /> 
java -jar swing-api-client-1.0.jar (to run the application).

# deployment
first we need to create a image for oracle database, please follow this step to create an image. <br />
**git clone https://github.com/oracle/docker-images.git** (clone oracle/docker-images: repository).<br />
download oracle for lunix system from official website, in this application we use 19.3.0 version so download file name should be LINUX.X64_193000_db_home.zip.<br />
put the download file in path docker-images\OracleDatabase\SingleInstance\dockerfiles\19.3.0 (note: we use 19.3.0 that why put in the path 19.3.0).<br />
in path **docker-images\OracleDatabase\SingleInstance\dockerfiles** you will find a script **buildContainerImage**, we will use it create oracle database image.<br />
**./buildContainerImage.sh -e -v 19.3.0 -t oracle-db:1.0. (please run this command with this name)**.<br />
**docker build --platform linux/amd64 -t ticket_support_app**. (create image for our application, please make sure that you are in application path) <br />
**docker-compose.yml up** (run docker compose to create our network and containers) <br/>
**before start using client application you need to execute the init.sql script, to create schema of database**. <br/>


