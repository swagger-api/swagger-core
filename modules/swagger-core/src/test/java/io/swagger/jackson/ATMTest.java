package io.swagger.jackson;

import io.swagger.converter.ModelConverterContextImpl;
import io.swagger.oas.models.media.Schema;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ATMTest extends SwaggerTestBase {

    @Test
    public void testATMModel() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
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

