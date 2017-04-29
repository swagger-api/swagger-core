# Swagger Sinatra Sample App

## Overview
This is an example of [Sinatra](http://www.sinatrarb.com/) hosting a [Swagger UI](https://github.com/wordnik/swagger-ui) instance.  The goal is to support API driven design via [Swagger JSON](https://github.com/wordnik/swagger-spec/blob/master/versions/1.2.md) files.  Although you could theoretically merge this project to document an existing [Sinatra](http://www.sinatrarb.com/) RESTful API, the `ruby-source2swagger` sample might be an easier alternative.

## Running The App

### Installing
This sample utilizes the following gems:
* [Bundler](http://bundler.io/) to manage its dependencies
* [Rack](https://github.com/rack/rack) as its HTTP server (underneath Sinatra).  

You can install these with:
```
gem install bundler
gem install rack
```

Once these are installed, run `bundle install` in the app directory to install [Sinatra](http://www.sinatrarb.com/).

### How To Run
You can run the app from its directory with:
```
rackup
```
__OR__

You can get automatic reloading of changed files by installing the [Rerun](https://github.com/alexch/rerun) gem and doing:
```
rerun 'rackup'
```

Then visit [http://localhost:9292](http://localhost:9292).

## APIs
The API files are located in the `public/api` directory.  To document your own API, edit `index.json` to link to your own JSON files by changing the path attribute (read the file for examples).