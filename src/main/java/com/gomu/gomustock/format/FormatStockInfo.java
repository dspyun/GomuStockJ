package main.java.com.gomu.gomustock.format;

public class FormatStockInfo {
    public String stock_code;
    public String stock_name;
    public String data;
    public String per;
    public String per12;
    public String area_per;
    public String pbr;
    public String div_rate;

    public String fogn_rate;

    public String beta;
    public String op_profit;
    public String cur_price;

    public String score;     // 이것은 파일로 저장하지 안는다. 웹에서 긁어오는 것이 이님.

    public String desc;
    public FormatStockInfo() {
        init();
    }
    public void init() {
        stock_code="";
        stock_name="";
        per="";
        per12="";
        area_per="";
        pbr="";
        div_rate="";
        fogn_rate = "";
        beta="";
        op_profit="";
        cur_price="";
        score="";
        desc="";
    }

    public String toString() {
        String stock_info="";
        stock_info += "PER/PBR = " + per +"/"+pbr + "\n";
        stock_info += "PER12/업종PER = "+ per12 +"/"+area_per + "\n";
        stock_info += "배당률/베타 = "+div_rate +"/"+beta + "\n";
        stock_info += "외국인비중 = " + fogn_rate+"\n";
        stock_info += "영업이익률 = "+ op_profit+"\n";
        return stock_info;
    }

    public void setHeader() {
        stock_code="Code";
        stock_name="Name";
        per="PER";
        per12="PER12";
        area_per="업종PER";
        pbr="PBR";
        div_rate="배당률";
        fogn_rate = "외국인지분";
        beta="Beta";
        op_profit="영업이익";
        cur_price="현재가";
        score="시그널";
        desc="정보";
    }

    public void addStockcode(String stockcode) {
        stock_code = stockcode;
    }
}
