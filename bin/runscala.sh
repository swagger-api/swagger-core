#!/bin/bash
echo "" > classpath.txt
for file in `ls lib`;
        do echo -n 'lib/' >> classpath.txt;
        echo -n $file >> classpath.txt;
        echo -n ':' >> classpath.txt;
done
for file in `ls build`;
	do echo -n 'build/' >> classpath.txt;
	echo -n $file >> classpath.txt;
	echo -n ':' >> classpath.txt;
done

export CLASSPATH=$(cat classpath.txt)
export JAVA_OPTS="${JAVA_OPTS} -Xmx4096M -DloggerPath=conf/log4j.properties"
scala $JAVA_CONFIG_OPTIONS  -cp $CLASSPATH "$@"
