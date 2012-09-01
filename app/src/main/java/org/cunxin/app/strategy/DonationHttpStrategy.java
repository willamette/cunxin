package org.cunxin.app.strategy;

import com.google.inject.Inject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.cunxin.app.model.network.request.AbstractOnlinePaymentRequest;
import org.cunxin.app.model.network.response.OnlinePaymentResponse;

/* *
*类名：DonationHttpStrategy
*功能：HttpClient方式访问
*详细：获取远程HTTP数据
*版本：3.2
*日期：2011-03-17
*/

public class DonationHttpStrategy extends AbstractDonationStrategy {


    private static String DEFAULT_CHARSET = "UTF-8";

    /**
     * 连接超时时间，由bean factory设置，缺省为8秒钟
     */
    private int defaultConnectionTimeout = 8000;

    /**
     * 回应超时时间, 由bean factory设置，缺省为30秒钟
     */
    private int defaultSoTimeout = 30000;

    /**
     * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟
     */
    private int defaultIdleConnTimeout = 60000;

    private int defaultMaxConnPerHost = 30;

    private int defaultMaxTotalConn = 80;

    /**
     * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
     */
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private HttpConnectionManager connectionManager;


    @Inject()
    public DonationHttpStrategy() {
        // 创建一个线程安全的HTTP连接池
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
        connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);

        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);

        ict.start();
    }

    /**
     * 执行Http请求
     *
     * @param onlinePaymentRequest
     * @return
     */
    public OnlinePaymentResponse execute(AbstractOnlinePaymentRequest onlinePaymentRequest) {
        HttpClient httpclient = new HttpClient(connectionManager);

        // 设置连接超时
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);

        // 设置回应超时
        httpclient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);

        // 设置等待ConnectionManager释放connection的时间
        httpclient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);

        HttpMethod method = null;

        //Only use POST
        method = new PostMethod(onlinePaymentRequest.getUrl());
        ((PostMethod) method).addParameters(onlinePaymentRequest.getParameters());
        method.addRequestHeader("Content-Type",
                "application/x-www-form-urlencoded; text/html; charset=" + DEFAULT_CHARSET);

        // 设置Http Header中的User-Agent属性
        method.addRequestHeader("User-Agent", "Mozilla/4.0");
        OnlinePaymentResponse onlinePaymentResponse = null;

        try {
            httpclient.executeMethod(method);
            onlinePaymentResponse = new OnlinePaymentResponse(method.getResponseHeaders(), method.getResponseBodyAsString());
        } catch (Exception ex) {
            //TODO: Don't catch general Exception in the future
            return null;
        } finally {
            method.releaseConnection();
        }
        return onlinePaymentResponse;
    }
}
