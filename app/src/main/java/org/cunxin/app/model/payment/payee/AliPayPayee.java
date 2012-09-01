package org.cunxin.app.model.payment.payee;

public class AliPayPayee extends Payee {
    private String _aliPayEmail = "";

    private String _partnerId = "";
    // 支付宝服务器通知的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
    // 必须保证其地址能够在互联网中访问的到
    private String _notifyUrl = "http://www.cunxin.com/";

    // 当前页面跳转后的页面 要用 http://格式的完整路径，不允许加?id=123这类自定义参数
    // 域名不能写成http://localhost/create_direct_pay_by_user_jsp_gb/return_url.jsp ，否则会导致return_url执行无效
    private String _returnUrl = "http://www.cunxin.com/";

    private String _key = "";

    public AliPayPayee(String aliPayEmail, double desiredAmount) {
        super(desiredAmount);
        _aliPayEmail = aliPayEmail;
    }

    public String getKey() {
        return _key;
    }

    public String getAliPayEmail() {
        return _aliPayEmail;
    }

    public String getPartnerId() {
        return _partnerId;
    }

    public String getNotifyUrl() {
        return _notifyUrl;
    }

    public String getReturnUrl() {
        return _returnUrl;
    }

    @Override
    public String getPayeeAccount() {
        return getAliPayEmail();
    }
}
