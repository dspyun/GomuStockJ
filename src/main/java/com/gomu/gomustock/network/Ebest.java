package main.java.com.gomu.gomustock.network;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class Ebest {

    String ACCESS_TOKEN="";
    final String HOST = "https://openapi.ebestsec.co.kr:8080/";

    public Ebest() {

    }

    public void testmain()  {
        getToken();
        //getHOGA();
        //getNEWS();
        //newslit();
        //getNEWSList();
        //getNEWSList_websocket_example();
        getGIGANJUGA();
    }

    public void strSplit(StringBuffer sb) {
        // json으로 파싱해야 한다. 웹크롤링하듯이 파싱하면 된다
        String temp=sb.toString();

        JsonObject keys = (JsonObject) JsonParser.parseString(temp);
        ACCESS_TOKEN = keys.get("access_token").getAsString();
        System.out.println(ACCESS_TOKEN); // apple
    }

    // String jsonMessage
    public void getToken() {
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


        HttpsURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr= null;

        try {

            for (String key : param.keySet()) {
                data.append(URLEncoder.encode(key, "UTF-8") + "=");
                data.append(URLEncoder.encode((String) param.get(key), "UTF-8") + "&");
            }

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
            String stock_name = bodyobj.getString("hname");
            int hoga = bodyobj.getInt("offerho1");
            System.out.println(stock_name + " 1단계호가 " + hoga);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getGIGANJUGA() {

        // 230815 주식 기간주가 조회 예제. 회신 결과를 파싱하고 open가격만 보여주도록 예제구현.
        // 이 후 시가, 고가, 저가, 종가, vol을 엑셀에 저장하는 기능 추가 필요
        String ContentsType="application/json; utf-8";
        String tokenRequestUrl = HOST + "stock/market-data";

        try {

            JSONObject innerdata = new JSONObject();
            innerdata.put("shcode","005930");
            innerdata.put("dwmcode",1); // 1:일, 2:주, 3:월
            innerdata.put("data","");
            innerdata.put("idx",0);
            innerdata.put("cnt",5); // 일수 1이상, 3년 250*3=750도 가능
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("t1305InBlock", innerdata);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(tokenRequestUrl);
            post.setHeader("Contents-Type",ContentsType);
            post.setHeader("authorization","Bearer " + ACCESS_TOKEN);
            post.setHeader("tr_cd","t1305");
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
            JSONArray bodyarry = jobj.getJSONArray("t1305OutBlock1");// []로 둘러쌓인 것은 array로 받아야 한다
            int size = bodyarry.length();
            for(int i =0;i<size;i++) {
                JSONObject jobj1 = (JSONObject) bodyarry.get(i);
                System.out.println("open " + jobj1.get("open"));
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getNEWS() {
        /*
        NWS 실시간 등록 이후 뉴스가 실시간으로 수신되면,
        NWS TR의 outblock 필드중 realkey 필드의 데이터를 가져와
        t3102 의 sNewsno 에 넣고 조회하시면 됩니다
        news조회 단독으로 사용하지 못함
        일단 NWS조회로 제목을 가져오면 sNewsno도 읽을 수 있음
        제목의 상세내용을 보고 싶으면 sNewsno로 뉴스읽기를 하면 됨됨
         NWS는 개발자콘솔 > 기타 > 실시간시세 > 실시간뉴스제목패킷(NWS)에 사용법이 있음
         */

        // 주식 호가 죄회 예제
        String ContentsType="application/json; utf-8";
        String tokenRequestUrl = HOST + "stock/investinfo";

        try {

            JSONObject innerdata = new JSONObject();
            innerdata.put("sNewsno","2023051510383935PL7HQ87D"); //192820
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("t3102InBlock", innerdata);

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(tokenRequestUrl);
            post.setHeader("Contents-Type",ContentsType);
            post.setHeader("authorization","Bearer " + ACCESS_TOKEN);
            post.setHeader("tr_cd","t3102"); // body에 쓰인 숫자와 동일해야 한다
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
            JSONObject bodyobj = jobj.getJSONObject("t3102InBlock");
            /*
            String stock_name = bodyobj.getString("hname");
            int hoga = bodyobj.getInt("offerho1");
            System.out.println(stock_name + " 1단계호가 " + hoga);
            */
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void getNEWSList()  {
        /*
        NWS 실시간 등록 이후 뉴스가 실시간으로 수신되면,
        NWS TR의 outblock 필드중 realkey 필드의 데이터를 가져와
        t3102 의 sNewsno 에 넣고 조회하시면 됩니다
        news조회 단독으로 사용하지 못함
        일단 NWS조회로 제목을 가져오면 sNewsno도 읽을 수 있음
        제목의 상세내용을 보고 싶으면 sNewsno로 뉴스읽기를 하면 됨됨
         NWS는 개발자콘솔 > 기타 > 실시간시세 > 실시간뉴스제목패킷(NWS)에 사용법이 있음
         */

        // 주식 호가 죄회 예제
        String ContentsType="application/json; utf-8";
        String tokenRequestUrl = "wss://openapi.ebestsec.co.kr:9443/" + "websocket/";
        //String tokenRequestUrl = "https://openapi.ebestsec.co.kr:29443/" + "websocket/etc";
        //String tokenRequestUrl = HOST + "websocket/etc";
        System.out.println("get newslist ------------------------\n");
        try {

            JSONObject jsonObject = new JSONObject();
            JSONObject inner1 = new JSONObject();
            jsonObject.put("tr_cd", "NWS");
            jsonObject.put("tr_key", "NWS001");
            //jsonObject.put("body",inner1);
            /*
            JSONObject inner2 = new JSONObject();
            inner2.put("tr_type", "3");
            inner2.put("token", ACCESS_TOKEN);
            jsonObject.put("body",inner2);
            */

            // post에 header설정하고
            HttpPost post = new HttpPost(tokenRequestUrl);
            post.setHeader("Contents-Type",ContentsType);
            post.setHeader("token",ACCESS_TOKEN);
            post.setHeader("tr_type","3"); // body에 쓰인 숫자와 동일해야 한다

            URI uri = new URIBuilder(post.getURI()).build();
            post.setURI(uri);
            post.setEntity(new StringEntity(jsonObject.toString(),ContentType.APPLICATION_JSON));

            System.out.println("\nSending 'POST' request to URL : " + tokenRequestUrl);
            HttpClient client = HttpClientBuilder.create().build();
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


        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }

    public void newslit() {
        try {
            String ContentsType="application/json; utf-8";
            String tokenRequestUrl = "wss://openapi.ebestsec.co.kr:9443/" + "websocket/etc";

            StringBuilder urlBuilder = new StringBuilder(tokenRequestUrl);
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject();
            JSONObject inner1 = new JSONObject();
            inner1.put("tr_cd", "NWS");
            inner1.put("tr_key", "NWS001");
            jsonObject.put("body",inner1);

            JSONObject inner2 = new JSONObject();
            inner2.put("Contents-Type", ContentsType);
            inner2.put("tr_type", "3");
            inner2.put("token", ACCESS_TOKEN);
            jsonObject.put("header",inner2);

            byte[] body = jsonObject.toString().getBytes();
            conn.setFixedLengthStreamingMode(body.length);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(body);
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getNEWSList_websocket_example()  {
        /*
        NWS 실시간 등록 이후 뉴스가 실시간으로 수신되면,
        NWS TR의 outblock 필드중 realkey 필드의 데이터를 가져와
        t3102 의 sNewsno 에 넣고 조회하시면 됩니다
        news조회 단독으로 사용하지 못함
        일단 NWS조회로 제목을 가져오면 sNewsno도 읽을 수 있음
        제목의 상세내용을 보고 싶으면 sNewsno로 뉴스읽기를 하면 됨됨
         NWS는 개발자콘솔 > 기타 > 실시간시세 > 실시간뉴스제목패킷(NWS)에 사용법이 있음
         */

        // 주식 호가 죄회 예제
        String ContentsType="application/json; utf-8";
        String tokenRequestUrl = "wss://openapi.ebestsec.co.kr:9443/" + "websocket/etc";

        System.out.println("get newslist ------------------------\n");
        try {

            JSONObject jsonObject = new JSONObject();
            JSONObject inner1 = new JSONObject();
            inner1.put("tr_cd", "NWS");
            inner1.put("tr_key", "NWS001");
            jsonObject.put("body",inner1);

            JSONObject inner2 = new JSONObject();
            inner2.put("Contents-Type", ContentsType);
            inner2.put("tr_type", "3");
            inner2.put("token", ACCESS_TOKEN);
            jsonObject.put("header",inner2);

            System.out.println("sending  .... " + jsonObject.toString());

            URI uri = new URI(tokenRequestUrl);
            WebSocketUtil webSocketUtil = new WebSocketUtil(uri, new Draft_6455());
            webSocketUtil.connectBlocking();
            //웹소켓 메세지 보내기
            webSocketUtil.send(jsonObject.toString());

            JSONObject result = webSocketUtil.getResult();
            webSocketUtil.close();

            String output = result.toString();
            System.out.println("Output  .... " + output);


            //JSONObject jobj = new JSONObject(respStr);
            //JSONObject bodyobj = jobj.getJSONObject("t3102InBlock");
                /*
                String stock_name = bodyobj.getString("hname");
                int hoga = bodyobj.getInt("offerho1");
                System.out.println(stock_name + " 1단계호가 " + hoga);
                */
        } catch (InterruptedException  | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public class WebSocketUtil extends WebSocketClient {

        private JSONObject obj;

        public WebSocketUtil(URI serverUri, Draft protocolDraft) {
            super(serverUri, protocolDraft);
        }

        @Override
        public void onMessage( String message ) {
            obj = new JSONObject(message);
            System.out.println("receive data");
        }

        @Override
        public void onOpen( ServerHandshake handshake ) {
            //System.out.println( "opened connection" );
        }

        @Override
        public void onClose( int code, String reason, boolean remote ) {
            //System.out.println( "closed connection" );
        }

        @Override
        public void onError( Exception ex ) {
            ex.printStackTrace();
        }

        public JSONObject getResult() {
            return this.obj;
        }

    }


}
