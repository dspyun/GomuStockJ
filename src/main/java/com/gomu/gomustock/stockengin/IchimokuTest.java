package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;
import main.java.com.gomu.gomustock.network.PriceBox;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IchimokuTest {

    TAlib mytalib = new TAlib();
    MyExcel myexcel = new MyExcel();
    String STOCK_CODE, STOCK_NAME;
    List<Float>  CLOSEDATA = new ArrayList<>();
    RSITest rsitest;
    int ONEYEAR = -1;
    List<Float> PROSPAN1LINE = new ArrayList<>();
    List<Float> PROSPAN2LINE = new ArrayList<>();
    List<Float> BEHINDESPANLINE = new ArrayList<>();
    List<Float> MAINLINE = new ArrayList<>();
    List<Float> TRANSLINE = new ArrayList<>();
    List<Float> X = new ArrayList<>();
    int PERIOD;

    public IchimokuTest (String stock_code, List<Float> close, int days) {
        PERIOD = days;
        int size = close.size();
        if (days == -1) CLOSEDATA = close;
        else {
            for (int i = 0; i < days; i++) {
                CLOSEDATA.add(close.get(size - days + i));
            }
        }
        ichimoku_test();
    }

    public List<Float> getX() {
        int size = BEHINDESPANLINE.size(); // 300, PERIOD가 200이다
        for(float i = 0;i<PERIOD;i++) X.add(i);
        return X;
    }

    public List<Float> getProspan1() {

        return cutPeriod(PROSPAN1LINE);
    }
    public List<Float> getProspan2() {
        return cutPeriod(PROSPAN2LINE);
    }
    public List<Float> getMainline() {
        return cutPeriod(MAINLINE);
    }
    public List<Float> getTransline() {
        return cutPeriod(TRANSLINE);
    }
    public List<Float> getBehindline() {
        return cutPeriod(BEHINDESPANLINE);
    }

    void ichimoku_test() {


        List<Float> input = new ArrayList<>();
        float fillvalue;

        int size = CLOSEDATA.size();
        for(int i =0;i<size;i++) {
            input.add(CLOSEDATA.get(i));
        }

        for(int i =0;i<52;i++) {
            input.add(0,input.get(0));
        }
        int start=52;

        //1. 전환선 : 9일간의 (최고+최저)/2
        for(int i =start-9;i<size+start-9;i++) {
            List<Float> nines = get9daylist(input, i);
            TRANSLINE.add((Collections.min(nines)+Collections.max(nines))/2);
        }
        for(int i=0;i<26;i++) TRANSLINE.add(TRANSLINE.get(size-1)); // 길이조정

        //2. 기준선 : 과거26일 (최고가 + 최저가)/2
        for(int i = start -26;i<size+start-26;i++) {
            List<Float> nines = get26daylist(input, i);
            MAINLINE.add((Collections.min(nines)+Collections.max(nines))/2);
        }
        for(int i=0;i<26;i++) MAINLINE.add(MAINLINE.get(size-1)); // 길이조정


        //3. 선행스팬1 : (당일전환선수치+당일기준선수치)/2 을 26일 앞으로 이동시킨 것
        for(int i = 0;i<size;i++) {
            PROSPAN1LINE.add((TRANSLINE.get(i)+MAINLINE.get(i))/2);
        }
        fillvalue = PROSPAN1LINE.get(0);
        for(int i=0;i<26;i++) PROSPAN1LINE.add(0,fillvalue); // 26일을 채움
        //for(int i=0;i<52;i++) prospan1line.add(prospan1line.get(size-1)); // 길이조정

        //4. 선행스팬2 : (52일 최고가+최저가)/2를 26일 앞으로 이동시킨 것
        for(int i = start -52;i<size+start-52;i++) {
            List<Float> nines = get52daylist(input, i);
            PROSPAN2LINE.add((Collections.min(nines)+Collections.max(nines))/2);
        }
        fillvalue = PROSPAN2LINE.get(0);
        for(int i=0;i<26;i++) PROSPAN2LINE.add(0,fillvalue); // 길이조정

        //5. 후행스팬 : 종가를 26일 뒤로 보낸 라인
        BEHINDESPANLINE = CLOSEDATA;
        fillvalue = BEHINDESPANLINE.get(BEHINDESPANLINE.size()-1);
        for(int i = 0;i<26;i++) BEHINDESPANLINE.add(fillvalue); // 240개를 26일 뒤로 보낸다
        //for(int i=0;i<26;i++) BEHINDESPANLINE.remove(i); // 26개 추가된 만큼 잘라준다.
        //for(int i = 0;i<26;i++) BEHINDESPANLINE.add(fillvalue); // 선행 span에서 26개 추가되었으니  26개 추가해주고


    }

    List<Float> get9daylist(List<Float> input, int start) {
        List<Float> day9_str = new ArrayList<>();
        for(int i = start;i<start+9;i++) {
            day9_str.add(input.get(i));
        }
        return day9_str;
    }

    List<Float> get26daylist(List<Float> input, int start) {
        List<Float> day26_str = new ArrayList<>();
        for(int i = start;i<start+26;i++) {
            day26_str.add(input.get(i));
        }
        return day26_str;
    }

    List<Float> get52daylist(List<Float> input, int start) {
        List<Float> day52_str = new ArrayList<>();
        for(int i = start;i<start+52;i++) {
            day52_str.add(input.get(i));
        }
        return day52_str;
    }

    List<Float> cutPeriod(List<Float> input) {

        List<Float> result = new ArrayList<>();
        int size = input.size(); // 300
        int start = size - PERIOD;
        for(int i =start;i<size;i++) {
            result.add(input.get(i));
        }
        //return input;
        return result;
    }

}
