package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairTest {

    List<Float> SRC_CLOSE = new ArrayList<>();
    List<Float> BASE_CLOSE = new ArrayList<>();
    List<Float> SRC_STD_CLOSE = new ArrayList<>();
    List<Float> BASE_STD_CLOSE = new ArrayList<>();
    int ONEYEAR = -1;
    MyExcel myexcel = new MyExcel();;
    String STOCK_CODE;

    boolean singlemode;
    public PairTest (String src_code, List<Float> src_close, List<Float> base_close) {
        singlemode = false;
        this.STOCK_CODE = src_code;
        this.SRC_CLOSE=src_close;
        this.BASE_CLOSE=base_close;
    }

    public void standardize() {
        MyStat mystat = new MyStat();
        SRC_STD_CLOSE = mystat.standardization_lib(SRC_CLOSE);
        BASE_STD_CLOSE = mystat.standardization_lib(BASE_CLOSE);
    }

    public List<Float> test_line(List<Float> src1, List<Float> src2) {
        // 두 개 데이터의 차이곡선을 test line으로 생성한다
        List<Float> result = new ArrayList<>();
        for(int i =0;i<src1.size();i++) {
            float value=0;
            value = src1.get(i)-src2.get(i);
            result.add(value);
        }
        return result;
    }

    public void testNfile(List<Float> src1, List<Float> src2) {
        List<Float> pair_buyscore = new ArrayList<>();
        List<Integer> buy_score = new ArrayList<>();
        List<Integer> sell_score = new ArrayList<>();
        float temp = 0;

        pair_buyscore = test_line(src1, src2);
        for(int i = 0; i< pair_buyscore.size(); i++) {
            buy_score.add(0);
            sell_score.add(0);
        }

        for(int i = 0; i< pair_buyscore.size(); i++) {
            temp = pair_buyscore.get(i);
            if(temp >= 0.5 && temp < 1) sell_score.set(i,1);
            else if(temp >= 1) sell_score.set(i,2);

            if(temp <= -0.5 && temp > -1) buy_score.set(i,1);
            else if(temp <= -1) buy_score.set(i,3);
        }
        List<String> close = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR,false);
        List<String> date = myexcel.read_ohlcv(STOCK_CODE, "DATE", ONEYEAR,false);
        myexcel.write_testdata(STOCK_CODE,date,close, buy_score, sell_score);
    }

    public void makeBackdataDouble() {
        standardize();
        testNfile(SRC_STD_CLOSE, BASE_STD_CLOSE);
    }
}
