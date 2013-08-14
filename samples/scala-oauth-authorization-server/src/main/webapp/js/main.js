define.amd = {jQuery : true};
requirejs.config({
  shim: {
    'jquery': {
      exports: '$'
    }
  },
  paths: {
    'jquery': 'http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min'
  }
});

define(function(require, exports, module) {
  var 
    OAuth = require('../../jso2'),
    jQuery = require('jquery');

  OAuth.enablejQuery($);

  $(document).ready(function() {
    var o = new OAuth('local', {
      client_id: "someclientid",
      redirect_uri: "http://localhost:8002/",
      authorization: "http://localhost:8002/oauth/dialog"
    });

    o.callback();

    o.ajax({
      url: "http://localhost:8002/oauth/something",
      oauth: {
        scopes: {
          request: ["https://www.googleapis.com/auth/userinfo.email"],
          require: ["https://www.googleapis.com/auth/userinfo.email"]
        }
      },
      dataType: 'json',
      success: function(data) {
        console.log("Response (google):");
        console.log(data);
        $(".loader-hideOnLoad").hide();
      }
    });
  });
});