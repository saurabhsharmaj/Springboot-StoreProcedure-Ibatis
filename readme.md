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