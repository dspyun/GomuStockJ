package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.network.TestBox;

import java.util.ArrayList;
import java.util.List;

public class Balance {

    String STOCK_CODE;
    MyExcel myexcel = new MyExcel();
    TestBox TESTBOX;
    List<Float> BUYLINE = new ArrayList<>();
    List<Float> SELLLINE = new ArrayList<>();
    float BALANCE=0;
    float TOTAL_BUY_PRICE=0, TOTAL_SELL_PRICE=0;
    float TOTAL_BUY_QUANTITY=0, TOTAL_SELL_QUANTITY=0;
    float HOLD_MONEY;
    float LAST_HOLD_QUANTITY;

    public Balance(String stock_code, float cur_price) {
        STOCK_CODE = stock_code;
        TESTBOX = new TestBox(stock_code);
        makeBuyhistory();
        makeSellhistory();
        makeTestResult();
    }

    public List<Float> getBuyline() {
        return BUYLINE;
    }
    public List<Float> getSellline() {
        return SELLLINE;
    }
    public Float getProfitRate() { return BALANCE/TOTAL_BUY_PRICE; }
    public Float getProfit() { return BALANCE; }
    public Float getAVERPrice() { return TOTAL_BUY_PRICE/TOTAL_BUY_QUANTITY; }
    public Float getTotalBuyCost() { return TOTAL_BUY_PRICE; }

    void makeBuyhistory() {
        int size = TESTBOX.getBuy().size();
        float buyprice;
        for(int i =0;i<size;i++) {
            buyprice =   TESTBOX.getBuy().get(i) * TESTBOX.getClose().get(i);
            BUYLINE.add(buyprice);
        }
    }

    void makeSellhistory() {
        int size = TESTBOX.getSell().size();
        float sellprice;
        for(int i =0;i<size;i++) {
            sellprice =   TESTBOX.getSell().get(i) * TESTBOX.getClose().get(i);
            SELLLINE.add(sellprice);
        }
    }

    void makeTestResult() {
        int size = TESTBOX.getSell().size();

        for(int i =0;i<size;i++) {
            TOTAL_BUY_PRICE = TOTAL_BUY_PRICE + BUYLINE.get(i);
            TOTAL_SELL_PRICE = TOTAL_SELL_PRICE + SELLLINE.get(i);
            TOTAL_BUY_QUANTITY = TOTAL_BUY_QUANTITY + TESTBOX.getBuy().get(i);
            TOTAL_SELL_QUANTITY = TOTAL_SELL_QUANTITY + TESTBOX.getSell().get(i);
        }

        // 1. 현재보유현금 = 원금 - 총매수액 + 총매도액 인데, 원금이 0원이니 아래와 같고
        float HOLD_MONEY = -TOTAL_BUY_PRICE + TOTAL_SELL_PRICE;
        // 2. 잔량주식가격 : 잔량*최신close
        float LAST_HOLD_QUANTITY = (TOTAL_BUY_QUANTITY-TOTAL_SELL_QUANTITY)*TESTBOX.getClose().get(size-1);
        // 최종손익 = 현금손익 + 잔량가격
        BALANCE = HOLD_MONEY + LAST_HOLD_QUANTITY;
        // 매수액, 평가액, 수익률, 평단가
        // 수익액 : BALANCE
        // 수익률 : 수익액/총매수액 = BALANCE/TOTAL_BUY_PRICE
        // 평단가 : 총매수액/총매수량 = TOTAL_BUY_PRICE/TOTAL_BUY_QUANTITY
        // 매수액 : TOTAL_BUY_PRICE
    }

}
