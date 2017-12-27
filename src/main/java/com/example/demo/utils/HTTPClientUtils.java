package com.example.demo.utils;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lzy on 2017/12/1.
 */
@SuppressWarnings("deprecation")
public class HTTPClientUtils {

    /**
     * Create a httpClient instance
     *
     * @param isSSL
     * @return HttpClient instance
     */
    public static CloseableHttpClient getClient(boolean isSSL) {
        // CloseableHttpClient httpClient = null;
        HttpClientBuilder clientbuilder = HttpClients.custom();

        if (isSSL) {
            X509TrustManager xtm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            try {
                SSLContext ctx = SSLContext.getInstance("TLS");

                ctx.init(null, new TrustManager[] { xtm }, null);
                SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);

                clientbuilder.setSSLSocketFactory(socketFactory);

                // httpClient =
                // HttpClients.custom().setRoutePlanner(routePlanner)
                // .setSSLSocketFactory(socketFactory)
                // .build();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        return clientbuilder.build();
    }


    /**
     * Check illegal String
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.lookingAt();
    }

}
