package main.java.com.gomu.gomustock.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.intellij.openapi.util.text.StringUtil;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import nonapi.io.github.classgraph.json.JSONSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        //testauth();
        testauth2();
    }


    public void testauth2() {
        final String AUTH_HOST = "https://openapi.ebestsec.co.kr:8080/";
        final String tokenRequestUrl = AUTH_HOST + "oauth2/token";

        String GRANT_TYPE = "client_credentials";
        String APP_KEY = "PSypDQNAvmgk7V8KIZQnBySi1B6yTGqx6RnL";
        String APP_SECRET = "4oOpWK4V200GtRMvbRf0eqrXalZL2wQw";
        String SCOPE = "oob";
        String ContentsType="application/x-www-form-urlencoded";

        HttpsURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr= null;

        try {
            final String params = String.format(
                    "grant_type=%s" +
                    "&appkey=%s" +
                    "&appsecretkey=%s" +
                    "&scope=%s"+
                    "&Contents-Type=%s",
                    GRANT_TYPE, APP_KEY, APP_SECRET, SCOPE, ContentsType);

            final URL url = new URL(tokenRequestUrl);

            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(params);
            writer.flush();

            final int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + tokenRequestUrl);
            System.out.println("Post parameters : " + params);
            System.out.println("Response Code : " + responseCode);

            isr = new InputStreamReader(conn.getInputStream());
            reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            strSplit(buffer);

            //System.out.println(buffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (writer != null) {
                try {
                    writer.close();
                } catch(Exception ignore) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch(Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch(Exception ignore) {
                }
            }
        }
    }

    public void strSplit(StringBuffer sb) {
        // json으로 파싱해야 한다. 웹크롤링하듯이 파싱하면 된다
        String temp=sb.toString();

        JsonObject keys = (JsonObject) JsonParser.parseString(temp);

        System.out.println(keys.get("access_token")); // apple
        System.out.println(keys.get("scope")); // 1
        System.out.println(keys.get("token_type")); // 1000
        System.out.println(keys.get("expires_in")); // 1000
        //jsonObject = (JSONObject) parser.parse(temp);
    }


}
