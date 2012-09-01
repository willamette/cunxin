package org.cunxin.app.model.network.request;

import org.apache.commons.httpclient.NameValuePair;
import org.codehaus.jackson.annotate.JsonProperty;
import org.cunxin.app.alipay.config.AliPayConfig;
import org.cunxin.app.alipay.util.AliPayCore;
import org.cunxin.app.model.payment.donation.AliPayDonation;
import org.cunxin.app.model.payment.donation.Donation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AliPayOnlinePaymentRequest implements AbstractOnlinePaymentRequest {

    private NameValuePair[] _parameters = null;

    @JsonProperty("donation")
    private AliPayDonation _donation;

    public AliPayOnlinePaymentRequest(AliPayDonation donation) {
        this._donation = donation;
    }

    public void parseParamsNameValuePairs() {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", _donation.getAliPayPayee().getPartnerId());
        sParaTemp.put("return_url", _donation.getAliPayPayee().getReturnUrl());
        sParaTemp.put("notify_url", _donation.getAliPayPayee().getNotifyUrl());
        sParaTemp.put("seller_email", _donation.getPayee().getPayeeAccount());
        sParaTemp.put("_input_charset", AliPayConfig.CHARSET);

        String mysign = AliPayCore.buildMysign(sParaTemp, _donation.getAliPayPayee().getKey());
        //签名结果与签名方式加入请求提交参数组中
        sParaTemp.put("sign", mysign);
        sParaTemp.put("sign_type", AliPayConfig.SIGN_TYPE);
        _parameters = new NameValuePair[sParaTemp.size()];
        Iterator<String> keyItr = sParaTemp.keySet().iterator();
        int i = 0;
        while (keyItr.hasNext()) {
            String key = keyItr.next();
            _parameters[i++] = new NameValuePair(key, sParaTemp.get(key));
        }
    }

    @Override
    public String getUrl() {
        return AliPayConfig.ALIPAY_GATEWAY_NEW;
    }

    @Override
    public NameValuePair[] getParameters() {
        if (_parameters == null) parseParamsNameValuePairs();
        return _parameters;
    }

    @Override
    public Donation getDonation() {
        return _donation;
    }
}
