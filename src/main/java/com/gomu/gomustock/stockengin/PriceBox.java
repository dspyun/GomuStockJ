package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;
import main.java.com.gomu.gomustock.format.FormatOHLCV;

import java.util.ArrayList;
import java.util.List;

public class PriceBox {


    String STOCK_CODE;
    int ONEYEAR = -1;
    int MINSIZE = 30;
    List<Float> CLOSEPRICE = new ArrayList<>();
    List<Float> CLOSESTDPRICE = new ArrayList<>();
    List<Float> HIGHPRICE = new ArrayList<>();
    List<Float> LOWPRICE = new ArrayList<>();
    MyExcel myexcel = new MyExcel();
    List<String> DATE = new ArrayList<>();

    public PriceBox(String stock_code) {
        STOCK_CODE = stock_code;
        loadExcelData();
    }

    void loadExcelData() {
        List<FormatOHLCV> ohlcvlist = new ArrayList<>();
        ohlcvlist = myexcel.readall_ohlcv(STOCK_CODE);
        int size = ohlcvlist.size();
        FormatOHLCV preoneohlcv = new FormatOHLCV();
        for(int i=0;i<size;i++) {
            FormatOHLCV oneohlcv = ohlcvlist.get(i);
            if(nullcheck(oneohlcv)) oneohlcv = preoneohlcv;
            CLOSEPRICE.add(Float.parseFloat(oneohlcv.close));
            DATE.add(oneohlcv.date);
            HIGHPRICE.add(Float.parseFloat(oneohlcv.high));
            LOWPRICE.add(Float.parseFloat(oneohlcv.low));
            preoneohlcv = oneohlcv;
        }
    }
    boolean nullcheck(FormatOHLCV data) {
        if(data.close.equals("null")) return true;
        return false;
    }

    public List<Float> getHigh() {
        return HIGHPRICE;
    }
    public List<Float> getLow() {
        return LOWPRICE;
    }
    public List<Float> getClose() {
        return CLOSEPRICE;
    }
    public List<Float> getClose(int days) {
        List<Float>closeprice=new ArrayList<>();
        if(CLOSEPRICE.size()>=MINSIZE && CLOSEPRICE.size() >= days) {
            int size = CLOSEPRICE.size();
            for (int i = 0; i < days; i++) {
                closeprice.add(CLOSEPRICE.get(size - days + i));
            }
        } else if(CLOSEPRICE.size()>= MINSIZE && days > CLOSEPRICE.size()) {
            closeprice = CLOSEPRICE;
        } else  {
            for(int i =0;i<days;i++) {
                closeprice.add(0f);
            }
        }
        return closeprice;
    }
    public List<Float> getStdClose(int days) {
        MyExcel myexcel = new MyExcel();
        List<Float>closeprice=new ArrayList<>();
        int size = CLOSEPRICE.size()-1;
        for(int i =0;i<days;i++) {
            closeprice.add(CLOSEPRICE.get(size-days+i));
        }
        CLOSESTDPRICE = myexcel.standardization_lib(closeprice);
        return CLOSESTDPRICE;
    }
    public List<String> getDate() {
        return DATE;
    }
    public String getStockCode() { return STOCK_CODE;}
    public boolean checkEmpty() {
        boolean flag;
        if(CLOSEPRICE.size() <= 1) {
            flag = true;
        }
        else {
            flag = false;
        }

        return flag;
    }
}
