#!/bin/bash

JAVAHOME=$JAVA_HOME


echo "--------start--------"

PID=$(ps -ef|grep user-server-1.0.0.jar  |grep -v grep|awk '{print $2}')


if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms512m -Xmn768m -Xmx1024m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC user-server-1.0.0.jar --spring.profiles.active=product --server.port=8700 > user.log 2>&1 &
    echo "--------------start success-----------"
    exit
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms512m -Xmn768m -Xmx1024m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC user-server-1.0.0.jar --spring.profiles.active=product --server.port=8700 > user.log 2>&1 &
    echo "--------------start success-----------"
    exit
fi
