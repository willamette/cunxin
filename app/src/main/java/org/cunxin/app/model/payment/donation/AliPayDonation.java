package org.cunxin.app.model.payment.donation;


import org.cunxin.app.model.payment.Payment;
import org.cunxin.app.model.payment.payee.AliPayPayee;
import org.cunxin.app.model.payment.payee.Payee;
import org.cunxin.app.model.payment.payer.AliPayPayer;
import org.cunxin.app.model.payment.payer.Payer;

public class AliPayDonation implements Donation {

    private AliPayPayer _aliPayPayer;
    private AliPayPayee _aliPayPayee;
    private Payment _payment;

    public AliPayDonation(AliPayPayer aliPayPayer, AliPayPayee aliPayPayee, Payment payment) {
        _aliPayPayer = aliPayPayer;
        _aliPayPayee = aliPayPayee;
        _payment = payment;
    }

    public AliPayPayee getAliPayPayee() {
        return _aliPayPayee;
    }

    public AliPayPayer getAliPayPayer() {
        return _aliPayPayer;
    }

    @Override
    public Payer getPayer() {
        return getAliPayPayer();
    }

    @Override
    public Payee getPayee() {
        return getAliPayPayee();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Payment getPayment() {
        return _payment;
    }
}
