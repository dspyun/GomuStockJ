package main.java.com.gomu.gomustock.format;

public class FormatStockInfo {
    public String stock_code;
    public String stock_name;
    public String stock_type;
    public String data;
    public String market_sum;
    public String ranking;
    public String per;
    public String expect_per;
    public String per12;
    public String area_per;
    public String pbr;
    public String div_rate;
    public String recommend;
    public String fogn_rate;
    public String beta;
    public String op_profit;
    public String cur_price;
    public String score;     // 이것은 파일로 저장하지 안는다. 웹에서 긁어오는 것이 이님.
    public String desc;
    public String news;
    public String fninfo;
    public String etfinfo;
    public String nav;

    public FormatStockInfo() {
        init();
    }
    public void init() {
        stock_code="";
        stock_name="";
        stock_type="";
        market_sum="";
        ranking="";
        per="";
        per12="";
        expect_per="";
        area_per="";
        pbr="";
        div_rate="";
        fogn_rate = "";
        beta="";
        op_profit="";
        cur_price="";
        score="";
        desc="";
        fninfo="";
        etfinfo="";
        nav="";
    }

    public void fill_empty() {

        market_sum="시가총액";
        ranking="총액순위";
        recommend="투자의견";
        per="PER";
        expect_per="예상PER";
        per12="PER12";
        area_per="업종PER";
        pbr="PBR";
        div_rate="배당률";
        fogn_rate = "외국인지분";
        cur_price="현재가";
        score="시그널";
        desc="정보";
        news="뉴스";
        fninfo="수익테이블";
        etfinfo="etf정보";
        nav="nav";
    }

    public String getDSC() {
        return desc;
    }


    public String toString() {
        String stock_info="";
        stock_info += stock_name + " " + stock_code + "\n";
        stock_info += ranking + ", 배당률 = " +div_rate + "\n";
        stock_info += "투자의견 = "+ recommend + "\n";
        stock_info += "PER = " + per + "\n";
        stock_info += "예상PER = " + expect_per + "\n";
        stock_info += "업종PER = " + area_per + "\n";
        return stock_info;
    }

    public void setHeader() {
        stock_code="Code";
        stock_name="Name";
        stock_type="Type";
        market_sum="시가총액";
        ranking="총액순위";
        recommend="투자의견";
        per="PER";
        expect_per="예상PER";
        per12="PER12";
        area_per="업종PER";
        pbr="PBR";
        div_rate="배당률";
        fogn_rate = "외국인지분";
        cur_price="현재가";
        score="시그널";
        desc="정보";
        news="뉴스";
        fninfo="fn정보";
        etfinfo="ETF정보";
        nav="nav";
    }

    public void addStockcode(String stockcode) {
        stock_code = stockcode;
    }
}
