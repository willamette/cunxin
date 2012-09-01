package org.cunxin.app.model.payment.payer;

public class AliPayPayer extends Payer {
    private String _aliPayEmail = "";

    public AliPayPayer(String aliPayEmail) {
        _aliPayEmail = aliPayEmail;
    }

    public String getAliPayEmail() {
        return _aliPayEmail;
    }

    @Override
    String getAccount() {
        return getAliPayEmail();
    }
}
