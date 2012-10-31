package org.cunxin.app.service;

import com.google.inject.Inject;
import org.cunxin.app.model.network.request.AbstractOnlinePaymentRequest;
import org.cunxin.app.model.network.response.OnlinePaymentResponse;
import org.cunxin.app.strategy.AbstractDonationStrategy;

public class CunxinDonationService {

    private AbstractDonationStrategy _donationStrategy;

    @Inject
    public CunxinDonationService(AbstractDonationStrategy donationStrategy) {
        _donationStrategy = donationStrategy;
    }

    public OnlinePaymentResponse doProcess(AbstractOnlinePaymentRequest request) {
        return _donationStrategy.execute(request);
    }
}
