package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompareTest {
    // 1년 종목 데이터를 불러온다
    // 1년 코스피 200을 불러온다
    // 표준화를 시킨다
    // 차이를 계산한다
    // min 차이 찾는다
    // min 차이의 날짜와 주가를 base price로 저장한다
    // base price와 일일 주가의 백분율을 산출한다
    // 매수 매도 table을 아래와 같이 작성한다.
    // 백분율이 0~-5%이면 1주매수, -5%~-10%차이면 2주 매수, 10%이상 차이나면 3주 매수
    // 0~5% 차이면 1주매도 5~10% 차이면 2주 매도, 10% 이상이면 3주 매도
    // 매수매도 테이블을 db에 입력하고 simulation 한다

    String srcfile;
    String basefile;
    List<String> srcdata = new ArrayList<>();
    List<String> basedata = new ArrayList<>();
    List<Float> basestddata = new ArrayList<>();
    List<Float>srcstddata = new ArrayList<>();

    MyExcel myexcel;
    String stock_code, base_code;

    boolean singlemode;
    public CompareTest (String stockcode, String basecode) {
        singlemode = false;
        myexcel = new MyExcel();
        this.srcfile = stockcode+".xls";
        this.basefile = basecode+".xls";
        this.stock_code = stockcode;
        this.base_code = basecode;
    }

    public CompareTest (String stockcode) {
        singlemode=true;
        myexcel = new MyExcel();
        this.srcfile = stockcode+".xls";
        this.stock_code = stockcode;
    }

    public void readData() {
        srcdata = myexcel.oa_readItem(srcfile, "CLOSE", false);
        if(singlemode==false)basedata = myexcel.oa_readItem(basefile, "CLOSE", false);
    }

    public void tranformData() {
        MyStat mystat = new MyStat();
        srcstddata = mystat.oa_standardization(srcdata);
        if(singlemode==false)basestddata = mystat.oa_standardization(basedata);
    }

    public List<Float> calDiffernce(List<Float> src1, List<Float> src2) {
        List<Float> result = new ArrayList<>();
        for(int i =0;i<src1.size();i++) {
            float value=0;
            value = src1.get(i)-src2.get(i);
            result.add(value);
        }
        return result;
    }

    public int find_baseindex(List<Float> input) {
        List<Float> temp = new ArrayList<>();

        for(int i =0;i<input.size();i++) {
            temp.add(Math.abs(input.get(i)));
        }
        Collections.sort(temp, Collections.reverseOrder());
        float basestd = temp.get(0);
        int index = input.indexOf(basestd);
        if(index == -1) index = input.indexOf(-basestd);
        return index;
    }

    public void genQuantity(List<Float> src1, List<Float> src2) {
        List<Float> difflist = new ArrayList<>();
        List<Integer> buy_quan = new ArrayList<>();
        List<Integer> sell_quan = new ArrayList<>();
        float temp = 0;

        difflist = calDiffernce(src1, src2);
        for(int i = 0; i< difflist.size(); i++) {
            buy_quan.add(0);
            sell_quan.add(0);
        }

        for(int i = 0; i< difflist.size(); i++) {
            temp = difflist.get(i);
            if(temp >= 0.5 && temp < 1) sell_quan.set(i,1);
            else if(temp >= 1) sell_quan.set(i,2);

            if(temp <= -0.5 && temp > -1) buy_quan.set(i,1);
            else if(temp <= -1) buy_quan.set(i,3);
        }
        List<String> close = myexcel.oa_readItem(srcfile, "CLoSE", false);
        List<String> date = myexcel.oa_readItem(srcfile, "DATE", false);
        myexcel.write_testdata(stock_code,date,close, buy_quan, sell_quan);
    }

    public void makeBackdataDouble() {
        readData();
        tranformData();
        genQuantity(srcstddata, basestddata);
    }

    public void makeBackdataSingle() {
        readData();
        tranformData();
        genQuantity(srcstddata, basestddata);
    }
}
