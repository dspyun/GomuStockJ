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
    String STOCK_CODE;
    List<Float>  CLOSEDATA = new ArrayList<>();
    RSITest rsitest;
    int ONEYEAR = -1;

    public BBandTest (String stock_code, List<Float> input) {
        rsitest = new RSITest(input);
        CLOSEDATA = input;
        STOCK_CODE = stock_code;
    }

    public List<Float> normaltest_forchart(int days) {
        List<Integer> testresult = new ArrayList<>();
        List<Float> chartvalue = new ArrayList<>();
        testresult = normaltest();
        int size = testresult.size();
        float pricemax = Collections.max(CLOSEDATA);
        for(int i =0;i<size;i++) {
            float value = (float)(pricemax + pricemax*0.01*testresult.get(i));
            chartvalue.add(i,value);
        }
        return chartvalue;
    }

    public List<Integer> normaltest() {

        List<Float> bband_buyscore = new ArrayList<>();
        List<Float> rsi_buyscore = new ArrayList<>();
        List<Integer> buy_score = new ArrayList<>();
        List<Integer> sell_score = new ArrayList<>();

        int days = CLOSEDATA.size();
        // bband score는 bband와 rsi의 buysell signal을 결합해서 계산한다
        bband_buyscore = buysell_line();
        rsi_buyscore = rsitest.buysell_line();

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
        }

        List<String> close = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR,false);
        close = myexcel.arrangeRev_string(close);
        List<String> date = myexcel.read_ohlcv(STOCK_CODE, "DATE", ONEYEAR,false);
        date = myexcel.arrangeRev_string(date);
        myexcel.write_testdata(STOCK_CODE,date,close, buy_score, sell_score);


        return buy_score;
    }

    public List<Float> loopbacktest_forchart() {
        List<Integer> testresult = new ArrayList<>();
        List<Float> chartvalue = new ArrayList<>();
        int days = CLOSEDATA.size();
        testresult = loopbacktest();
        int size = testresult.size();
        float pricemax = Collections.max(CLOSEDATA);
        for(int i =0;i<size;i++) {
            float value = (float)(pricemax + pricemax*0.01*testresult.get(i));
            chartvalue.add(i,value);
        }
        return chartvalue;
    }

    public List<Integer> loopbacktest() {

        List<Float> bband_buyscore = new ArrayList<>();
        List<Float> rsi_buyscore = new ArrayList<>();
        List<Integer> buy_score = new ArrayList<>();
        List<Integer> sell_score = new ArrayList<>();
        int days = CLOSEDATA.size();
        bband_buyscore = bband_30day_loop();
        rsi_buyscore = rsitest.rsi_30day_loop();
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
        }

        List<String> close = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR,false);
        close = myexcel.arrangeRev_string(close);
        List<String> date = myexcel.read_ohlcv(STOCK_CODE, "DATE", ONEYEAR,false);
        date = myexcel.arrangeRev_string(date);
        myexcel.write_testdata(STOCK_CODE,date,close, buy_score, sell_score);
        return buy_score;
    }

    public List<Float> bband_30day_loop() {
        List<Float> todaylist = new ArrayList<>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = 30;

        // The number of periods to average together.
        final int PERIODS_AVERAGE = 5;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outRealUpperBand = new double[TOTAL_PERIODS];
        double[] outRealMiddleBand = new double[TOTAL_PERIODS];
        double[] outRealLowerBand = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double optInNbDevUp = 2; // 상한선 = 표준편차*2
        double optInNbDevDn = 2; // 하한선 = 표준편차*2
        MAType optInMAType = Sma; // 단순이동평균

        int days = CLOSEDATA.size();
        int loop_days = days - TOTAL_PERIODS;
        Core c = new Core();
        for(int l = 0;l<loop_days;l++) {
            // CLOSEDATA는 과거>현재순으로 정렬된 상태
            int k =0;
            for (int i = l; i < TOTAL_PERIODS+l; i++) {
                closePrice[k] = (double) CLOSEDATA.get(i);
                k++;
            }

            RetCode retCode = c.bbands_oneday(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE,
                    optInNbDevUp, optInNbDevDn, optInMAType,
                    begin, length, outRealUpperBand, outRealMiddleBand, outRealLowerBand);

            int start = begin.value;
            int end = (begin.value + length.value);
            int today = end - start - 1; // 가장 마지막 데이터를 오늘 데이터로 누적시킨다
            float today_perb = (float) ((closePrice[start + today] - outRealLowerBand[today]) / (outRealUpperBand[today] - outRealLowerBand[today]));
            todaylist.add(today_perb*100);
        }
        float first_perb = todaylist.get(0);
        for(int i =0;i<TOTAL_PERIODS;i++) {
            todaylist.add(0,first_perb);
        }
        return todaylist;
    }

    // 리턴값이 1~100 사이이지만 마이너스 또는 100을 초과할 때도 있음
    public Float TodayScore() {
        List<Float> todaylist = new ArrayList<>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = CLOSEDATA.size();

        // The number of periods to average together.
        final int PERIODS_AVERAGE = 5;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outRealUpperBand = new double[TOTAL_PERIODS];
        double[] outRealMiddleBand = new double[TOTAL_PERIODS];
        double[] outRealLowerBand = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        double optInNbDevUp = 2; // 상한선 = 표준편차*2
        double optInNbDevDn = 2; // 하한선 = 표준편차*2
        MAType optInMAType = Sma; // 단순이동평균

        Core c = new Core();
        int days = CLOSEDATA.size();
        // CLOSEDATA는 과거>현재순으로 정렬된 상태
        for (int i = 0; i < days; i++) {
            closePrice[i] = (double) CLOSEDATA.get(i);
        }

        RetCode retCode = c.bbands_oneday(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE,
                optInNbDevUp, optInNbDevDn, optInMAType,
                begin, length, outRealUpperBand, outRealMiddleBand, outRealLowerBand);

        int start = begin.value;
        int end = (begin.value + length.value);
        int today = end - start - 1;

        float today_perb = (float) ((closePrice[start + today] - outRealLowerBand[today]) / (outRealUpperBand[today] - outRealLowerBand[today]));

        return today_perb*100;
    }


    public List<Float> buysell_line() {
        // bband test에서 buysell signal은 percent_b를 사용한다
        // percentb를 1~100 사이로 스케일링해서 돌려준다.
        List<Float> bband_score = new ArrayList<>();
        List<Float> bband_signal = new ArrayList<>();
        int days = CLOSEDATA.size();
        List<List<Float>> bband_result = mytalib.bbands(CLOSEDATA,days);
        bband_signal = bband_result.get(3);

        for(float ftemp: bband_signal) {
            bband_score.add(ftemp*100); // 보통 0~100이지만 마이너스와 100을 넘어갈때도 있다
        }
        return bband_score;
    }

    public void makeBackTestData(int days) {
        normaltest();
        loopbacktest();
    }

    public List<Double> getTestData() {
        List<Double> result = new ArrayList<>();
        List<Float> price = new ArrayList<>();
        List<FormatTestData> testdata = myexcel.readtestbuy(STOCK_CODE,false);
        int size = testdata.size();
        for(int i =0;i<size;i++) {
            price.add( Float.parseFloat(testdata.get(i).price));
        }
        float maxprice = Collections.max(price);
        for(int i =0;i<size;i++) {
            result.add(maxprice + maxprice*0.01*Float.parseFloat(testdata.get(i).buy_quantity));
        }
        return result;
    }
}
