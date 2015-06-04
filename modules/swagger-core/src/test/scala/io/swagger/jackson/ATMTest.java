package io.swagger.jackson;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;

public class ATMTest extends SwaggerTestBase {
  static class ATM {
    private Currency currency;

    public void setCurrency(Currency currency) {
      this.currency = currency;
    }

    public Currency getCurrency() {
      return currency;
    }
  }

  public enum Currency { USA, CANADA }

  public void testATMModel() throws Exception {
    
	ModelResolver modelResolver = new ModelResolver(mapper());
	   ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);
	
	Model model = context
       .resolve(ATM.class);
    assertNotNull(model);
    /*
    prettyPrint(model);
    */
  }
}

