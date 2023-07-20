package main.java.com.gomu.gomustock.stockengin;

import static com.tictactec.ta.lib.MAType.Sma;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatTestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BBandTest {

    TAlib mytalib = new TAlib();
    MyExcel myexcel = new MyExcel();
    String STOCK_CODE, STOCK_NAME;
    List<Float>  CLOSEDATA = new ArrayList<>();
    RSITest rsitest;
    int ONEYEAR = -1;
    List<Float> UPPERLINE = new ArrayList<>();
    List<Float> MIDDLELINE = new ArrayList<>();
    List<Float> LOWLINE = new ArrayList<>();
    List<Float> PERCENTB = new ArrayList<>();
    List<Integer> SELLSCORE = new ArrayList<>();
    List<Integer> BUYSCORE = new ArrayList<>();

    int CUR_PRICE;
    public List<Float> getClosePirce() {
        //List<String> close_str = new ArrayList<>();
        //close_str = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR, false);
        //CLOSEDATA = myexcel.string2float(close_str,1);
        return CLOSEDATA;
    }
    public List<Float> getUpperLine() {
        return  UPPERLINE;
    }
    public List<Float> getMiddleLine() {
        return  MIDDLELINE;
    }
    public List<Float> getLowLine() {
        return LOWLINE;
    }
    public String getStock_code() { return STOCK_CODE; }
    public String getStock_name() { return STOCK_NAME; }
    public int getCurPrice() {
        return CUR_PRICE;
    }
    public void putCurPrice(int cur_price) {
        CUR_PRICE = cur_price;
    }
    float PRICEMAX;
    int DAYS;
    public BBandTest (String stock_code, List<Float> close, int days) {

        int size = close.size();
        if (days == -1) CLOSEDATA = close;
        else {
            for (int i = 0; i < days; i++) {
                CLOSEDATA.add(close.get(size - days + i));
            }
        }
        DAYS = days;
        STOCK_CODE = stock_code;
        //StockDic stockdic = new StockDic();
        //STOCK_NAME = stockdic.getStockname(stock_code);
        loadTestData();
        rsitest = new RSITest(CLOSEDATA, days);
        testNsave(true);
        PRICEMAX = Collections.max(CLOSEDATA);
    }

    void loadTestData() {
        List<List<Float>> bband_result = mytalib.bbands(CLOSEDATA,DAYS);
        UPPERLINE = bband_result.get(0);
        MIDDLELINE = bband_result.get(1);
        LOWLINE = bband_result.get(2);
        PERCENTB = bband_result.get(3);
    }

    // 백분율 스케일링된 percentb로 스코어링을 한 결과를
    // 차트에 보여주기 위해서 다시 maxprice 스케일링을 한다
    public List<Float> getBuyPoint() {
        List<Integer> testresult = new ArrayList<>();
        List<Float> chartvalue = new ArrayList<>();
        testresult = BUYSCORE;
        int size = testresult.size();
        for(int i =0;i<size;i++) {
            float value = (float)(PRICEMAX + PRICEMAX*0.01*testresult.get(i));
            chartvalue.add(i,value);
        }
        return chartvalue;
    }

    // bband test에서 buysell signal은 percent_b를 사용한다
    // percentb를 1~100 사이로 백분율 스케일링해서 돌려준다
    public List<Float> scaled_percentb1() {
        List<Float> bband_score = new ArrayList<>();
        List<Float> bband_signal = new ArrayList<>();
        bband_signal = PERCENTB;

        for(float ftemp: bband_signal) {
            bband_score.add(ftemp*100); // 보통 0~100이지만 마이너스와 100을 넘어갈때도 있다
        }
        return bband_score;
    }

    public List<Float> scaled_percentb() {
        List<Float> bband_score = new ArrayList<>();
        List<Float> bband_signal = new ArrayList<>();
        bband_signal = PERCENTB;
        double pricemax = Collections.max(CLOSEDATA);
        pricemax = pricemax+pricemax*0.02;
        for(float ftemp: bband_signal) {
            if(ftemp <= 0.3) bband_score.add((float)(pricemax - pricemax*0.1*ftemp));// 보통 0~100이지만 마이너스와 100을 넘어갈때도 있다
            else bband_score.add((float)(pricemax));
        }
        return bband_score;
    }

    // 백분율 스케일링된 percentb로 스코어링을 하고 그 결과를 저장한다
    public List<Integer> testNsave(boolean save_flag) {

        List<Float> bband_buyscore = new ArrayList<>();
        List<Float> rsi_buyscore = new ArrayList<>();
        List<Integer> buy_score = new ArrayList<>();
        List<Integer> sell_score = new ArrayList<>();

        int days = CLOSEDATA.size();
        // bband score는 bband와 rsi의 test line을 결합해서 계산한다
        bband_buyscore = scaled_percentb();
        rsi_buyscore = rsitest.test_line();

        int size = bband_buyscore.size();
        for(int i = 0; i< size; i++) {
            buy_score.add(0);
            sell_score.add(0);
        }

        for(int i=0;i<size;i++){
            if(bband_buyscore.get(i)<=30 && bband_buyscore.get(i) > 20
                    && rsi_buyscore.get(i)<=50 && rsi_buyscore.get(i) > 40) {
                buy_score.set(i,1);
            } else if( bband_buyscore.get(i)<=20 && bband_buyscore.get(i) >10
                    && rsi_buyscore.get(i)<=40 && rsi_buyscore.get(i) > 30){
                buy_score.set(i,2);
            }else if( bband_buyscore.get(i)<=10
                    && rsi_buyscore.get(i)<=30 ) {
                buy_score.set(i, 3);
            } else {
                buy_score.set(i, 0);
            }
            sell_score.set(i,0);
        }
        BUYSCORE = buy_score;
        SELLSCORE = sell_score;

        if(save_flag==true) {
            List<String> close = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR, false);
            List<String> date = myexcel.read_ohlcv(STOCK_CODE, "DATE", ONEYEAR, false);
            myexcel.write_testdata(STOCK_CODE, date, close, buy_score, sell_score);
        }
        return buy_score;
    }


    // 리턴값이 1~100 사이이지만 마이너스 또는 100을 초과할 때도 있음
    public Float TodayScore() {
        int size = PERCENTB.size();
        return PERCENTB.get(size-1)*100;
    }

    public void makeBackTestData(int days) {
        testNsave(true);
    }

    public List<Float> trim(List<Float> source, int days) {
        List<Float> temp = new ArrayList<>();
        int start = source.size() - days;
        for(int i =0;i<days;i++) {
            temp.add(source.get(start+i));
        }
        return temp;
    }

}
