package main.java.com.gomu.gomustock.network;

import main.java.com.gomu.gomustock.MyDate;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;


import main.java.com.gomu.gomustock.format.FormatKOSPI;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
import main.java.com.gomu.gomustock.format.FormatSector;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyWeb {


    String target_stock ="";
    public String result ="";

    public MyWeb() {

    }
    public MyWeb(String stock_no) {
        target_stock = stock_no;
        target_stock = "005930";
    }

    public FormatStockInfo getStockinfo(String stock_code) {
        FormatStockInfo result = new FormatStockInfo();
        try {

            String URL = "https://comp.fnguide.com/SVO2/ASP/SVD_main.asp?gicode=A"+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements classinfo0 = doc.select(".corp_group1");
            Element giname = classinfo0.select("#giName").get(0);
            result.stock_name = giname.text();

            Elements classinfo1 = doc.select("#corp_group2");
            Elements dd_list = classinfo1.select("dd");

            result.per = dd_list.get(1).text();
            result.per12 = dd_list.get(3).text();
            result.area_per = dd_list.get(5).text();
            result.pbr = dd_list.get(7).text();
            result.div_rate = dd_list.get(9).text();

            Elements classinfo2 = doc.select("#svdMainGrid1");
            Elements tbody_list = classinfo2.select("tbody");
            Elements tr_list = tbody_list.select("tr");;
            Elements td_list = tr_list.get(2).select("td");;
            Element th1 = td_list.get(1);;
            result.fogn_rate = th1.text();;

            Elements td2_list = tr_list.get(3).select("td");;
            Element th2 = td2_list.get(1);;
            result.beta = th2.text();

            Elements classinfo3 = doc.select("#svdMainGrid2");
            Elements tbody1_list = classinfo3.select("tbody");
            Elements td1_list = tbody1_list.select("td");;
            result.op_profit = td1_list.get(3).text();;

/*
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
        return result;
    }

    public List<List<String>> getAgencyFogn(String stock_code, String pageno) {

        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();
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
        List<List<String>> result = new ArrayList<List<String>>();
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

        try {
            doc = Jsoup.connect(URL).get();
            Elements elem = doc.select(".date");
            String[] str = elem.text().split(" ");

            Elements todaylist =doc.select(".new_totalinfo dl>dd");

            String juga = todaylist.get(3).text().split(" ")[1];

            String DungRakrate = todaylist.get(3).text().split(" ")[6];
            String siga =  todaylist.get(5).text().split(" ")[1];
            String goga = todaylist.get(6).text().split(" ")[1];
            String zeoga = todaylist.get(8).text().split(" ")[1];
            String georaeryang = todaylist.get(10).text().split(" ")[1];

            String stype = todaylist.get(3).text().split(" ")[3]; //상한가,상승,보합,하한가,하락 구분

            String vsyesterday = todaylist.get(3).text().split(" ")[4];

            stockprice = juga;
            //System.out.println(stockcode + " 주가 : "+juga);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stockprice;
    }

    public String getCurrentKospi200() {

        //https://jul-liet.tistory.com/209
        String stockprice="";
        String URL = "https://finance.naver.com/sise/sise_index.naver?code=KPI200";
        Document doc;

        try {
            doc = Jsoup.connect(URL).get();

            Elements todaylist =doc.select(".subtop_sise_detail");

            Element tdlist = todaylist.select("tr").get(0);
            Element kospi200 = tdlist.select("td").get(0);
            return kospi200.text();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("반환값 "+stockprice);

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
       // System.out.println("반환값 "+stockprice);

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



    public List<FormatKOSPI> getKOSPI6(int inptu_no) {

        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();
        List<FormatKOSPI> kospilist = new ArrayList<>();
        String pageno = Integer.toString(inptu_no);
        try {
            String pagenumber;
            if(pageno.equals("0")) pagenumber="";
            else pagenumber = "&page="+pageno;
            String URL = "https://finance.naver.com/sise/sise_index_day.naver?code=KOSPI"+pagenumber;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements upperclass = doc.select(".box_type_m");
            Elements trlist = upperclass.select("tr");
            for(int i =2;i<5;i++ ){
                FormatKOSPI kospi = new FormatKOSPI();
                Elements tdlist = trlist.get(i).select("td");
                kospi.date = tdlist.get(0).text().replaceAll("\\.","");
                kospi.price = tdlist.get(1).text().replaceAll(",", "");
                kospi.diff = tdlist.get(2).text().replaceAll(",", "");
                kospi.rate = tdlist.get(3).text().replaceAll(",", "");
                kospi.volume = tdlist.get(4).text().replaceAll(",", "");
                kospi.amount = tdlist.get(5).text().replaceAll(",", "");
                kospilist.add(kospi);
            }
            for(int i =9;i<12;i++ ){
                FormatKOSPI kospi = new FormatKOSPI();
                Elements tdlist = trlist.get(i).select("td");
                kospi.date = tdlist.get(0).text().replaceAll("\\.","");
                kospi.price = tdlist.get(1).text().replaceAll(",", "");
                kospi.diff = tdlist.get(2).text().replaceAll(",", "");
                kospi.rate = tdlist.get(3).text().replaceAll(",", "");
                kospi.volume = tdlist.get(4).text().replaceAll(",", "");
                kospi.amount = tdlist.get(5).text().replaceAll(",", "");
                kospilist.add(kospi);
            }

            int j = 0;
    } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return kospilist;
    }

    public List<FormatKOSPI> getKOSPI(int days) {
        List<FormatKOSPI> kospilist = new ArrayList<>();
        int total_page = days/6;
        for(int i =0;i<total_page;i++) {
            kospilist.addAll(getKOSPI6(i));
        }
        return kospilist;
    }

    public List<Float> getKOSPIPrice(int days) {
        List<FormatKOSPI> kospilist = new ArrayList<>();
        kospilist = getKOSPI(240);
        List<Float> pricelist = new ArrayList<>();
        int size = kospilist.size();
        for(int i =0;i<size;i++) {
            pricelist.add(20*Float.parseFloat(kospilist.get(i).price));
        }
        return pricelist;
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
            result.recommend = trlist.get(6).select("td").get(0).text();

            result.per = trlist.get(9).select("td").get(0).text();
            result.expect_per = trlist.get(10).select("td").get(0).text();
            result.pbr = trlist.get(11).select("td").get(0).text();
            result.div_rate = trlist.get(12).select("td").get(0).text();
            result.area_per = trlist.get(13).select("td").get(0).text();
            //result.op_profit = td1_list.get(3).text();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
