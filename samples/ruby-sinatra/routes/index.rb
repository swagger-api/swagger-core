class MyApp < Sinatra::Application
	get '/' do
  	view :index
	end 
end