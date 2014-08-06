class MyApp < Sinatra::Application
	get '/api/?' do
		view :"api/index"
	end
	get '/api/*/?' do |path|
  	view :"api/#{path}"
	end
end