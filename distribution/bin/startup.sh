#!/bin/bash


export BASE_DIR=`cd $(dirname $0)/..; pwd`

cd ${BASE_DIR}

#===========================================================================================
# JVM Configuration
#===========================================================================================
JAVA_OPT="${JAVA_OPT} -server -Xmx128m"

if [ ! -d $BASE_DIR/logs ]; then
  mkdir $BASE_DIR/logs
fi

pid=`ps ax | grep -i 'quarkus-run.jar' | grep $BASE_DIR | grep java | grep -v grep | awk '{print $1}'`
if [ ! -z "$pid" ] ; then
        echo "Is running."
        exit -1;
fi

MODULES=("module-system" "module-org" "module-auth" "module-chat" "module-billing")
for item in "${MODULES[@]}"
do
    echo "The $item is starting."
    nohup java -jar $BASE_DIR/infra/$item/quarkus-run.jar &> $BASE_DIR/logs/$item.out &
    echo "The $item is startup successfully=> [PID=$!, CODE=$?]"
    sleep 8
done

APPS=("app-open")
for item in "${APPS[@]}"
do
    echo "The $item is starting."
    nohup java -jar $BASE_DIR/app/$item/quarkus-run.jar &> $BASE_DIR/logs/$item.out &
    echo "The $item is startup successfully=> [PID=$!, CODE=$?]"
done
