#!/bin/bash

echo "Rebuilding Webmenus..."
mvn clean install -U
echo "Stopping Tomcat..."
sudo systemctl stop tomcat9
sudo bash -c "systemctl stop tomcat9"
echo "Copying WAR file..."
sudo bash -c "cp target/webmenus-1.0-SNAPSHOT.war /var/lib/tomcat9/webapps/webmenus.war"
echo "Restarting Tomcat..."
sudo bash -c "systemctl start tomcat9"
