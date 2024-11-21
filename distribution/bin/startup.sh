#!/bin/bash


function DoRunJava() {
      jar=$1
      JAVA_OPT=$2
      echo "The $item is starting..."
      cmd="java -jar ${jar} -server ${JAVA_OPT}"
      nohup $cmd &>/dev/null &
      return $?
}

export BASE_DIR=`cd $(dirname $0)/..; pwd`
echo "WorkDir:${BASE_DIR}"
cd ${BASE_DIR}

EXTRA_ARGS="$@"
JAVA_OPT=""

if [ ! -d $BASE_DIR/logs ]; then
  mkdir $BASE_DIR/logs
fi

JAVA_OPT="${JAVA_OPT} ${EXTRA_ARGS}"

pid=`ps ax | grep -i 'quarkus-run.jar' | grep $BASE_DIR | grep java | grep -v grep | awk '{print $1}'`
if [ ! -z "$pid" ] ; then
        echo "Is running."
        exit 1;
fi

PIDS=()
MODULES=("module-bus" "module-system" "module-org" "module-chat" "module-billing" "module-work" "module-auth")
for item in ${MODULES[@]}
do
#  start-stop-daemon --start --background --exec /path/to/daemon --pidfile /var/run/daemon.pid
  DoRunJava $BASE_DIR/infra/$item/quarkus-run.jar ${JAVA_OPT}
  if [ $? -lt 1 ]; then
    echo "The $item is startup failed."
  else
    PID=$!
    PIDS+="$PID "
    echo "The $item is startup successfully=>[$PID]"
  fi
done

APPS=("app-open")
for item in ${APPS[@]}
do
    echo "The $item is starting."
    DoRunJava $BASE_DIR/app/$item/quarkus-run.jar ${JAVA_OPT}
    PID=$!
    PIDS+="$PID "
    echo "The $item is startup successfully=>[$PID]"
done

echo "OkStack process pid is: ${PIDS[@]}"

for pid in ${PIDS[@]} ; do
  echo "Wait for process $pid"
  wait $pid
  echo "Process:$pid is exiting."
done

echo "OkStack Server has been exit."
