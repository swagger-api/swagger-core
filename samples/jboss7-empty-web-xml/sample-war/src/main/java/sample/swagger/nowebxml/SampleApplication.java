package sample.swagger.nowebxml;

import com.wordnik.swagger.jaxrs.config.BeanConfig;
import sample.swagger.nowebxml.resource.LowercaseResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath( "/api" )
public class SampleApplication
   extends Application
{
   public SampleApplication ()
   {
      System.out.println( "Initializing SampleApplication" );
      BeanConfig beanConfig = new BeanConfig();
      beanConfig.setVersion( "1.0" );
      beanConfig.setResourcePackage( LowercaseResource.class.getPackage().getName() );
      beanConfig.setBasePath( "http://localhost:9080/sample/api" );
      beanConfig.setDescription( "Sample RESTful resources" );
      beanConfig.setTitle( "Sample RESTful API" );
      beanConfig.setScan( true );   }
}
