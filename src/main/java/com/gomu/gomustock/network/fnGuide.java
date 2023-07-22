package main.java.com.gomu.gomustock.network;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fnGuide {

    public fnGuide() {

    }
    public boolean checkKRStock(String stock_code) {
        // 숫자 스트링이면 true, 문자가 있으면 false를 반환한다.
        // 즉 한국주식이면 true, 외국주식이면 false 반환
        boolean isNumeric =  stock_code.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }
    class fninfo {
        List<String> header = new ArrayList<>(); // 평가시작
        List<String> revenue = new ArrayList<>(); // 매출액
        List<String> op_profit = new ArrayList<>(); // 영업이익
        List<String> net_income = new ArrayList<>(); // 당기순이익
        List<String> op_profit_rate = new ArrayList<>(); // 영업이익률
    }

    public String getFnguideInfo(String stock_code) {
        String result="";

        //System.out.println("stock_code = " + stock_code+"\n");
        if(!checkKRStock(stock_code)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            return result="empty";
        }
        try {
            fninfo myfninfo = new fninfo();
            String URL = "https://comp.fnguide.com/SVO2/ASP/SVD_main.asp?gicode=A"+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements id_IFRS = doc.select("#highlight_D_Q"); // 연결/분기테이블 id 가져오기
            if(id_IFRS.text().isEmpty()) {
                return result="empty";
            }
            Element fnthead = id_IFRS.select("thead").get(0);
            Elements thlist = fnthead.select("th");
            myfninfo.header.add(thlist.get(6).text());
            for(int i =7;i<10;i++) { // 매출액
                myfninfo.header.add(thlist.get(i).select("a").text());
            }

            Element fntbody = id_IFRS.select("tbody").get(0);
            Elements trlist = fntbody.select("tr");
            for(int i =4;i<8;i++) { // 매출액
                myfninfo.revenue.add(trlist.get(0).select("td").get(i).text());
            }
            for(int i =4;i<8;i++) { // 영업이익
                myfninfo.op_profit.add(trlist.get(1).select("td").get(i).text());
            }
            for(int i =4;i<8;i++) { // 당기순이익
                myfninfo.net_income.add(trlist.get(3).select("td").get(i).text());
            }
            for(int i =4;i<8;i++) { // 영업이익률
                myfninfo.op_profit_rate.add(trlist.get(14).select("td").get(i).text());
            }
            String header="", revenue="매출액 ",op_profit="영업이익 ", net_income="당기순이익 ", op_rate="영업이익률 ";
            for(int i=0;i<4;i++) {
                header += myfninfo.header.get(i) + " / ";
                revenue += myfninfo.revenue.get(i) + " / ";
                op_profit += myfninfo.op_profit.get(i) + " / ";
                net_income += myfninfo.net_income.get(i) + " / ";
                op_rate += myfninfo.op_profit_rate.get(i) + " / ";
            }

            Elements classinfo2 = doc.select("#svdMainGrid1");
            Elements tbody_list = classinfo2.select("tbody");
            Elements tr_list = tbody_list.select("tr");;
            Elements td2_list = tr_list.get(3).select("td");;
            Element th2 = td2_list.get(1);;
            String beta = th2.text();

            result = "베타 = " + beta + "\n";
            result += header + "\n" + revenue + "\n" + op_profit + "\n" + net_income + "\n" + op_rate;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    class fnetfinfo {
        String risk_info; // 평가시작
        String divide_turn; // 평가시작
        String divide_rate;
        List<String> op_profit = new ArrayList<>(); // 영업이익
        List<String> net_income = new ArrayList<>(); // 당기순이익
        List<String> op_profit_rate = new ArrayList<>(); // 영업이익률
    }
    public String getFnguideETFInfo(String stock_code) {
        String result="";

        //System.out.println("stock_code = " + stock_code+"\n");
        if(!checkKRStock(stock_code)) {
            // 외국주식이면 빈칸으로 채우고 건너뜀
            return result="empty";
        }
        try {
            fnetfinfo myfninfo = new fnetfinfo();
            String URL = "https://comp.fnguide.com/SVO2/ASP/SVD_main.asp?gicode=A"+stock_code;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements risklist = doc.select(".corp_group3"); // 위험등급정보
            myfninfo.risk_info = risklist.select("dt").get(0).text();
            myfninfo.risk_info += " " + risklist.select("dd").text();

            Elements dividelist = doc.select("#etfDivInfo1"); // 분배금현황
            Elements trlist = dividelist.select("tr");
            myfninfo.divide_turn = trlist.get(1).select("th").get(0).text();
            myfninfo.divide_turn += trlist.get(1).select("td").get(0).text();

            myfninfo.divide_rate = trlist.get(1).select("th").get(1).text();
            myfninfo.divide_rate += trlist.get(1).select("td").get(1).text();

            result = myfninfo.risk_info + "\n";
            result += myfninfo.divide_turn + "\n";
            result += myfninfo.divide_rate + "\n";

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
