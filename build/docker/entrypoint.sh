#!/bin/bash
set -e


export OK_STACK_HOME="${OK_STACK_DIR}"
#sh $OK_STACK_HOME/bin/startup.sh

# --chuid ${OK_STAR_USER}:${OK_STAR_USER} \
exec start-stop-daemon --start --exec "$OK_STACK_HOME/bin/startup.sh"
