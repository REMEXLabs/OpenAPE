sh ../../bin/shutdown.sh

mv openape-server.war openape-server/openape-server.war
cd openape-server
jar xvf openape-server.war
cd ..
cp installationfiles/log4j.xml openape-server/log4j.xml
cp installationfiles/log4j.xml openape-server/WEB-INF/classes/log4j.xml
cp installationfiles/mongo.properties openape-server/config/mongo.properties
cp installationfiles/mongo.properties openape-server/WEB-INF/classes/config/mongo.properties
mv openape-server/openape-server.war openape-server.war
rm ../../logs/*
 sh ../../bin/startup.sh
