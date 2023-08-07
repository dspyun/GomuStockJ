package main.java.com.gomu.gomustock.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
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
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
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

    String ACCESS_TOKEN="";
    final String HOST = "https://openapi.ebestsec.co.kr:8080/";

    public Ebest() {

    }

    public void testmain() throws IOException {
        getToken();
        getHOGA();
    }

    public void strSplit(StringBuffer sb) {
        // json으로 파싱해야 한다. 웹크롤링하듯이 파싱하면 된다
        String temp=sb.toString();

        JsonObject keys = (JsonObject) JsonParser.parseString(temp);
        ACCESS_TOKEN = keys.get("access_token").getAsString();
        System.out.println(ACCESS_TOKEN); // apple
    }

    // String jsonMessage
    public void getToken() throws UnsupportedEncodingException {
        // 주식 token 발급 예제

        final String tokenRequestUrl = HOST + "oauth2/token";

        String GRANT_TYPE = "client_credentials";
        String APP_KEY = "PSypDQNAvmgk7V8KIZQnBySi1B6yTGqx6RnL";
        String APP_SECRET = "4oOpWK4V200GtRMvbRf0eqrXalZL2wQw";
        String SCOPE = "oob";
        String ContentsType="application/x-www-form-urlencoded";

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Contents-Type",ContentsType);
        param.put("grant_type",GRANT_TYPE);
        param.put("appkey",APP_KEY);
        param.put("appsecretkey",APP_SECRET);
        param.put("scope",SCOPE);
        StringBuffer data = new StringBuffer();
        for (String key : param.keySet()) {
            data.append(URLEncoder.encode(key, "UTF-8") + "=");
            data.append(URLEncoder.encode((String) param.get(key), "UTF-8") + "&");
        }

        HttpsURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr= null;

        try {

            final URL url = new URL(tokenRequestUrl);

            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Contents-Type", ContentsType);
            //conn.setRequestProperty("Contents-Type", data.toString());
            conn.setDoOutput(true);

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data.substring(0, data.length() - 1));
            writer.flush();

            final int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + tokenRequestUrl);
            System.out.println("Post parameters : " + param.toString());
            System.out.println("Response Code : " + responseCode);

            isr = new InputStreamReader(conn.getInputStream());
            reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            strSplit(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHOGA() {

        // 주식 호가 죄회 예제
        String ContentsType="application/json; utf-8";
        String tokenRequestUrl = HOST + "stock/market-data";

        try {

            JSONObject innerdata = new JSONObject();
            innerdata.put("shcode","005930");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("t1101InBlock", innerdata);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(tokenRequestUrl);
            post.setHeader("Contents-Type",ContentsType);
            post.setHeader("authorization","Bearer " + ACCESS_TOKEN);
            post.setHeader("tr_cd","t1101");
            post.setHeader("tr_cont","N");
            post.setHeader("tr_cont_key","");
            URI uri = new URIBuilder(post.getURI()).build();
            post.setURI(uri);
            post.setEntity(new StringEntity(jsonObject.toString(),ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {

            }

            System.out.println("Request body " + jsonObject.toString());

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output  .... ");
            String respStr = "";
            while ((output = br.readLine()) != null) {
                respStr = respStr + output;
                System.out.println(output);
            }

            JSONObject jobj = new JSONObject(respStr);
            JSONObject bodyobj = jobj.getJSONObject("t1101OutBlock");
            System.out.println(bodyobj.getString("hname"));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
