#!/bin/bash


export BASE_DIR=`cd $(dirname $0)/..; pwd`
echo "WorkDir:${BASE_DIR}"
cd ${BASE_DIR}

/etc/init.d/nginx start
echo "Start nginx=>$?"

daemon=0
# 使用 getopts 解析选项
while getopts "d:" opt; do
  case $opt in
    d)
      daemon=1
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
  esac
done

JAVA_OPT="${JAVA_OPT} -server -Xmx128m"
if [ ! -d $BASE_DIR/logs ]; then
  mkdir $BASE_DIR/logs
fi

# allow arguments to be passed to openfire launch
if [[ ${1:0:1} = '-' ]]; then
  EXTRA_ARGS="$@"
  set --
fi

pid=`ps ax | grep -i 'quarkus-run.jar' | grep $BASE_DIR | grep java | grep -v grep | awk '{print $1}'`
if [ ! -z "$pid" ] ; then
        echo "Is running."
        exit -1;
fi

PIDS=()
MODULES=("module-system" "module-org" "module-auth" "module-chat" "module-billing")
for item in "${MODULES[@]}"
do
#  start-stop-daemon --start --background --exec /path/to/daemon --pidfile /var/run/daemon.pid
    echo "The $item is starting."
    nohup java -jar $BASE_DIR/infra/$item/quarkus-run.jar ${EXTRA_ARGS} &
    PID=$!
    PIDS+="$PID "
    echo "The $item is startup successfully=>[$PID]"
    sleep 8
done

APPS=("app-open")
for item in "${APPS[@]}"
do
    echo "The $item is starting."
    nohup java -jar $BASE_DIR/app/$item/quarkus-run.jar ${EXTRA_ARGS} &
    PID=$!
    PIDS+="$PID "
    echo "The $item is startup successfully=>[$PID]"
done

echo "Wait for process: ${PIDS[@]}"
wait -p -n ${PIDS[@]}

echo "OkStack Server has been exit."