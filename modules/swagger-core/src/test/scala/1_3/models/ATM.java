package models;

import java.util.List;

public class ATM {
    private List<Currency> supportedCurrencies;

    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}