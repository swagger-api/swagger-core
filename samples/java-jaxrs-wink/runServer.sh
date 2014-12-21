#!/bin/bash

PORT=9999

if [[ $# -eq 1 ]] ; then
	PORT=${1}
fi

mvn compile jetty:run -Djetty.port=${PORT}


