package org.cunxin.api.adapter;

import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;
import org.cunxin.api.model.CunxinDonationRequest;
import org.cunxin.app.dao.CunxinDonationDao;
import org.cunxin.app.model.network.request.AliPayOnlinePaymentRequest;
import org.cunxin.app.model.network.response.CunxinDonationResponse;
import org.cunxin.app.model.network.response.OnlinePaymentResponse;
import org.cunxin.app.model.payment.Payment;
import org.cunxin.app.model.payment.donation.AliPayDonation;
import org.cunxin.app.model.payment.payee.AliPayPayee;
import org.cunxin.app.model.payment.payer.AliPayPayer;
import org.cunxin.app.service.CunxinDonationService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

public class AliPayDonationAdapter extends AbstractDonationAdapter {

    @Inject
    public AliPayDonationAdapter(CunxinDonationService cunxinDonationService, CunxinDonationDao cunxinDonationDao) {
        super(cunxinDonationService, cunxinDonationDao);
    }

    @Override
    @POST
    @Timed
    @Path("/alipay")
    protected CunxinDonationResponse doProcess(CunxinDonationRequest request) {
        AliPayPayee aliPayPayee = _cunxinDonationDao.findAliPayPayeeByEmail(request.getPayeeEmail());
        if (aliPayPayee == null) {
            //TODO:
        }
        if (aliPayPayee.isFilled()) {
            //TODO:
        }
        AliPayPayer aliPayPayer = new AliPayPayer(request.getPayerEmail());
        Payment payment = new Payment(request.getDonationAmount(), request.getCurrencyCode());
        AliPayDonation donation = new AliPayDonation(aliPayPayer, aliPayPayee, payment);
        AliPayOnlinePaymentRequest aliPayRequest = new AliPayOnlinePaymentRequest(donation);
        OnlinePaymentResponse onlinePaymentResponse = _cunxinDonationService.doProcess(aliPayRequest);
        CunxinDonationResponse response = parseReponse(onlinePaymentResponse);
        return response;
    }

    private CunxinDonationResponse parseReponse(OnlinePaymentResponse onlinePaymentResponse) {
        return null;
    }
}
