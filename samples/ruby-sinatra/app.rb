require 'sinatra'

class MyApp < Sinatra::Application 
end

require_relative 'helpers/init'
require_relative 'routes/init'