package org.cunxin.app.model.network.response;

import org.apache.commons.httpclient.Header;

public class OnlinePaymentResponse {

    /**
     * 返回中的Header信息
     */
    private Header[] _responseHeaders;

    /**
     * String类型的result
     */
    private String _stringResult;

    public OnlinePaymentResponse(Header[] responseHeaders, String stringResult) {
        _responseHeaders = responseHeaders;
        _stringResult = stringResult;
    }

    public Header[] getResponseHeaders() {
        return _responseHeaders;
    }

    public String getStringResult() {
        return _stringResult;
    }
}
