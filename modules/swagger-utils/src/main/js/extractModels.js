fs = require('fs')
var yaml = require('json2yaml')

var args = process.argv.splice(2);

if(args.length == 0) {
  console.log("nothing");
  process.exit(1);
}

fs.readFile("out.txt", "utf8", function (err, data) {
  var lines = data.split("\n");
  var models = {};

  var i = 0;
  for(i in lines) {
    var line = lines[i];
    if(line != "") {
      var o = JSON.parse(line);
      models[o.id] = o;
    }
  }
  
  var data = {
    Models: models
  };

  var yml = yaml.stringify(data);
  console.log(yml);
});

