package main.java.com.gomu.gomustock.stockengin;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import main.java.com.gomu.gomustock.MyExcel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tictactec.ta.lib.MAType.Sma;

public class BBandLoopbackTest {
    TAlib mytalib = new TAlib();
    MyExcel myexcel = new MyExcel();
    String STOCK_CODE;
    List<Float>  CLOSEDATA = new ArrayList<>();
    RSITest rsitest;
    int ONEYEAR = -1;
    List<Float> UPPERLINE = new ArrayList<>();
    List<Float> MIDDLELINE = new ArrayList<>();
    List<Float> LOWLINE = new ArrayList<>();
    List<Float> PERCENTB = new ArrayList<>();

    public List<Float> chartdata() {
        List<Integer> testresult = new ArrayList<>();
        List<Float> chartvalue = new ArrayList<>();
        int days = CLOSEDATA.size();
        testresult = testNsave(false);
        int size = testresult.size();
        float pricemax = Collections.max(CLOSEDATA);
        for(int i =0;i<size;i++) {
            float value = (float)(pricemax + pricemax*0.01*testresult.get(i));
            chartvalue.add(i,value);
        }
        return chartvalue;
    }

    public List<Integer> testNsave(boolean save_flag) {

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
            sell_score.set(i,0);
        }

        if(save_flag == true) {
            List<String> close = myexcel.read_ohlcv(STOCK_CODE, "CLOSE", ONEYEAR, false);
            List<String> date = myexcel.read_ohlcv(STOCK_CODE, "DATE", ONEYEAR, false);
            myexcel.write_testdata(STOCK_CODE, date, close, buy_score, sell_score);
        }
        return buy_score;
    }

    List<Float> bband_30day_loop() {
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
}
