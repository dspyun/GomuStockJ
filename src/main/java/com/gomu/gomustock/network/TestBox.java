package main.java.com.gomu.gomustock.network;

import main.java.com.gomu.gomustock.MyExcel;

import main.java.com.gomu.gomustock.format.FormatTestData;

import java.util.ArrayList;
import java.util.List;

public class TestBox {

    String STOCK_CODE;
    int ONEYEAR = -1;
    List<String> DATE = new ArrayList<>();
    List<Float> CLOSE = new ArrayList<>();
    List<Integer> BUY = new ArrayList<>();
    List<Integer> SELL = new ArrayList<>();
    List<FormatTestData> TESTDATA = new ArrayList<>();

    public TestBox(String stock_code) {
        STOCK_CODE = stock_code;
        MyExcel myexcel = new MyExcel();
        TESTDATA = myexcel.readtestset(STOCK_CODE,false);
        loadDate();
        loadClose();
        loadBuy();
        loadSell();
    }

    public List<Float> getClose() {
        return CLOSE;
    }
    public List<String> getDate() {
        return DATE;
    }
    public List<Integer> getBuy() {
        return BUY;
    }
    public List<Integer> getSell() {
        return SELL;
    }

    void loadDate() {
        int size = TESTDATA.size();
        for(int i =0;i<size;i++) {
            DATE.add(TESTDATA.get(i).date);
        }
    }

    void loadClose() {
        int size = TESTDATA.size();
        String close;
        for(int i =0;i<size;i++) {
            close = TESTDATA.get(i).price;
            CLOSE.add(Float.parseFloat(close));
        }
    }

    void loadBuy() {
        int size = TESTDATA.size();
        String buy;
        for(int i =0;i<size;i++) {
            buy = TESTDATA.get(i).buy_quantity;
            BUY.add(Integer.parseInt(buy));
        }
    }
    void loadSell() {
        int size = TESTDATA.size();
        String sell;
        for(int i =0;i<size;i++) {
            sell = TESTDATA.get(i).sell_quantity;
            SELL.add(Integer.parseInt(sell));
        }
        int j = 0;
    }
}
