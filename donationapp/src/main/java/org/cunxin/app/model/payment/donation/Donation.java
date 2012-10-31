package org.cunxin.app.model.payment.donation;

import org.cunxin.app.model.payment.Payment;
import org.cunxin.app.model.payment.payee.Payee;
import org.cunxin.app.model.payment.payer.Payer;

public interface Donation {

    public Payer getPayer();

    public Payee getPayee();

    public Payment getPayment();
}
