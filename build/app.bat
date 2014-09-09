@echo off

java -Xmx512m -Dlog4j.configuration=file:///java/test//conf/log4j.properties -jar app.jar %1