package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;

import java.util.ArrayList;
import java.util.List;

public class StockDic {
    List<String> STOCK_NO = new ArrayList<String>();
    List<String> STOCK_NAME = new ArrayList<String>();
    List<String> MARKET = new ArrayList<String>();
    public StockDic() {
        List<List<String>> result = new ArrayList<List<String>>();
        MyExcel myexcel = new MyExcel();
        result = myexcel.readStockDic();
        STOCK_NO = result.get(0);
        STOCK_NAME = result.get(1);
        MARKET = result.get(2);
    }
    public String getMarket(String stock_code) {
        int index = STOCK_NO.indexOf(stock_code);
        if(index == -1) return "";
        return MARKET.get(index);
    }
    public String getStockname(String stock_code) {
        int index = STOCK_NO.indexOf(stock_code);
        if(index == -1) return "";
        return STOCK_NAME.get(index);
    }
    public String getStockcode(String stock_name) {
        int index = STOCK_NAME.indexOf(stock_name);
        if(index == -1) return "";
        return STOCK_NO.get(index);
    }
    public boolean checkKRStock(String stock_code) {
        // 숫자 스트링이면 true, 문자가 있으면 false를 반환한다.
        // 즉 한국주식이면 true, 외국주식이면 false 반환
        boolean isNumeric =  stock_code.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }

}
