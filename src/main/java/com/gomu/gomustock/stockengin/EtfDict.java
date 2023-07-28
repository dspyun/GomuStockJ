package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;

import java.util.ArrayList;
import java.util.List;

public class EtfDict {

    List<String> STOCK_CODE = new ArrayList<String>();
    List<String> STOCK_NAME = new ArrayList<String>();
    List<String> BASEIDX = new ArrayList<String>();
;
    public EtfDict() {
        List<List<String>> result = new ArrayList<List<String>>();
        MyExcel myexcel = new MyExcel();
        result = myexcel.readETFDict();
        STOCK_CODE = result.get(0);
        STOCK_NAME = result.get(1);
    }

    public String getStockname(String stock_code) {
        int index = STOCK_CODE.indexOf(stock_code);
        if(index == -1) return "";
        return STOCK_NAME.get(index);
    }
    public String getStockcode(String stock_name) {
        int index = STOCK_NAME.indexOf(stock_name);
        if(index == -1) return "";
        return STOCK_CODE.get(index);
    }
    public boolean checkKRStock(String stock_code) {
        // 숫자 스트링이면 true, 문자가 있으면 false를 반환한다.
        // 즉 한국주식이면 true, 외국주식이면 false 반환
        boolean isNumeric =  stock_code.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }
}
