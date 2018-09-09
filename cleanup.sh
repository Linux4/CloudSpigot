#!/bin/bash

mvn clean && rm -f Paperclip/minecraft_server.1.8.9.jar && rm -f Paperclip/cloudspigot-1.8.9.jar && rm -f ./Paperclip.jar && cd Paperclip && mvn clean && cd ..
