#!/bin/bash
set -e

systemctl start nginx

export OK_STACK_HOME="${OK_STACK_DIR}"
exec start-stop-daemon --start --exec "$OK_STACK_HOME/bin/startup.sh"
