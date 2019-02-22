FROM openjdk:8-jre

ADD ./build/libs/*-SNAPSHOT-boot.jar app.jar

ENV xms=1g

ENV xmx=1g

ENTRYPOINT exec java -server -Xms$xms -Xmx$xmx -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -jar app.jar