package org.cunxin.app.alipay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* *
 *类名：AlipayFunction
 *功能：支付宝接口公用函数类
 *详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 *版本：3.2
 *日期：2011-03-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AliPayCoreFunction {

    /**
     * 生成签名结果
     *
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysign(Map<String, String> sArray, String key) {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        prestr = prestr + key; //把拼接后的字符串再与安全校验码直接连接起来
        String mysign = AliPayMD5Encrypt.md5(prestr);
        return mysign;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

}
