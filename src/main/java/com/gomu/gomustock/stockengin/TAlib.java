package main.java.com.gomu.gomustock.stockengin;

import static com.tictactec.ta.lib.MAType.Sma;

import main.java.com.gomu.gomustock.MyExcel;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TAlib {


    public List<Float> bband_signal = new ArrayList<Float>();
    Double[] CLOSEPRICE = new Double[240];
    List<String> CLOSE_STR = new ArrayList<>();
    public TAlib() {

    }
    public TAlib(String stock_code, int days) {
        CLOSEPRICE = readClosePrice(stock_code, days);
    }

    public Double[] readClosePrice(String stock_code, int days) {
        Double[] price = new Double[240];
        MyExcel myexcel = new MyExcel();
        CLOSE_STR = myexcel.readhistory(stock_code+".xls","CLOSE",days,false);
        CLOSE_STR = myexcel.arrangeRev_string(CLOSE_STR);
        List<Double> closedata = myexcel.string2double(CLOSE_STR,1);
        for (int i =  0; i < price.length; i++) {
            price[i] = (double) closedata.get(i);
        }
        return price;
    }
    public boolean checkPriceBufferEmpty(int days) {
        boolean flag;
        if(days>CLOSE_STR.size()) flag = true;
        else flag = false;
        return flag;
    }

    public List<List<Float>> macd(List<Float> input, int days ) {

        List<List<Float>> result = new ArrayList<List<Float>>();
        final int TOTAL_PERIODS = days;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outMACD = new double[TOTAL_PERIODS];
        double[] outMACDSignal = new double[TOTAL_PERIODS];
        double[] outMACDHist = new double[TOTAL_PERIODS];
        double[] outSlowD = new double[TOTAL_PERIODS];
        MInteger outBegIdx = new MInteger();;
        MInteger outNBElement = new MInteger();
        MInteger begin = new MInteger();;
        MInteger length = new MInteger();
        int optInTimePeriod = 14;
        MAType optInMAType = Sma; // 단순이동평균
        int optInFastPeriod = 12;
        int optInSlowPeriod = 26;
        int optInSignalPeriod = 9;

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) input.get(i);
        }


        Core c = new Core();
        RetCode retCode = c.macd(0, closePrice.length - 1,closePrice,
                optInFastPeriod, optInSlowPeriod, optInSignalPeriod,
                begin, length, outMACD, outMACDSignal,outMACDHist);

        if (retCode == RetCode.Success) {
            //System.out.println("Output Start Period: " + begin.value);
            //System.out.println("Output End Period: " + (begin.value + length.value - 1));
            int start = begin.value;
            int end = (begin.value + length.value);
            // 결과를 float 리스트로 패킹해서 전달한다;
            // 시작이 4부터임. 그래서 0~3까지 초기값으로 채워넣음
            List<Float> value = new ArrayList<Float>();
            for (int i = 0; i < start; i++) {
                value.add((float) outMACD[0]);
            }
            for (int i = 0; i < end - start; i++) {
                value.add((float) outMACD[i]);
            }
            result.add(value);

            List<Float> value1 = new ArrayList<Float>();
            for (int i = 0; i < start; i++) {
                value1.add((float) outMACDSignal[0]);
            }
            for (int i = 0; i < end - start; i++) {
                value1.add((float) outMACDSignal[i]);
            }
            result.add(value1);

            List<Float> value2 = new ArrayList<Float>();
            for (int i = 0; i < start; i++) {
                value2.add((float) outMACDHist[0]);
            }
            for (int i = 0; i < end - start; i++) {
                value2.add((float) outMACDHist[i]);
            }
            result.add(value2);
        }
        return result;
    }

    public List<List<Float>> rsi(List<Float> input, int days) {
        List<List<Float>> resultlist = new ArrayList<List<Float>>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = days;

        // The number of periods to average together.
        final int optInTimePeriod = 5;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outReal = new double[TOTAL_PERIODS];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) input.get(i);
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

            for (int i = 0; i < start; i++) {
                result.add((float) outReal[0]);
            }
            for(int i = 0;i < end-start;i++ ) {
                result.add((float)outReal[i]);
            }

            resultlist.add(result);

            List<Float> temp = rsi_month_average(start, end, result);
            resultlist.add(temp);
        }
        return resultlist;
    }



    public List<List<Float>> bbands(List<Float> close, int days) {

        List<List<Float>> threechart = new ArrayList<List<Float>>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = days;

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

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) close.get(i);
        }

        Core c = new Core();
        //RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);
        RetCode retCode = c.bbands(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE,
                optInNbDevUp, optInNbDevDn, optInMAType,
                begin, length, outRealUpperBand, outRealMiddleBand, outRealLowerBand);

        if (retCode == RetCode.Success) {
            //System.out.println("Output Start Period: " + begin.value);
            //System.out.println("Output End Period: " + (begin.value + length.value - 1));
            int start = begin.value;
            int end = (begin.value + length.value);
            // 결과를 float 리스트로 패킹해서 전달한다
            int length2 = outRealUpperBand.length;
            // 시작이 4부터임. 그래서 0~3까지 초기값으로 채워넣음
            List<Float> value = new ArrayList<Float>();
            for(int i = 0;i<start;i++) {
                value.add((float)outRealUpperBand[0]);
            }
            for(int i = 0;i < end-start;i++ ) {
                value.add((float)outRealUpperBand[i]);
            }
            threechart.add(value);

            List<Float> value1 = new ArrayList<Float>();
            for(int i = 0;i<start;i++) {
                value1.add((float)outRealMiddleBand[0]);
            }
            for(int i = 0; i <end-start;i++ ) {
                value1.add((float)outRealMiddleBand[i]);
            }
            threechart.add(value1);

            List<Float> value2 = new ArrayList<Float>();
            for(int i = 0;i<start;i++) {
                value2.add((float)outRealLowerBand[0]);
            }
            for(int i = 0; i <end-start;i++ ) {
                value2.add((float)outRealLowerBand[i]);
            }
            threechart.add(value2);

            List<Float> value3 = new ArrayList<Float>();

            for(int i = 0; i <end-start;i++ ) {
                // %percent_b는 밴드내에서 주가의 위치를 알려준다. 0~1 사이이지만 마이너스로 갈때도 있다
                double percent_b = (closePrice[start+i]-outRealLowerBand[i])/(outRealUpperBand[i]-outRealLowerBand[i]);
                value3.add((float)percent_b);
            }
            float first_perb = value3.get(0);
            for(int i = 0;i<start;i++) {
                value3.add(0,(float)first_perb);
            }

            threechart.add(value3);
        }
        else {
            System.out.println("Error");
        }

        return threechart;
    }


    public List<Float> adx(List<Float> input, List<Float> highdata, List<Float> lowdata, int days) {


        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = days;


        double[] closePrice = new double[TOTAL_PERIODS];
        double[] highPrice = new double[TOTAL_PERIODS];
        double[] lowPrice = new double[TOTAL_PERIODS];
        double[] outReal = new double[TOTAL_PERIODS];
        MInteger outBegIdx = new MInteger();;
        MInteger outNBElement = new MInteger();
        MInteger begin = new MInteger();;
        MInteger length = new MInteger();
        int optInTimePeriod = 14;

        List<Float> value = new ArrayList<Float>();

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) input.get(i);
        }

        for (int i =  0; i < highPrice.length; i++) {
            highPrice[i] = (double) highdata.get(i);
        }

        for (int i =  0; i < lowPrice.length; i++) {
            lowPrice[i] = (double) lowdata.get(i);
        }

        Core c = new Core();
        //RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, PERIODS_AVERAGE, begin, length, out);
        RetCode retCode = c.adx( 0, closePrice.length -1,  highPrice, lowPrice, closePrice,
                optInTimePeriod, begin, length,  outReal);

        if (retCode == RetCode.Success) {
            //System.out.println("Output Start Period: " + begin.value);
            //System.out.println("Output Period length : " +length.value);
            int start = begin.value;
            int end = (begin.value + length.value);
            // 결과를 float 리스트로 패킹해서 전달한다
            int length2 = outReal.length;
            // 시작이 4부터임. 그래서 0~3까지 초기값으로 채워넣음

            for(int i = 0;i<start;i++) {
                value.add((float)outReal[0]);
            }
            for(int i = 0;i < end-start;i++ ) {
                value.add((float)outReal[i]);
            }

        }
        else {
            System.out.println("Error");
        }

        return value;
    }

    public List<Float> rsi_month_average(int start, int end, List<Float> input) {
        List<Float> resultlist = new ArrayList<>();
        int size = input.size();
        float average=0f;
        for(int i = start;i<end - start;i++) {
            average += input.get(i);
        }
        average = average/(size-start);

        int i;
        float month1_ave=0f, month2_ave=0, month3_ave=0, month4_ave=0;
        for(i=50;i<60;i++) month1_ave += input.get(i);
        month1_ave = month1_ave/10;

        for(i=40;i<50;i++) month2_ave += input.get(i);
        month2_ave = month2_ave/10;

        for(i=30;i<40;i++) month3_ave += input.get(i);
        month3_ave = month3_ave/10;

        for(i=20;i<30;i++) month4_ave += input.get(i);
        month4_ave = month4_ave/10;

        for(i =0;i<20;i++) resultlist.add(average);
        for(i =20;i<30;i++) resultlist.add(month4_ave);
        for(i =30;i<40;i++) resultlist.add(month3_ave);
        for(i =40;i<50;i++) resultlist.add(month2_ave);
        for(i =50;i<60;i++) resultlist.add(month1_ave);

        return resultlist;
    }

    public List<List<Float>> stoch(List<Float> close, List<Float> highdata, List<Float> lowdata, int days ) {

        // The total number of periods to generate data for.
        int TOTAL_PERIODS = days;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] highPrice = new double[TOTAL_PERIODS];
        double[] lowPrice = new double[TOTAL_PERIODS];
        double[] outSlowK = new double[TOTAL_PERIODS];
        double[] outSlowD = new double[TOTAL_PERIODS];
        MInteger outBegIdx = new MInteger();;
        MInteger outNBElement = new MInteger();
        MInteger begin = new MInteger();;
        MInteger length = new MInteger();
        int optInTimePeriod = 14;
        MAType optInMAType = Sma; // 단순이동평균
        int optInFastK_Period = 5;
        int optInSlowK_Period = 3;
        int optInSlowD_Period = 3;
        MAType optInSlowK_MAType = Sma;
        MAType optInSlowD_MAType = Sma;

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) close.get(i);
        }

        for (int i =  0; i < highPrice.length; i++) {
            highPrice[i] = (double) highdata.get(i);
        }

        for (int i =  0; i < lowPrice.length; i++) {
            lowPrice[i] = (double) lowdata.get(i);
        }

        // outNBElement : 생성된 자료의 배술 수. = output 길이
        // 결과가 배열로 반환되기 때문에 길이정보도 같이 반환되어야 함
        Core c = new Core();
        RetCode retCode  = c.stoch( 0, closePrice.length -1, highPrice, lowPrice, closePrice,
                optInFastK_Period, optInSlowK_Period, optInSlowK_MAType, optInSlowD_Period, optInSlowD_MAType,
                outBegIdx, outNBElement, outSlowK, outSlowD );

        List<List<Float>> threechart = new ArrayList<List<Float>>();

        List<Float> outSlowK_list = new ArrayList<Float>();
        int start = outBegIdx.value;
        int end = (outBegIdx.value + outNBElement.value);

        for(int i = 0;i<start;i++) {
            outSlowK_list.add((float)outSlowK[0]);
        }
        for(int i = 0;i < end-start;i++ ) {
            outSlowK_list.add((float)outSlowK[i]);
        }
        threechart.add(outSlowK_list);

        List<Float> outSlowD_list = new ArrayList<Float>();
        start = outBegIdx.value;
        end = (outBegIdx.value + outNBElement.value);

        for(int i = 0;i<start;i++) {
            outSlowD_list.add((float)outSlowD[0]);
        }
        for(int i = 0;i < end-start;i++ ) {
            outSlowD_list.add((float)outSlowD[i]);
        }
        threechart.add(outSlowD_list);

        return threechart;
    }


    public List<Float> mom(List<Float> close, int days) {

        List<List<Float>> resultlist = new ArrayList<List<Float>>();

        // The total number of periods to generate data for.
        final int TOTAL_PERIODS = days;

        // The number of periods to average together.
        final int optInTimePeriod = 10;

        double[] closePrice = new double[TOTAL_PERIODS];
        double[] outReal = new double[TOTAL_PERIODS];
        MInteger outBegIdx = new MInteger();
        MInteger outNBElement = new MInteger();

        for (int i =  0; i < closePrice.length; i++) {
            closePrice[i] = (double) close.get(i);
        }

        Core c = new Core();
        RetCode retCode  = c.mom( 0, closePrice.length -1, closePrice,optInTimePeriod,
                outBegIdx, outNBElement, outReal);

        List<List<Float>> threechart = new ArrayList<List<Float>>();

        List<Float> result = new ArrayList<Float>();
        int start = outBegIdx.value;
        int end = (outBegIdx.value + outNBElement.value);

        for(int i = 0;i<start;i++) {
            result.add((float)outReal[0]);
        }
        for(int i = 0;i < end-start;i++ ) {
            result.add((float)outReal[i]);
        }
        return result;
    }

}
