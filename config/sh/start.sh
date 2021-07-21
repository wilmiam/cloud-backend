#!/bin/bash

JAVAHOME=$JAVA_HOME

echo "--------eureka start--------"

cd /home/cloud/eureka
PID=$(ps -ef|grep eureka-server-1.0.0.jar  |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC eureka-server-1.0.0.jar --server.port=9900 > eureka.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC eureka-server-1.0.0.jar --server.port=9900 > eureka.log 2>&1 &
    echo "--------------start success-----------"
fi

sleep 20s
cd /home/cloud/config
echo "--------config start--------"

PID=$(ps -ef|grep config-server-1.0.0.jar  |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC config-server-1.0.0.jar --server.port=8500 > config.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC config-server-1.0.0.jar --server.port=8500 > config.log 2>&1 &
    echo "--------------start success-----------"
fi

sleep 30s
cd /home/cloud/gateway
echo "--------gateway start--------"

PID=$(ps -ef|grep gateway-server-1.0.0.jar |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC gateway-server-1.0.0.jar --spring.profiles.active=product --server.port=8999 > gateway.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC gateway-server-1.0.0.jar --spring.profiles.active=product --server.port=8999 > gateway.log 2>&1 &
    echo "--------------start success-----------"
fi

sleep 2s
cd /home/cloud/sys
echo "--------sys start--------"

PID=$(ps -ef|grep sys-server-1.0.0.jar  |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC sys-server-1.0.0.jar --spring.profiles.active=product --server.port=8666 > sys.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC sys-server-1.0.0.jar --spring.profiles.active=product --server.port=8666 > sys.log 2>&1 &
    echo "--------------start success-----------"
fi

sleep 2s
cd /home/cloud/api
echo "--------api start--------"

PID=$(ps -ef|grep api-server-1.0.0.jar  |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC api-server-1.0.0.jar --spring.profiles.active=product --server.port=8100 > api.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC api-server-1.0.0.jar --spring.profiles.active=product --server.port=8100 > api.log 2>&1 &
    echo "--------------start success-----------"
fi

sleep 2s
cd /home/cloud/file
echo "--------file start--------"

PID=$(ps -ef|grep file-server-1.0.0.jar  |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC file-server-1.0.0.jar --spring.profiles.active=product --server.port=8200 > file.log 2>&1 &
    echo "--------------start success-----------"
else
    kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -Xms256m -Xmn384m -Xmx512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC file-server-1.0.0.jar --spring.profiles.active=product --server.port=8200 > file.log 2>&1 &
    echo "--------------start success-----------"
fi


sleep 2s
cd /home/cloud/user
echo "--------user start--------"

PID=$(ps -ef|grep user-server-1.0.0.jar |grep -v grep|awk '{print $2}')
if [ ! $PID ]; then
    nohup java -jar -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms512m -Xmn768m -Xmx1024m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC user-server-1.0.0.jar --spring.profiles.active=product --server.port=8700 > user.log 2>&1 &
    echo "--------------start success-----------"
else
 kill -9 ${PID}
    echo "--------------kill success-----------"
    echo "---------------now wait start-------"
    nohup java -jar -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Xms512m -Xmn768m -Xmx1024m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC user-server-1.0.0.jar --spring.profiles.active=product --server.port=8700 > user.log 2>&1 &
    echo "--------------start success-----------"
fi