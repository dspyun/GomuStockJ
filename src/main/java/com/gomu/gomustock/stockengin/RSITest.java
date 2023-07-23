package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.util.ArrayList;
import java.util.List;

public class RSITest {
    TAlib mytalib = new TAlib();
    int ONEYEAR = -1;
    String STOCK_CODE;
    List<Float>  CLOSEDATA = new ArrayList<>();
    int DAYS;
    public RSITest(List<Float> close, int days) {
        DAYS = days;
        int size = close.size();
        if(days == -1) CLOSEDATA = close;
        else {
            for (int i = 0; i < days; i++) {
                CLOSEDATA.add(close.get(size - days + i));
            }
        }
    }

    public RSITest(String stock_code,List<Float> close, int days) {
        DAYS = days;
        int size = close.size();
        if(days == -1) CLOSEDATA = close;
        else {
            for (int i = 0; i < days; i++) {
                CLOSEDATA.add(close.get(size - days + i));
            }
        }

    }

    public List<Float> test_line() {
        int days = CLOSEDATA.size();
        List<List<Float>> signal = mytalib.rsi(CLOSEDATA, days);
        return signal.get(0);
    }

    public List<Float> rsi_30day_loop() {
        List<Float> todaylist = new ArrayList<>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = 30;

        // The number of periods to average together.
        final int optInTimePeriod = 5;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outReal = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        int days = CLOSEDATA.size();
        int loop_days = days - TOTAL_PERIODS;
        Core c = new Core();

        for(int l = 0;l<loop_days;l++) {
            // CLOSEDATA는 과거>현재순으로 정렬된 상태
            int k = 0;
            for (int i = l; i < TOTAL_PERIODS+l; i++) {
                closePrice[k] = (double) CLOSEDATA.get(i);
                k++;
            }

            RetCode retCode = c.rsi(0, closePrice.length - 1, closePrice, optInTimePeriod, begin, length, outReal);

            if (retCode == RetCode.Success) {
                //System.out.println("Output Start Period: " + begin.value);
                //System.out.println("Output End Period: " + (begin.value + length.value - 1));
                int start = begin.value;
                int end = (begin.value + length.value);
                // 결과를 float 리스트로 패킹해서 전달한다

                int today = end - start - 1; // 가장 마지막 값을 today로 정하고 today값을 누적시킴
                float today_perb = (float)outReal[today] ;
                todaylist.add(today_perb);
            }
        }

        float first_perb = todaylist.get(0);
        for(int i =0;i<TOTAL_PERIODS;i++) {
            todaylist.add(0,first_perb);
        }
        return todaylist;
    }

    // 리턴값이 1~100 사이임
    public Float TodayScore() {
        List<Float> todaylist = new ArrayList<>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = CLOSEDATA.size();

        // The number of periods to average together.
        final int optInTimePeriod = 5;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outReal = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        float today_perb=0;
        int days = CLOSEDATA.size();
        // CLOSEDATA는 과거>현재순으로 정렬된 상태
        for (int i = 0; i < days; i++) {
            closePrice[i] = (double) CLOSEDATA.get(i);
        }

        Core c = new Core();
        RetCode retCode = c.rsi(0, closePrice.length - 1, closePrice, optInTimePeriod, begin, length, outReal);

        List<Float> result = new ArrayList<Float>();
        if (retCode == RetCode.Success) {
            //System.out.println("Output Start Period: " + begin.value);
            //System.out.println("Output End Period: " + (begin.value + length.value - 1));
            int start = begin.value;
            int end = (begin.value + length.value);
            // 결과를 float 리스트로 패킹해서 전달한다
            int length2 = outReal.length;
            // 시작이 4부터임. 그래서 0~3까지 초기값으로 채워넣음

            int today = end - start - 1;
            for (int i = 0; i < start; i++) {
                result.add((float) outReal[0]);
            }
            for(int i = 0;i < end-start;i++ ) {
                result.add((float)outReal[i]);
            }
            today_perb = (float)outReal[today] ;
        }
        return today_perb;
    }

}
