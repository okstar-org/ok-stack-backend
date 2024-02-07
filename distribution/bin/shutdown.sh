#!/bin/bash

export BASE_DIR=`cd $(dirname $0)/..; pwd`

pid=`ps ax | grep -i 'quarkus-run.jar' | grep $BASE_DIR | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "Not running."
        exit -1;
fi

for p in ${pid[@]} ; do
    kill -9 ${p}
    echo "Send kill signal to ${p}"
done
