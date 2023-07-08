package main.java.com.gomu.gomustock.network;


import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;

import java.util.ArrayList;
import java.util.List;

public class StockBox {

    String STOCK_CODE;
    int ONEYEAR = -1;
    List<Float> CLOSEPRICE = new ArrayList<>();
    List<String> DATE = new ArrayList<>();

    public StockBox(String stock_code) {
        STOCK_CODE = stock_code;
        loadAdjclose();
        loadDate();
    }

    public List<Float> getClose() {
        return CLOSEPRICE;
    }
    public List<String> getDate() {
        return DATE;
    }

    void loadAdjclose() {
        List<String> closeprice = new ArrayList<>();
        MyExcel myexcel = new MyExcel();
        MyStat mystat = new MyStat();
        closeprice = myexcel.read_ohlcv(STOCK_CODE,"CLOSE",ONEYEAR,false);
        CLOSEPRICE = mystat.string2float(closeprice,1);
    }
    void loadDate() {
        MyExcel myexcel = new MyExcel();
        DATE = myexcel.read_ohlcv(STOCK_CODE,"DATE",ONEYEAR,false);
    }

}
