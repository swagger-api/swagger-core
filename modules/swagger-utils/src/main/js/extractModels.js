fs = require('fs')
var yaml = require('json2yaml')

var args = process.argv.splice(2);

if(args.length == 0) {
  console.log("No models found!");
  process.exit(1);
}

fs.readFile("out.txt", "utf8", function (err, data) {
  var lines = data.split("\n");
  var data = {};

  var i = 0;
  for(i in lines) {
    var line = lines[i];
    if(line != "") {
      var o = JSON.parse(line);
      data.models = o;
    }
  }
  
  var yml = yaml.stringify(data);
  console.log(yml);
});
