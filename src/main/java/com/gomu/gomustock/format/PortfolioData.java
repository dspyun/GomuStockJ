package main.java.com.gomu.gomustock.format;

import java.io.Serializable;

public class PortfolioData implements Serializable {
    // 종목명, 평가손익, 평가금액, 현재가
    // 보유수량, 수익률, 매입금액, 평균단가

    public String transaction_type; // 거래형태 매수 or 매도
    public String stock_name; // 종목명
    public int estim_profit; // 평가손익
    public int estim_price; // 평가금액
    public int cur_price; // 현재가
    public int hold_quantity; // 보유수량
    public double profit_rate; // 수익률
    public int buy_price; // 매입금액
    public int ave_price; // 평균단가

    public void reset() {
        stock_name="";
        estim_profit=0;
        estim_price=0;
        cur_price=0;
        hold_quantity=0;
        profit_rate=0;
        buy_price=0;
        ave_price=0;
    }
}
