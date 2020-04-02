## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is simple web application, that allows logged user to create their own advert/offer. It is allowed to create new user account. Each user can create one offer. User can edit or delete their offer. User with authority set to ROLE_ADMIN is allowed to open administration panel and display all registerd users. 
	
## Technologies
Project is created with:
* Spring framework
* Spring Security
* Hibernate
* Junit
* Maven
* Works with MySql database.
* runs on Tomcat 
	
## Setup
To run this project no your local tomcat server: 
1. Run `mvn package`
2. Deploy on tomcat webapp directory
3. Restart tomcat server
4. Accessed in web browser

###You need will:
* Maven
* Tomcat (with mysql driver in lib)
* MySQL server

###MySQL database config:
Hibernate will automaticly create needed tables.
To let JNDI-lookup create Resource on Tomcat server in context.xml:
```
	<Resource name="jdbc/AppDB" auth="Container"
		type="javax.sql.DataSource"
		factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
		maxTotal="100" maxIdle="30" maxWaitMillis="10000" username="[USER]"
		password="[PASSWORD]" driverClassName="com.mysql.cj.jdbc.Driver"
		url="jdbc:mysql://localhost:3306/OffersAppDB?useTimezone=true&amp;createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=UTF-8" />

```









