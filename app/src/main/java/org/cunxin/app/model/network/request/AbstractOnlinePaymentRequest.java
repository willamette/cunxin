package org.cunxin.app.model.network.request;

import org.apache.commons.httpclient.NameValuePair;
import org.cunxin.app.model.payment.donation.Donation;

public interface AbstractOnlinePaymentRequest {

    public String getUrl();

    public NameValuePair[] getParameters();

    public Donation getDonation();

    public abstract void parseParamsNameValuePairs();
}
