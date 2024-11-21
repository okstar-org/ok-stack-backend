#!/bin/bash
set -e

# Start nginx
/usr/sbin/nginx

# Start ok-stack
exec ${OK_STACK_DIR}/bin/startup.sh -Xmx100M
