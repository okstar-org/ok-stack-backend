#!/bin/bash
set -e

/usr/sbin/nginx
exec start-stop-daemon --start --exec "${OK_STACK_DIR}/bin/startup.sh -Xmx100M"
