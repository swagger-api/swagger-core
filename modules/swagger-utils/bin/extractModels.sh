#!/usr/bin/env bash
echo "" > classpath.txt
for file in `ls target/lib`;
        do echo -n 'target/lib/' >> classpath.txt;
        echo -n $file >> classpath.txt;
        echo -n ':' >> classpath.txt;
done
for file in `ls target`;
        do echo -n 'target/' >> classpath.txt;
        echo -n $file >> classpath.txt;
        echo -n ':' >> classpath.txt;
done
echo -n ':' >> classpath.txt;
echo -n $2 >> classpath.txt;
export CLASSPATH=$(cat classpath.txt)

scala -cp $CLASSPATH ModelExporter $1 out.txt

node ./src/main/js/extractModels.js $@ > output.yml

cat output.yml
