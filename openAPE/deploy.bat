call mvn clean package -DskipTests
rd ..\..\..\..\..\..\apache8\index-apache-tomcat-8.0.3\logs /s /q
md ..\..\..\..\..\..\apache8\index-apache-tomcat-8.0.3\logs 

rd ..\..\..\..\..\..\apache8\index-apache-tomcat-8.0.3\webapps\ROOT /s /q
del ..\..\..\..\..\..\apache8\index-apache-tomcat-8.0.3\webapps\ROOT.war
copy server\target\server-1.1-SNAPSHOT.war ..\..\..\..\..\..\apache8\index-apache-tomcat-8.0.3\webapps\ROOT.war