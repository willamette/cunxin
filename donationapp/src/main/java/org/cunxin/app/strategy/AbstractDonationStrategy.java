package org.cunxin.app.strategy;

import org.cunxin.app.model.network.request.AbstractOnlinePaymentRequest;
import org.cunxin.app.model.network.response.OnlinePaymentResponse;

public abstract class AbstractDonationStrategy {
    public abstract OnlinePaymentResponse execute(AbstractOnlinePaymentRequest onlinePaymentRequest);
}
