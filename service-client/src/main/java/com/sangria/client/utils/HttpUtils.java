package com.sangria.client.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String get(String url) {
        String result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            
            HttpGet httpGet = new HttpGet(uri);
            
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String post(String url, String jsonStr) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        httpPost.setHeader("Connection", "Close");

        StringEntity entity = new StringEntity(jsonStr, Charset.forName("UTF-8"));

        entity.setContentEncoding("UTF-8");

        entity.setContentType("application/json");

        httpPost.setEntity(entity);

        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
