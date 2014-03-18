scala -cp 'target/*:target/lib/*:../swagger-core/target/*' ModelExporter $@ out.txt

node ./src/main/js/extractModels.js $@