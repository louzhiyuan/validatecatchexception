/*********************************************************************
 * 
 * CHINA TELECOM CORPORATION CONFIDENTIAL
 * ______________________________________________________________
 * 
 *  [2015] - [2020] China Telecom Corporation Limited, 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of China Telecom Corporation and its suppliers,
 * if any. The intellectual and technical concepts contained 
 * herein are proprietary to China Telecom Corporation and its 
 * suppliers and may be covered by China and Foreign Patents,
 * patents in process, and are protected by trade secret  or 
 * copyright law. Dissemination of this information or 
 * reproduction of this material is strictly forbidden unless prior 
 * written permission is obtained from China Telecom Corporation.
 **********************************************************************/

package DaDeTest;


import java.util.HashMap;
import java.util.Map;

/**
 * @author lichao
 */
public class TokenHandler implements Runnable {

    //更换成自己的apikey
    private static final String APIKEY ="D942B399B89A840C7BDA1D0D39691BB8";

   //更换成自己的秘钥
    private static final String SECRETKEY ="02559FD132E547460FE4D5A856684FCA";

    //访问的IP地址或域名
    private static final String HOST ="113.106.54.66";
    //端口
    private static final int PORT =18080;

    // 协议
    private static final String protocol = "http://";

    // 分隔符
    private static final String separator = "/";


    @Override
    public void run() {
        try {
            // 获取publickey
            String publicKey = getPublicKey(APIKEY);
            // 获取sign
            String sign = encryptMd5(APIKEY, SECRETKEY, publicKey);
            // 获取token
            String tokenId = getToken(APIKEY, publicKey, sign);
            TokenManager.getInstance().putToken(tokenId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**ç
     * 获取tokenID
     * 
     * @param apiKey2
     * @param publicKey
     * @param sign
     * @return
     * @throws
     */
    @SuppressWarnings("unchecked")
    private String getToken(String apiKey2, String publicKey, String sign) throws Exception {

        String url = getUrl(2);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("apiKey", apiKey2);
        params.put("authCode", publicKey);
        params.put("sign", sign);
        UHttpClient.Res res = UHttpClient.doRequest(UHttpClient.Method.get, url, params, "UTF-8", true);
        if (null != res && res.statusCode == 200) {
            try {
                System.out.println(res.content);
                Map<String, Object> vaueMap = CommonUtil.jsonToMapObj(res.content);
                Map<String, String>  tokenMap=  (Map<String, String>) vaueMap.get("data");
                return tokenMap.get("token");
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }


    /**
     * 拼接请求url
     * 
     * @param type url 类型
     * @return
     */
    private String getUrl(int type) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(HOST).append(":").append(PORT);
        sb = type == 1 ? sb.append("/restful/system/publicKey.json") : sb.append("/restful/system/token.json");
        return sb.toString();

    }


    /**
     * @param apiKey2
     * @param secretKey2
     * @param publicKey
     * @return
     */
    private String encryptMd5(String apiKey2, String secretKey2, String publicKey) {
        return CommonUtil.MD5(apiKey2.concat(secretKey2).concat(publicKey));
    }


    /**
     * 获取 authcode
     * 
     * @param userApiKey
     * @return
     * @throws
     */
    private String getPublicKey(String userApiKey) throws Exception {
        String url = getUrl(1);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("apiKey", userApiKey);
        UHttpClient.Res res = UHttpClient.doRequest(UHttpClient.Method.get, url, params, "UTF-8", true);
        if (null != res && res.statusCode == 200) {
            try {
                Map<String, String> vaueMap = CommonUtil.jsonToMap(res.content);
                return vaueMap.get("data");
            } catch (Exception e) {
                throw  e;
            }
        }
        return null;
    }


    /**
     * 获得请求业务接口的url
     * 
     * @param product
     * @param module
     * @param method
     * @return
     */
    public static String getReqUrl(String product, String module, String method) {
        String token = TokenManager.getInstance().getToken();
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(HOST).append(":").append(PORT).append("/restful");
        sb.append(separator).append(product).append(separator).append(module).append(separator).append(method)
                .append(separator).append(APIKEY).append(separator).append(TokenManager.getInstance().getToken()).append(".json");
        return sb.toString();
    }

}
