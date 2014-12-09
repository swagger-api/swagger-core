package com.fasterxml.jackson.module.swagger;

import com.wordnik.swagger.jackson.*;
import com.wordnik.swagger.models.*;

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
    Model model = new ModelResolver(mapper())
       .resolve(ATM.class);
    assertNotNull(model);
    /*
    prettyPrint(model);
    */
  }
}

