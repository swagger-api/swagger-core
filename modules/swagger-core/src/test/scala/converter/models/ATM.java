package converter.model;

import java.util.List;

public class ATM {
    private List<Currency> supportedCurrencies;

    private Currency currency;

    public void setCurrency(Currency currency) {
      this.currency = currency;
    }

    public Currency getCurrency() {
      return currency;
    }
}