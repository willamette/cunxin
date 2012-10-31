package org.cunxin.app.model.payment;

import org.codehaus.jackson.annotate.JsonProperty;

public class Payment {
    @JsonProperty("amount")
    private double _amount;
    @JsonProperty("currencyCode")
    private String _currencyCode;

    public Payment(double amount, String currencyCode) {
        _amount = amount;
        _currencyCode = currencyCode;
    }

    public double getAmount() {
        return _amount;
    }

    public String getCurrencyCode() {
        return _currencyCode;
    }
}
