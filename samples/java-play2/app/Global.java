import play.*;

import com.wordnik.swagger.model.*;
import com.wordnik.swagger.config.*;
import com.wordnik.swagger.config.FilterFactory;

public class Global extends GlobalSettings {
  @Override
  public void onStart(Application app) {
    // do any additional initialization here, such as set your base path programmatically as such:
    // ConfigFactory.config().setBasePath("http://www.foo.com/");

    ApiInfo info = new ApiInfo(
      "Swagger Sample App",                             /* title */
      "This is a sample server Petstore server.  You can find out more about Swagger " + 
      "at <a href=\"http://swagger.wordnik.com\">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.  For this sample, " + 
      "you can use the api key \"special-key\" to test the authorization filters", 
      "http://helloreverb.com/terms/",                  /* TOS URL */
      "apiteam@wordnik.com",                            /* Contact */
      "Apache 2.0",                                     /* license */
      "http://www.apache.org/licenses/LICENSE-2.0.html" /* license URL */
    );
    ConfigFactory.config().setApiInfo(info);
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }   
}