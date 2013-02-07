#!/usr/bin/env ruby

require 'rubygems'
require 'json'
require 'sinatra'
require 'sinatra/cross_origin'


class PetApi < Sinatra::Base
  register Sinatra::CrossOrigin

  enable :cross_origin

  disable :logging

  set :public_folder, '/public'


  configure :production do
    disable :dump_errors
  end

  # returns the api docs for the resource listing
  get '/api-docs' do
    res = File.read(File.join('public', 'api-docs'))
    body res
    status 200
  end

  # returns the api docs for each path
  get '/api-docs/:api' do
    res = File.read(File.join('public', params[:api].to_s))
    body res
    status 200
  end

  ## root api params
  ##~ a = source2swagger.namespace("pet")
  ##~ a.basePath = "http://localhost:4567"
  ##~ a.swaggerVersion = "1.1"
  ##~ a.apiVersion = "1.0"
  ##~ a = source2swagger.namespace("pet")
  ## endpoints
  ##~ e = a.apis.add
  ## 
  ##~ e.set :path => "/pet/{petId}", :format => "json"
  ##~ e.description = "Access to the sentiment of a given word"
  ##
  ##~ op = e.operations.add   
  ##~ op.responseClass = "Pet"
  ##~ op.set :httpMethod => "GET", :nickname => "getPetById", :deprecated => false
  ##~ op.summary = "Returns a pet based on ID"  
  ##~ op.parameters.add :name => "petId", :description => "ID of the pet that needs to be fetched", :dataType => "string", :allowMultiple => false, :required => true, :paramType => "path"
  ##
  ##  declaring errors for the operation
  ##~ err = op.errorResponses.add 
  ##~ err.set :reason => "pet not found", :code => 404
  ##~ op.errorResponses.add :reason => "Invalid ID supplied", :code => 400
  ##

  get '/:petId' do
    res = {:message => "thanks", :code => 100}
    body res.to_json
    status 200
  end

  ## models
  ##~ a.models["Category"] = {:id => "Category", :properties => {:id => {:type => "long"}, :name => {:type => "string"}}}
  ##~ a.models["Pet"] = {:id => "Pet", :properties => {:id => {:type => "long"}, :category => {:type => "Category"}, :tags => {:type => "Array", :items => {:$ref => "Tag"}}, :status => {:type => "string"}, :name => {:type => "string"}, :photoUrls => {:items => {:type => "string"}, :type => "Array"}}}
  ##~ a.models["Tag"] = {:id => "Category", :properties => {:id => {:type => "long"}, :name => {:type => "string"}}}
  

  not_found do
    ""
  end
  
  error do
    error_code = 500
    error error_code, env['sinatra.error'].to_s 
  end
end

PetApi.run! :port => ARGV[0]