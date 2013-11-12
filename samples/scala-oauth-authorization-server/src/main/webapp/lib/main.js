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
		OAuth = require('.jso2'),
		jQuery = require('jquery');

	OAuth.enablejQuery($);

	$(document).ready(function() {

		var o = new OAuth('google', {
			client_id: "541950296471.apps.googleusercontent.com",
			redirect_uri: "http://bridge.uninett.no/jso/index.html",
			authorization: "https://accounts.google.com/o/oauth2/auth"
		});

		o.callback();

		o.ajax({
			url: "https://www.googleapis.com/oauth2/v1/userinfo",
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
