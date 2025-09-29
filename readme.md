https://download.oracle.com/otn-pub/otn_software/jdbc/239/ojdbc11.jar

<dependency>
	<groupId>com.oracle.database.jdbc</groupId>
	<artifactId>ojdbc11</artifactId>
	<version>23.3.0.0</version>
</dependency>
		
mvn install:install-file ^
    -Dfile=C:\libs\ojdbc11.jar ^
    -DgroupId=com.oracle.database.jdbc ^
    -DartifactId=ojdbc11 ^
    -Dversion=23.3.0.0 ^
    -Dpackaging=jar
    
#Call API:
http://localhost:8080/users?status=active&startDate=2023-01-01&endDate=2025-09-30
http://localhost:8080/users/excel?status=active&startDate=2023-01-01&endDate=2025-09-30

http://localhost:8080/users?status=inactive&startDate=2023-01-01&endDate=2025-09-30
http://localhost:8080/users/excel?status=inactive&startDate=2023-01-01&endDate=2025-09-30

#OpenAPI
http://localhost:8080/swagger-ui/index.html