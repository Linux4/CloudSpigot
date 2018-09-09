#!/usr/bin/env bash

cp ./CloudSpigot-Server/target/cloudspigot*-SNAPSHOT.jar ./Paperclip/cloudspigot-1.8.9.jar
cp ./work/1.8.9/1.8.9.jar ./Paperclip/minecraft_server.1.8.9.jar
cd ./Paperclip
mvn clean package
cd ..
cp ./Paperclip/assembly/target/paperclip*-full.jar ./Paperclip.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/Paperclip.jar"
