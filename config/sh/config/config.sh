#!/bin/bash

JAVAHOME=$JAVA_HOME


echo "--------start--------"

PID=$(ps -ef|grep config-server-1.0.0.jar  |grep -v grep|awk '{print $2}')


if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC config-server-1.0.0.jar --server.port=8500 > config.log 2>&1 &
    echo "--------------start success-----------"
    exit
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC config-server-1.0.0.jar --server.port=8500 > config.log 2>&1 &
    echo "--------------start success-----------"
    exit
fi
