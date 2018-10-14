CloudSpigot [![Build Status](https://ci.server24-7.eu/job/CloudSpigot/badge/icon)](https://ci.server24-7.eu/job/CloudSpigot/) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/9510a76cbf1f4fa3a192ee3e56050082)](https://www.codacy.com/app/Server24-7/CloudSpigot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Server24-7/CloudSpigot&amp;utm_campaign=Badge_Grade)  
===========

High performance PaperSpigot fork used by Minecraft Server Cloudz.ml.


**Support and Project Discussion:**
 - [Discord](https://discord.gg/5qp26hf)


How To (Server Admins)
------
CloudSpigot is a jar file that you can download and run just like a normal jar file.

Download a copy of cloudspigot.jar from [our build server, here](https://ci.server24-7.eu/job/CloudSpigot/).

Run the CloudSpigot jar directly from your server. Just like old times

  * For a sneak peak on upcoming features, [see here](https://github.com/Server24-7/CloudSpigot/projects)

How To (Plugin Developers)
------
 * Maven Repo (for cloudspigot):
```xml
<repository>
    <id>server24-7.eu</id>
    <url>https://mvn.server24-7.eu/</url>
</repository>
```
 * Artifact Information:
```xml
<dependency>
    <groupId>eu.server24-7</groupId>
    <artifactId>cloudspigot</artifactId>
    <version>1.8.10-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
 </dependency>
 ```

How To (Compiling Jar From Source)
------
To compile CloudSpigot, you need JDK 8, maven, and an internet connection.

Clone this repo, run `./build.sh` from *bash*, get files.

How To (Pull Request)
------
See [Contributing](CONTRIBUTING.md)

