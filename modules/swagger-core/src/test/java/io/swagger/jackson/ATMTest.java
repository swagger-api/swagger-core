package io.swagger.jackson;

import static org.testng.Assert.assertNotNull;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.models.Model;

import org.testng.annotations.Test;

public class ATMTest extends SwaggerTestBase {

    @Test
    public void testATMModel() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Model model = context
                .resolve(ATM.class);
        assertNotNull(model);
    }

    public enum Currency {USA, CANADA}

    static class ATM {
        private Currency currency;

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }
    }
}

