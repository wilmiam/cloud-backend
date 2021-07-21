#!/bin/bash

JAVAHOME=$JAVA_HOME


echo "--------start--------"

PID=$(ps -ef|grep file-server-1.0.0.jar  |grep -v grep|awk '{print $2}')


if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC file-server-1.0.0.jar --spring.profiles.active=product --server.port=8200 > gateway.log 2>&1 &
    echo "--------------start success-----------"
    exit
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC file-server-1.0.0.jar --spring.profiles.active=product --server.port=8200 > gateway.log 2>&1 &
    echo "--------------start success-----------"
    exit
fi
