package com.example.demo.utils.httpclient;

import com.piccfs.dt.apiadpter.utils.HTTPClientUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpPostUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpPostUtil.class);
	
	/**
	 * 获取响应的状态码
	 * @param url
	 * @param params
	 */
	public static int getResponseStatusCode(String url, Map<String, String> params) {
		int statusCode = -1;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
			// 创建参数列表
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : params.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(uefEntity);
			logger.debug("POST url is :" + post.getURI());
			// 执行请求
			CloseableHttpResponse httpResponse = httpclient.execute(post);
			try {
				StatusLine statusLine = httpResponse.getStatusLine();
				statusCode = statusLine.getStatusCode();
			} finally {
				httpResponse.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return statusCode;
	}
	
	public static String getResponseContext(String url, Map<String, String> params) {
		String context = null;
		CloseableHttpClient httpclient = HTTPClientUtils.getClient(false);
		try {
			HttpPost post = new HttpPost(url);
			// 创建参数列表
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : params.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(uefEntity);
			logger.debug("POST url is :" + post.getURI());
			// 执行请求
			CloseableHttpResponse httpResponse = httpclient.execute(post);
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					context = EntityUtils.toString(entity);
				}
			} finally {
				httpResponse.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context;
	}
}
