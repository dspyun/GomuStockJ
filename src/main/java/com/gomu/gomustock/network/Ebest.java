package main.java.com.gomu.gomustock.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.amazon.ion.impl._Private_IonConstants.False;
import static jxl.biff.FormatRecord.logger;

public class Ebest {


    public Ebest() {

    }

    public void testmain() throws IOException {
        //String msgMap = sendREST("http:localhost", json);
        //testauth2();
        testauth();
    }

    static void testauth2() {
        String APP_KEY = "PSypDQNAvmgk7V8KIZQnBySi1B6yTGqx6RnL";
        String APP_SECRET = "4oOpWK4V200GtRMvbRf0eqrXalZL2wQw";

        String url = "https://openapi.ebestsec.co.kr:8080/oauth2/token/";
        HashMap map = new HashMap<String,String>();
        //map.put("Accept-Charset","UTF-8");
        map.put("Content-Type","application/x-www-form-urlencoded");
        map.put("grant_type","client_credentials");
        map.put("appkey",APP_KEY);
        map.put("appsecretkey",APP_SECRET);
        map.put("scope","oob");

        String result = sendPost(url, map,null);

        String accessToken = (String) new Gson().fromJson(result, HashMap.class).get("access_token");
    }

    static String APP_KEY = "PSypDQNAvmgk7V8KIZQnBySi1B6yTGqx6RnL";
    static String APP_SECRET = "4oOpWK4V200GtRMvbRf0eqrXalZL2wQw";

    static void testauth() throws IOException {

        String BASE_URL = "https://openapi.ebestsec.co.kr:8080/";
        String PATH = "oauth2/token/";
        String URLSTR = BASE_URL + PATH;

        StringBuilder urlBuilder = new StringBuilder(URLSTR);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Charset", "UTF-8"); // 관리자 권고
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 관리자 권고
        conn.setRequestProperty("grant_type", "client_credentials"); // grant type : 가이드 권고
        conn.setRequestProperty("appkey", APP_KEY);
        conn.setRequestProperty("appsecretkey", APP_SECRET);
        conn.setRequestProperty("scope", "oob"); // Access Token 권한 범위

        byte[] body = "".getBytes();
        conn.setFixedLengthStreamingMode(body.length);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(body);
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }

    static void testauth3() throws IOException {

        String BASE_URL = "https://openapi.ebestsec.co.kr:8080/";
        String PATH = "oauth2/token/";
        String URLSTR = BASE_URL + PATH;

        StringBuilder urlBuilder = new StringBuilder(URLSTR);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept-Charset", "UTF-8"); // 관리자 권고
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 관리자 권고

        conn.setRequestProperty("grant_type", "client_credentials"); // grant type : 가이드 권고
        conn.setRequestProperty("appkey", APP_KEY);
        conn.setRequestProperty("appsecretkey", APP_SECRET);
        conn.setRequestProperty("scope", "oob"); // Access Token 권한 범위
        //conn.setRequestProperty("token_type", "Bearer"); // Access Token 권한 범위
        //conn.setRequestProperty("Authorization", "bearer " + accessToken);

        /*
        String param = "grant_type=" + "client_credentials";
        param += "appkey=" + APP_KEY;
        param += "appsecretkey=" + APP_SECRET;
        param += "scope=" + "oob";
        */

        byte[] body = "".getBytes();
        conn.setFixedLengthStreamingMode(body.length);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(body);
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }


    public static void testjson() {
        try{
            StringBuilder urlBuilder = new StringBuilder("/stock/market-data");
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            byte[] body = "".getBytes();
            conn.setFixedLengthStreamingMode(body.length);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(body);
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendREST(String sendUrl, String jsonValue) throws IllegalStateException {

        String inputLine = null;
        StringBuffer outResult = new StringBuffer();

        try{
            //logger.debug("REST API Start");
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            OutputStream os = conn.getOutputStream();
            os.write(jsonValue.getBytes("UTF-8"));
            os.flush();

            // 리턴된 결과 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }

            conn.disconnect();
            //logger.debug("REST API End");
        }catch(Exception e){
            //logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return outResult.toString();
    }


    private static final int timeout = 30 * 1000;

    public static String sendPost(String url, Map parameterMap, String accessToken)
    {
        Map paramMap = parameterMap;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = null;

        try
        {
            HttpPost post = new HttpPost(url);
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build();

            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(config);
            httpClient = builder.setSSLSocketFactory(getSSLSocketFactory()).
                    disableCookieManagement().build();

            String jsonString = new GsonBuilder().disableHtmlEscaping().create().toJson(parameterMap);
            HttpEntity stringEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
            post.setEntity(stringEntity);

            if ( StringUtil.isNotEmpty(accessToken))
            {
                post.setHeader("Authorization","Bearer " + accessToken);
            }

            // Execute the method.
            httpResponse = httpClient.execute(post);

            result = getResultContent(httpResponse);
            System.out.println("result : " + result);

        }
        catch (IOException e)
        {
            System.out.println("sendPost - IOException ... : " + url);
        }
        catch(Exception e)
        {
            System.out.println("sendPost - Exception ... : " + url);
        }
        finally
        {
            try
            {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (httpResponse != null) {
                    httpResponse.close();
                }
            }
            catch (IOException e)
            {
                System.out.println( e);
            }
        }

        return result;
    }

    private static String getResultContent(CloseableHttpResponse httpResponse) throws IOException
    {
        InputStream in = httpResponse.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private static SSLConnectionSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException
    {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());

        return connectionFactory;
    }
}
