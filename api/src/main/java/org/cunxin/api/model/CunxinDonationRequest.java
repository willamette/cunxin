package org.cunxin.api.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class CunxinDonationRequest {

    @JsonProperty("payeeEmail")
    private String _payeeEmail;

    @JsonProperty("payerEmail")
    private String _payerEmail;

    @JsonProperty("donationAmount")
    private double _donationAmount;

    @JsonProperty("currencyCode")
    private String _currencyCode;

    public CunxinDonationRequest(String payeeEmail, String payerEmail, double donationAmount, String currencyCode) {
        _payeeEmail = payeeEmail;
        _payerEmail = payerEmail;
        _donationAmount = donationAmount;
        _currencyCode = currencyCode;
    }

    public String getPayeeEmail() {
        return _payeeEmail;
    }

    public String getPayerEmail() {
        return _payerEmail;
    }

    public double getDonationAmount() {
        return _donationAmount;
    }

    public String getCurrencyCode() {
        return _currencyCode;
    }
}
