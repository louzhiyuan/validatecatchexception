package com.example.demo.utils.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lzy on 2017/12/1.
 */


import java.io.IOException;

public class HttpGetUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpGetUtils.class);

    public static String getResponseContext(String url) {
        String content = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse httpResponse = httpclient.execute(httpget);
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                content = EntityUtils.toString(entity,"utf-8");
            }
            logger.debug("response content is : " + content);

            //打印请求参数
            //System.out.println("response content is : " + content);
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

/*    public static void main(String[] args) {
       String url =  "https://tapi.ccxcredit.com:443/data-service/riskinfo/t1?account=renbao1234&cid=123456789012345&name=%E5%BC%A0%E4%B8%89%E6%B5%8B%E8%AF%95%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&type=2&reqTid=1512365363095&sign=59D3DD23852331E1D6782AEAB5162689";

        System.out.println(getResponseContext(url));
    }*/
}
