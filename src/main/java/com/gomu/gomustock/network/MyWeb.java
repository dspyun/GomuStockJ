package main.java.com.gomu.gomustock.network;

import main.java.com.gomu.gomustock.MyDate;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;


import main.java.com.gomu.gomustock.format.*;
import main.java.com.gomu.gomustock.jlist.InfoDownload;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


public class MyWeb {


    String target_stock ="";
    public String result ="";

    public MyWeb() {

    }
    public MyWeb(String stock_no) {
        target_stock = stock_no;
        target_stock = "005930";
    }

    public interface IFCallback {
        public void callback(String str);
    }
    // 콜백인터페이스를 구현한 클래스 인스턴스
    private MyWeb.IFCallback _cb;
    public void setCallback(MyWeb.IFCallback cb) {
        this._cb = cb;
    }


    public boolean checkKRStock(String stock_code) {
        // 숫자 스트링이면 true, 문자가 있으면 false를 반환한다.
        // 즉 한국주식이면 true, 외국주식이면 false 반환
        boolean isNumeric =  stock_code.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }


    public FormatStockInfo getNaverStockinfo(String stock_code) {
        FormatStockInfo result = new FormatStockInfo();

        System.out.println("stock_code = " + stock_code+"\n");
        if(!checkKRStock(stock_code)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            result.init();
            return result;
        }
        try {

            String URL = "https://finance.naver.com/item/coinfo.naver?code="+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements classinfo0 = doc.select(".wrap_company"); // class가져오기
            Element giname = classinfo0.select("h2").get(0);
            result.stock_name = giname.text();

            Elements plist = classinfo0.select(".summary_info");
            int size = plist.size();
            for(int i =0;i<size;i++) {
                result.desc = plist.get(i).text() + "\n";
            }

            Elements classinfo1 = doc.select("#tab_con1"); // id 가져오기
            Elements trlist = classinfo1.select("tr");

            result.market_sum = trlist.get(0).select("td").get(0).text();
            result.ranking = trlist.get(1).select("td").get(0).text();
            result.fogn_rate = trlist.get(6).select("td").get(0).text();
            result.recommend = trlist.get(7).select("td").get(0).text();

            result.per = trlist.get(9).select("td").get(0).text();
            result.expect_per = trlist.get(10).select("td").get(0).text();
            String temp_pbr =  trlist.get(11).select("td").get(0).text();
            result.pbr = temp_pbr.replaceAll("\\.", "");
            result.div_rate = trlist.get(12).select("td").get(0).text();
            result.area_per = trlist.get(13).select("td").get(0).text();
            //result.op_profit = td1_list.get(3).text();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }


    public FormatETFInfo getNaverETFinfo(String stock_code) {
        FormatETFInfo result = new FormatETFInfo();

        System.out.println("stock_code = " + stock_code+"\n");
        if(!checkKRStock(stock_code)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            result.init();
            return result;
        }
        try {
            String URL = "https://finance.naver.com/item/main.naver?code="+stock_code;
            //String URL = "https://finance.naver.com/item/coinfo.naver?code="+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements classinfo0 = doc.select(".wrap_company"); // class가져오기
            Element giname = classinfo0.select("h2").get(0);
            result.stock_name = giname.text();

            Elements plist = classinfo0.select(".summary_info");
            int size = plist.size();
            for(int i =0;i<size;i++) {
                result.desc = plist.get(i).text() + "\n";
            }

            Elements classinfo1 = doc.select("#tab_con1"); // id 가져오기
            Elements trlist = classinfo1.select("tr");

            result.market_sum = trlist.get(0).select("td").get(0).text();
            result.fund_fee =  trlist.get(6).select("td").get(0).text();
            result.nav = trlist.get(8).select("td").get(0).text();
            result.m1_profit_rate = trlist.get(9).select("td").get(0).text();
            result.m3_profit_rate = trlist.get(10).select("td").get(0).text();
            result.m6_profit_rate = trlist.get(11).select("td").get(0).text();
            result.y1_profit_rate = trlist.get(12).select("td").get(0).text();
            result.stock_code = stock_code;
            //result.op_profit = td1_list.get(3).text();

            Elements tableinfo = doc.getElementsByClass("section etf_asset"); // class 가져오기
            //Elements classlinfo = doc.getElementsByClass("ETF 주요 구성자산");
            //Elements tableinfo = classinfo2.select("table"); // class 가져오기
            //Elements tbodyinfo = tableinfo.select("tbody"); // class 가져오기
            Elements tdlist = tableinfo.select("td"); // class 가져오기
            Elements linklist = tdlist.select("a");
            result.companies="";// class 가져오기
            int size1 = linklist.size();
            for(int i =0;i<size1;i++) {
                String name = linklist.get(i).text();
                if(!name.equals("") && !name.equals("null")) result.companies += name +" ";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public List<List<String>> getAgencyFogn(String stock_code, String pageno) {
        List<List<String>> result = new ArrayList<List<String>>();

        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();

        if(!checkKRStock(stock_code)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            for(int i =0;i<20;i++) {
                agent.add("");
                fogn.add("");
            }
            result.add(agent);
            result.add(fogn);
            return result;
        }

        try {
            String pagenumber;
            if(pageno.equals("0")) pagenumber="";
            else pagenumber = "&page="+pageno;

            String URL = "https://finance.naver.com/item/frgn.naver?code="+stock_code+pagenumber;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements classinfo = doc.select(".inner_sub");
            Element table1 = classinfo.select("table").get(1);
            Elements trlist = table1.select("tr");

            for(int i = 3;i<8;i++) {
                Element tr3 = trlist.get(i);
                Elements tdlist = tr3.select("td");
                agent.add(tdlist.get(5).text());
                fogn.add(tdlist.get(6).text());
            }
            for(int i = 11;i<16;i++) {
                Element tr3 = trlist.get(i);
                Elements tdlist = tr3.select("td");
                agent.add(tdlist.get(5).text());
                fogn.add(tdlist.get(6).text());
            }
            for(int i = 19;i<24;i++) {
                Element tr3 = trlist.get(i);
                Elements tdlist = tr3.select("td");
                agent.add(tdlist.get(5).text());
                fogn.add(tdlist.get(6).text());
            }
            for(int i = 27;i<32;i++) {
                Element tr3 = trlist.get(i);
                Elements tdlist = tr3.select("td");
                agent.add(tdlist.get(5).text());
                fogn.add(tdlist.get(6).text());
            }
            int j = 0;
            ;/*
            System.out.println("per = " + result.per+"\n");
            System.out.println("per12 = " + result.per12+"\n");
            System.out.println("area_per = " + result.area_per+"\n");
            System.out.println("pbr = " + result.pbr+"\n");
            System.out.println("div_rate = " +result.div_rate+"\n");
            System.out.println("fogn_rate = " + result.fogn_rate+"\n");
            System.out.println("beta = " + result.beta+"\n");
            System.out.println("op_profit = " + result.op_profit+"\n");
*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result.add(agent);
        result.add(fogn);
        return result;
    }

    public String getCurrentStockPrice(String stockcode) {

        //https://jul-liet.tistory.com/209
        String stockprice="";
        //https://finance.naver.com/sise/sise_index.naver?code=KPI200
        String URL = "https://finance.naver.com/item/main.nhn?code=" + stockcode;
        Document doc;
        if(!checkKRStock(stockcode)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            return stockprice="0";
        }
        try {
            doc = Jsoup.connect(URL).get();
            Elements elem = doc.select(".date");
            String[] str = elem.text().split(" ");
            Elements todaylist =doc.select(".new_totalinfo dl>dd");
            String juga = todaylist.get(3).text().split(" ")[1];
            /*
            String DungRakrate = todaylist.get(3).text().split(" ")[6];
            String siga =  todaylist.get(5).text().split(" ")[1];
            String goga = todaylist.get(6).text().split(" ")[1];
            String zeoga = todaylist.get(8).text().split(" ")[1];
            String georaeryang = todaylist.get(10).text().split(" ")[1];
            String stype = todaylist.get(3).text().split(" ")[3]; //상한가,상승,보합,하한가,하락 구분
            String vsyesterday = todaylist.get(3).text().split(" ")[4];
            */
            stockprice = juga;
            System.out.println(stockcode + " 주가 : "+juga);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stockprice;
    }


    public String checkOpenday() {

        //https://jul-liet.tistory.com/209
        String stockprice="";
        String URL = "https://finance.naver.com/";
        Document doc;

        try {
            doc = Jsoup.connect(URL).get();

            Elements timeinfo =doc.select(".ly_realtime");
            Element marketday = timeinfo.select("#time").get(0);
            String day = marketday.text().replaceAll("\\.", "").substring(0,8);
            return day;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("반환값 "+stockprice);

        return stockprice;
    }

    public void dl_fogninfo(List<String> buylist) {

        for(int i =0;i<buylist.size();i++) {
            MyExcel myexcel = new MyExcel();
            List<List<String>> value = new ArrayList<List<String>>();
            List<String> agency = new ArrayList<>();
            List<String> fogn = new ArrayList<>();

            String stock_code = buylist.get(i);
            value = getAgencyFogn(stock_code,"0");
            agency.addAll(value.get(0));
            fogn.addAll(value.get(1));

            value = getAgencyFogn(stock_code,"2");
            agency.addAll(value.get(0));
            fogn.addAll(value.get(1));

            value = getAgencyFogn(stock_code,"3");
            agency.addAll(value.get(0));
            fogn.addAll(value.get(1));

            value = getAgencyFogn(stock_code,"4");
            agency.addAll(value.get(0));
            fogn.addAll(value.get(1));

            value = getAgencyFogn(stock_code,"5");
            agency.addAll(value.get(0));
            fogn.addAll(value.get(1));

            agency.add(0,"AGENCY");
            fogn.add(0,"FOREIgN");
            myexcel.writefogninfo(stock_code, fogn, agency);
        }
    }

    public void dl_fogninfo_one(String stock_code) {

        MyExcel myexcel = new MyExcel();
        List<List<String>> value = new ArrayList<List<String>>();
        List<String> agency = new ArrayList<>();
        List<String> fogn = new ArrayList<>();

        value = getAgencyFogn(stock_code,"0");
        agency.addAll(value.get(0));
        fogn.addAll(value.get(1));

        value = getAgencyFogn(stock_code,"2");
        agency.addAll(value.get(0));
        fogn.addAll(value.get(1));

        value = getAgencyFogn(stock_code,"3");
        agency.addAll(value.get(0));
        fogn.addAll(value.get(1));

        value = getAgencyFogn(stock_code,"4");
        agency.addAll(value.get(0));
        fogn.addAll(value.get(1));

        value = getAgencyFogn(stock_code,"5");
        agency.addAll(value.get(0));
        fogn.addAll(value.get(1));

        agency.add(0,"AGENCY");
        fogn.add(0,"FOREIgN");
        myexcel.writefogninfo(stock_code, fogn, agency);

    }

    public void getNaverpriceByday(String stock_code, int day) {
        List<FormatOHLCV> naverpricelist = new ArrayList<>();
        String page="";
        int index = day/10;
        for(int i =1;i<=index;i++) {
            page = Integer.toString(i);
            naverpricelist.addAll(getPrice10(stock_code,page));
        }
        FormatOHLCV naverheader = new FormatOHLCV();
        naverheader.date = "date";
        naverheader.close = "close";
        naverheader.open = "open";
        naverheader.high = "high";
        naverheader.low = "low";
        naverheader.volume = "volume";
        naverpricelist.add(0,naverheader);
        MyExcel myexcel = new MyExcel();
        myexcel.writeprice(stock_code,naverpricelist);
    }

    public List<FormatOHLCV> getPrice10(String stock_code, String pageno) {

        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();
        List<FormatOHLCV> naverpricelist = new ArrayList<>();

        try {
            String pagenumber;
            if(pageno.equals("0")) pagenumber="";
            else pagenumber = "&page="+pageno;
            String URL = "https://finance.naver.com/item/sise_day.naver?code="+stock_code+pagenumber;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements trlist = doc.select("tr");
            for(int i =2;i<7;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.","");
                naverprice.close = tdlist.get(1).text().replaceAll(",", "");
                naverprice.open = tdlist.get(3).text().replaceAll(",", "");
                naverprice.high = tdlist.get(4).text().replaceAll(",", "");
                naverprice.low = tdlist.get(5).text().replaceAll(",", "");
                naverprice.volume = tdlist.get(6).text().replaceAll(",", "");
                naverpricelist.add(naverprice);
            }
            for(int i =10;i<15;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.","");
                naverprice.close = tdlist.get(1).text().replaceAll(",", "");
                naverprice.open = tdlist.get(3).text().replaceAll(",", "");
                naverprice.high = tdlist.get(4).text().replaceAll(",", "");
                naverprice.low = tdlist.get(5).text().replaceAll(",", "");
                naverprice.volume = tdlist.get(6).text().replaceAll(",", "");
                naverpricelist.add(naverprice);
            }

            int j = 0;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return naverpricelist;
    }


    public void getNaverpriceByToday(String stock_code, int minute) {
        // 1page는 10분 데이터. 30분은 3페이지, 1시간은 6페이지
        List<FormatOHLCV> naverpricelist = new ArrayList<>();
        String page="";

        for(int i =1;i<=minute;i++) {
            page = Integer.toString(i);
            naverpricelist.addAll(getTodayPrice10(stock_code,page));
        }
        FormatOHLCV naverheader = new FormatOHLCV();
        naverheader.date = "date";
        naverheader.close = "close";
        naverheader.open = "open";
        naverheader.high = "high";
        naverheader.low = "low";
        naverheader.volume = "volume";
        naverpricelist.add(0,naverheader);
        MyExcel myexcel = new MyExcel();
        myexcel.writetodayprice(stock_code+"today",naverpricelist);
    }

    public List<FormatOHLCV> getTodayPrice10(String stock_code, String pageno) {

        MyDate mydate = new MyDate();
        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();
        List<FormatOHLCV> naverpricelist = new ArrayList<>();

        try {
            String pagenumber;
            if(pageno.equals("0")) pagenumber="";
            else pagenumber = "&page="+pageno;
            String time="";
            if(mydate.getTodayDayofWeek() == 6) {
                time = mydate.getBeforeday(1);
                time += 153000;
            } else if (mydate.getTodayDayofWeek() == 7){
                time = mydate.getBeforeday(2);
                time += 153000;
            } else {
                time = mydate.getTodayFullTime();
            }
            // /item/sise_time.naver?code=005490&thistime=20230721113906
            //time = "20230728153000";
            String URL = "https://finance.naver.com/item/sise_time.naver?code="+stock_code+"&thistime="+time+pagenumber;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements trlist = doc.select("tr");
            for(int i =2;i<7;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.",""); // 시간
                naverprice.close = tdlist.get(1).text().replaceAll(",", ""); // 체결가 close
                naverprice.open = tdlist.get(3).text().replaceAll(",", ""); // 매도 open
                naverprice.high = tdlist.get(4).text().replaceAll(",", ""); // 매수 high
                naverprice.volume = tdlist.get(5).text().replaceAll(",", ""); // 거래량
                naverpricelist.add(naverprice);
            }
            for(int i =10;i<15;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.","");
                naverprice.close = tdlist.get(1).text().replaceAll(",", "");
                naverprice.open = tdlist.get(3).text().replaceAll(",", "");
                naverprice.high = tdlist.get(4).text().replaceAll(",", "");
                naverprice.volume = tdlist.get(5).text().replaceAll(",", "");
                naverpricelist.add(naverprice);
            }

            int j = 0;
            ;/*
            System.out.println("per = " + result.per+"\n");
            System.out.println("per12 = " + result.per12+"\n");
            System.out.println("area_per = " + result.area_per+"\n");
            System.out.println("pbr = " + result.pbr+"\n");
            System.out.println("div_rate = " +result.div_rate+"\n");
            System.out.println("fogn_rate = " + result.fogn_rate+"\n");
            System.out.println("beta = " + result.beta+"\n");
            System.out.println("op_profit = " + result.op_profit+"\n");
*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return naverpricelist;
    }


    public void dl_NaverPriceByday(List<String> stock_code, int day) {
        MyStat mystat = new MyStat();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 신규로 추가된 종목이 리스트의 가장 끝에 있다
                // 신규 종목을 가장 먼저 다운로드 하기 위해
                // 리스트를 역순으로 해준다.
                List<String> stock_code_rev = mystat.arrangeRev_string(stock_code);
                for(int i=0;i<stock_code_rev.size();i++) {
                    getNaverpriceByday(stock_code_rev.get(i), day);
                }
                getNaverpriceByday("069500", day); // kodex 200 상품
                //updatePortfolioPrice();
            }
        }).start();
    }


    public List<FormatSector> getSectorInfo() {
        ;
        List<FormatSector> sectorlist = new ArrayList<FormatSector>();

        try {

            String URL = "http://www.paxnet.co.kr/stock/sise/industry";
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements tbody = doc.select("div.pne tbody");
            for(int i =0;i<3;i++ ){
                FormatSector oneinfo = new FormatSector();
                Elements trlist = tbody.get(i).select("tr");
                Elements tdlist = trlist.select("td");
                oneinfo.name = tdlist.get(0).text(); // trlist 0번
                oneinfo.changerate = tdlist.get(1).text(); //
                Elements span2_1 = tdlist.get(2).select("span span");
                oneinfo.changerate_period = span2_1.text();
                Elements span2_2 = tdlist.get(3).select("span span");
                oneinfo.foreign = span2_2.text();
                Elements span2_3 = tdlist.get(4).select("span span");
                oneinfo.agency = span2_3.text();
                Elements span2_4 = tdlist.get(5).select("span span");
                oneinfo.ant = span2_4.text();
                Elements span_5 = tdlist.get(6).select("span");
                oneinfo.top1 = span_5.text();
                Elements span_6 = tdlist.get(7).select("span");
                oneinfo.top2 = span_6.text();
                sectorlist.add(oneinfo);
            }

            int j = 0;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return sectorlist;
    }


    public String getNaverNews() {

        String news_string="";

        try {
            String URL = "https://finance.naver.com/";
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements newsclass = doc.select(".section_strategy");
            Elements lilist = newsclass.select("li");
            for(int i=0;i<6;i++)  {
                news_string += lilist.get(i).text() + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return news_string;
    }

    public String getNaverStockNews(String stock_code) {

        String news_string="";

        try {
            String URL = "https://finance.naver.com/item/main.naver?code="+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements newsclass = doc.select(".news_section");
            Elements alist = newsclass.select("a");
            int size = alist.size();
            for(int i=0;i<size;i++)  {
                news_string += alist.get(i).text() + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return news_string;
    }
    public String getNaverCompanyInfo(String stock_code) {

        String company_string="";
        String ranking="";
        try {
            String URL = "https://finance.naver.com/item/coinfo.naver?code="+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements compinfo = doc.select(".tab_con1");
            Elements trlist = compinfo.select("tr");
            ranking = trlist.get(0).select("td").text() + " ";
            ranking += trlist.get(1).select("td").text()+"\n";

            Elements newsclass = doc.select(".summary_info");
            Elements plist = newsclass.select("p");
            int size = plist.size();
            for(int i=0;i<size;i++)  {
                company_string += plist.get(i).text() + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ranking + company_string;
    }

    public void checkbox_test1() throws IOException {

        final String urlPost = "https://finance.naver.com/sise/sise_group_detail.naver?type=upjong&no=278";
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:115.0) Gecko/20100101 Firefox/115.0";


        Document doc = Jsoup.connect(urlPost).get();

        Elements selectlist = doc.getElementsByClass("item_list");
        Elements allInputFields = selectlist.get(0).getElementsByTag("input");
        Elements allSelections = selectlist.get(0).getElementsByTag("input");
        Map<String, String> postData = new HashMap<String, String>();
        /*
        for(Element selectField:allSelectionFields){
            postData.put(selectField.attr("name"), selectField.attr("value"));
        }
         */
        for(Element selectField:allSelections){
            String nameField = selectField.attr("name");
            String valueField = "";
            Elements allOptions = selectField.getElementsByTag("option");
            for(Element opt:allOptions){
                if(opt.attr("selected").equalsIgnoreCase("selected")){
                    valueField = opt.attr("value");
                    break;
                }
            }
            postData.put(nameField, valueField);
        }

        for(Element inputField:allInputFields) {
            postData.put(inputField.attr("value"), "0");
        }

        for(Element inputField:allInputFields) {
            if (inputField.attr("type").equalsIgnoreCase("checkbox")) {
                if(inputField.attr("id").equalsIgnoreCase("option5")) {
                    postData.put(inputField.attr("value"), inputField.attr("checked").equalsIgnoreCase("checked") ? "0" : "1");
                } else {
                    //postData.put(inputField.attr("value"), inputField.attr("value"));
                    postData.put(inputField.attr("value"), "0");
                }
            }
        }

        Document doc2 = Jsoup.connect(urlPost).ignoreContentType(true).userAgent(USER_AGENT).data(postData).post();

        Elements selectlist1 = doc.getElementsByClass("item_list");
        Elements allInputFields1 = selectlist1.get(0).getElementsByTag("label");
        Elements allSelections1 = selectlist1.get(0).getElementsByTag("input");

        int i = 0;

    }

    public void checkbox_test(String upjong_code,String upjong_name) throws IOException {

        final String urlPost = "https://finance.naver.com/sise/sise_group_detail.naver?type=upjong&no="+upjong_code;
        //System.setProperty("webdriver.gecko.driver","d:\\driver\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "d:\\driver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        options.addArguments("headless"); // 크롬을 열지 않고 실행

        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPost);

        for(int i=1;i<=27;i++) {
            WebElement option = driver.findElement(By.id("option"+Integer.toString(i)));
            if(option.isSelected()) option.click();
        }
        int index;
        index = cboxname("시가총액"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("영업이익"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("영업이익증가율"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("당기순이익"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("매출액"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("매출액증가율"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        driver.findElement(By.xpath("//a[@href='javascript:fieldSubmit()']")).click();

        List<WebElement> mytbody = driver.findElements(By.tagName("tbody"));
        List<WebElement> trlist = mytbody.get(2).findElements(By.tagName("tr"));
        List<FormatUpjongInfo> upjonglist = new ArrayList<>();
        int size = trlist.size();
        for(int i=0;i<trlist.size()-10;i++) {
            FormatUpjongInfo one = new FormatUpjongInfo();
            List<WebElement> tdlist = trlist.get(i).findElements(By.tagName("td"));
            one.stock_name = tdlist.get(0).getText().replace(" *","");
            one.cur_price = tdlist.get(1).getText();
            one.compare_yester = tdlist.get(2).getText();
            one.updown_ratio = tdlist.get(3).getText();

            one.market_volume = tdlist.get(4).getText();
            one.profit = tdlist.get(5).getText();
            one.profit_ratio = tdlist.get(6).getText();
            one.quarter_pureprofit = tdlist.get(7).getText();
            one.revenue = tdlist.get(8).getText();
            one.revenue_ratio = tdlist.get(9).getText();
            upjonglist.add(one);
            System.out.println(i + " " + one.stock_name);
        }
        MyExcel myexcel = new MyExcel();
        myexcel.writenaverupjong("info_"+upjong_name,upjonglist );
    }

    public void getNaverUpjong(String upjong_code,String upjong_name, int max) {

        StockDic mydict = new StockDic();
        final String urlPost = "https://finance.naver.com/sise/sise_group_detail.naver?type=upjong&no="+upjong_code;
        //System.setProperty("webdriver.gecko.driver","d:\\driver\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "d:\\driver\\chromedriver.exe");

        _cb.callback("다운로드" + "\n" + "준비중");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        options.addArguments("headless"); // 크롬을 열지 않고 실행

        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPost);

        for(int i=1;i<=27;i++) {
            WebElement option = driver.findElement(By.id("option"+Integer.toString(i)));
            if(option.isSelected()) option.click();
        }
        int index;
        index = cboxname("시가총액"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("영업이익"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("영업이익증가율"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("당기순이익"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("매출액"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        index = cboxname("매출액증가율"); driver.findElement(By.id("option"+Integer.toString(index))).click();
        driver.findElement(By.xpath("//a[@href='javascript:fieldSubmit()']")).click();

        List<WebElement> mythead = driver.findElements(By.tagName("thead"));
        List<WebElement> thlist = mythead.get(0).findElements(By.tagName("th"));
        List<String> header = new ArrayList<>();
        int size = thlist.size();
        for(int i =0;i<size;i++) {
            header.add(thlist.get(i).getText());
        }
        header.add(0,"종목코드"); // 종목코드는 웹페이지에 없기 때문에 별도로 추가해준다

        List<WebElement> mytbody = driver.findElements(By.tagName("tbody"));
        List<WebElement> trlist = mytbody.get(2).findElements(By.tagName("tr"));
        List<List<String>> upjonglist = new ArrayList<List<String>>();

        if(max == -1 ) size = trlist.size();
        else size = max;

        String sizestr = Integer.toString(size);
        for(int i=0;i<size;i++) {
            List<String> one = new ArrayList<>();
            List<WebElement> tdlist = trlist.get(i).findElements(By.tagName("td"));
            one.add(tdlist.get(0).getText().replace(" *",""));
            int size2 = tdlist.size();
            for(int j = 1;j<size2;j++) {
                one.add(tdlist.get(j).getText());
            }
            System.out.println(i + " " + one.get(0));
            _cb.callback("download" + "\n" +Integer.toString(i)+"/"+sizestr +"\n" + one.get(0));
            String stock_code = mydict.getStockcode(one.get(0));
            one.add(0, stock_code);
            upjonglist.add(one);
        }
        MyExcel myexcel = new MyExcel();
        upjonglist.add(0,header);
        FormatStockInfo oneinfo = new FormatStockInfo();
        myexcel.writenaverupjong2("group_"+upjong_name,upjonglist );
        myexcel.writenaverupjong2("info_"+upjong_name,upjonglist );
    }
    
    public int cboxname(String name) {

        String checkname[] = {"거래량","매수호가","거래대금","시가총액","영업이익","PER",
                                "시가","매도호가","전일거래량","자산총액","영업이익증가율","ROE",
                                "고가","매수총잔량","외국인비율","부채총계","당기순이익","ROA",
                                "저가", "매도총잔량","상장주식수","매출액","주당순이익","PBR",
                                "매출액증가율","보통주배당금","유보율"};
        int result=0;
        for(int i =0;i<27;i++) {
            if(name.equals(checkname[i])) { result = i; break;}
        }
        return result+1;
    }


    public List<List<String>> getNaverGoodEarningStock(String count, boolean header_flag) {

        final String urlPost = "https://finance.naver.com/sise/sise_market_sum.naver?&page="+count;
        //System.setProperty("webdriver.gecko.driver","d:\\driver\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "d:\\driver\\chromedriver.exe");

        List<List<String>> upjonglist = new ArrayList<List<String>>();
        List<String> header = new ArrayList<>();

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        options.addArguments("headless"); // 크롬을 열지 않고 실행

        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPost);

        for (int i = 1; i <= 27; i++) {
            WebElement option = driver.findElement(By.id("option" + Integer.toString(i)));
            if (option.isSelected()) option.click();
        }
        int index;
        index = cboxname("시가총액");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        index = cboxname("영업이익");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        index = cboxname("영업이익증가율");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        index = cboxname("당기순이익");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        index = cboxname("매출액");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        index = cboxname("매출액증가율");
        driver.findElement(By.id("option" + Integer.toString(index))).click();
        driver.findElement(By.xpath("//a[@href='javascript:fieldSubmit()']")).click();


        if (header_flag != false) {
            List<WebElement> mythead = driver.findElements(By.tagName("thead"));
            List<WebElement> thlist = mythead.get(0).findElements(By.tagName("th"));
            int size = thlist.size();
            for (int i = 0; i < size; i++) {
                header.add(thlist.get(i).getText());
            }
        }
        List<WebElement> mytbody = driver.findElements(By.tagName("tbody"));
        List<WebElement> trlist = mytbody.get(1).findElements(By.tagName("tr"));

        int size = trlist.size();
        for (int i = 0; i < trlist.size(); i++) {
            List<String> one = new ArrayList<>();
            List<WebElement> tdlist = trlist.get(i).findElements(By.tagName("td"));
            one.add(tdlist.get(0).getText().replace(" *", ""));
            int size2 = tdlist.size();
            for (int j = 1; j < size2; j++) {
                one.add(tdlist.get(j).getText());
            }
            System.out.println(i + " " + one.get(0));
            if (one.size() > 2) upjonglist.add(one);
        }

        if(header_flag != false) upjonglist.add(0,header);
        return upjonglist;
    }


    public List<List<String>> getNaverGoodEarningETF() {

        final String urlPost = "https://finance.naver.com/sise/etf.naver";
        //System.setProperty("webdriver.gecko.driver","d:\\driver\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "d:\\driver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
        options.addArguments("headless"); // 크롬을 열지 않고 실행

        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPost);

        WebElement tables = driver.findElement(By.tagName("table"));

        //String tablestr = tables.getText();
        WebElement tbody = tables.findElement(By.tagName("tbody"));

        List<String> header = new ArrayList<>();
        List<WebElement> thlists = tbody.findElements(By.tagName("th"));
        int size3 = thlists.size();
        for(int i =0;i<size3;i++) {
            header.add(thlists.get(i).getText());
        }

        List<List<String>> etflist = new ArrayList<List<String>>();
        List<WebElement> trlists = tbody.findElements(By.tagName("tr"));
        int size1 = trlists.size();
        for(int i =0;i<size1;i++) {
            List<String> onelist = new ArrayList<>();
            List<WebElement> tdlists = trlists.get(i).findElements(By.tagName("td"));
            int size2 = tdlists.size();
            if(size2 <= 2) continue;
            for(int j =0;j<size2;j++) {
                onelist.add(tdlists.get(j).getText());
            }
            etflist.add(onelist);
            System.out.println(onelist.toString());
        }
        etflist.add(0,header);
        return etflist;
    }
}
