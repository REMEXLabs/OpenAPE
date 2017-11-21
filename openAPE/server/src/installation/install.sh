sh ../../bin/shutdown.sh
rm -r openape
rm logs/*
mkdir openape
mv openape.war openape/openape.war
cd openape
jar xvf openape.war
cd ..
cp installationfiles/log4j.xml openape/log4j.xml
cp installationfiles/log4j.xml openape/WEB-INF/classes/log4j.xml
cp installationfiles/mongo.properties openape/config/mongo.properties
cp installationfiles/mongo.properties openape/WEB-INF/classes/config/mongo.properties
mv openape/openape.war openape.war
#rm ../../logs/*
 sh ../../bin/startup.sh
