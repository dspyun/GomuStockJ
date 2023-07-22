package main.java.com.gomu.gomustock.format;

public class FormatETFInfo {
    public String stock_name;
    public String stock_code;
    public String desc;
    public String market_sum;
    public String fund_fee; // etf 정보
    public String nav; // etf 정보
    public String m1_profit_rate; // etf 정보
    public String m3_profit_rate; // etf 정보
    public String m6_profit_rate; // etf 정보
    public String y1_profit_rate; // etf 정보
    public String news;
    public String fninfo;

    public void init() {
        fund_fee=""; // etf 정보
        nav=""; // etf 정보
        m1_profit_rate=""; // etf 정보
        m3_profit_rate=""; // etf 정보
        m6_profit_rate=""; // etf 정보
        y1_profit_rate=""; // etf 정보
    }
    @Override
    public String toString() {
        String result="";
        result += stock_name + "("+stock_code+")"+ "\n";
        result += "시가총액 " + market_sum +"\n";
        result += "펀드보수 " + fund_fee +"\n";
        result += "NAV " + nav +"\n";
        result += "1개월 수익률 " + m1_profit_rate +"\n";
        result += "3개월 수익률 " + m3_profit_rate +"\n";
        result += "6개월 수익률 " + m6_profit_rate +"\n";
        result += "1년 수익률 " + y1_profit_rate +"\n";
        return result;
    }
}
