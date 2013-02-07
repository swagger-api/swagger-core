# Swagger Sample App

## Overview
This is an example of using the [source2swagger](https://github.com/solso/source2swagger) swagger generator.  It demonstrates--with some modifications--how to use the source2swagger library to make a set of static JSON files which are swagger compliant.

Instead of having a tight server integration, this technique scans the source code and uses a ruby-based library to 
generate the swagger json files.  It requires that you format your source comments appropriately so the model can be
built.

### To generate the swagger json

Run ./bin/gen-json.sh to run the (modified) source2swagger library.  This sets the input file and output location,
and also writes a api-docs file for the resource listing.  After running this, you should see files in your `public`
directory

### To run the stub server

You can run the sinatra-based server as follows:

```
ruby src/pet_api.rb 4567
```

This will start a server listening on http://localhost:4567.

#### Browsing the docs

With [swagger-ui](https://github.com/wordnik/swagger-ui) by entering http://localhost:4567/api-docs in
the URL.

#### How it works

After generating the json files, the single api class (`pet_api.rb`) has two additional routes.  They are used to
deliver the resource listing and api declarations:

```ruby
  # respond to requests to /api-docs, and serve contents of ./public/api-docs
  get '/api-docs' do
    res = File.read(File.join('public', 'api-docs'))
    body res
    status 200
  end

  # respond to requests to /api-docs/:api (i.e. /api-docs/pet) and serve contents of ./public/:api
  get '/api-docs/:api' do
    res = File.read(File.join('public', params[:api].to_s))
    body res
    status 200
  end
```

Note!  These paths (public, api-docs, api-docs/:api) are configured in the bin/source2swagger script!  If you don't like
the way it's configured, start there.
