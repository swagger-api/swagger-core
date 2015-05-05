#!/bin/bash

PORT=9999

if [[ $# -eq 1 ]] ; then
	PORT=${1}
fi

mvn package jetty:run -Djetty.port=${PORT}