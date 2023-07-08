package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.format.FormatScore;
import main.java.com.gomu.gomustock.network.StockBox;


import java.util.ArrayList;
import java.util.List;

public class MyScore {

    List<String> srcdata = new ArrayList<>();
    List<Float> benchstd = new ArrayList<>();
    List<String> benchdata = new ArrayList<>();
    List<Float> srcstddata = new ArrayList<>();
    public List<FormatScore> scorebox = new ArrayList<>();
    List<String> stockcodelist = new ArrayList<String>();
    //Context context;
    FormatScore benchbox = new FormatScore();

    String STOCK_CODE;

    public MyScore(List<String> codelist, String benchcode) {

        putStockcodelist(codelist);
        makeBenchBox(benchcode);
        makeScoreBox(stockcodelist);
        loadHistory2Scorebox();
        //loadKodex2002Scorebox();
    }

    public MyScore(String stockcode) {
        STOCK_CODE = stockcode;
        FormatScore temp = new FormatScore();
        temp = makeScoreBox(stockcode);
        temp = loadHistory2Scorebox(temp, 60);
    }

    public List<FormatScore> getScorebox() {
        List<FormatScore> trim_scoreobx = scorebox;

        int size = trim_scoreobx.size();
        for (int i = 0; i < size; i++) {
            if (trim_scoreobx.get(i).stock_code.equals(benchbox.stock_code)) {
                trim_scoreobx.remove(i);
                break;
            }
        }
        return trim_scoreobx;
    }

    public void putStockcodelist(List<String> codelist) {
        stockcodelist = codelist;
    }

    public void calcScore() {
        int size = scorebox.size();
        for (int i = 0; i < size; i++) {
            //if(mixlist.get(i).stock_name.equals("코스피 200")) continue;
            String stock_code = scorebox.get(i).stock_code;
            int score = scoring_bband(stock_code);
            scorebox.get(i).score = score;
            System.out.println(scorebox.get(i).stock_code + " = " + Integer.toString(score));
        }
    }

    public String getScore(String stock_code) {
        String result = "";
        int size = scorebox.size();
        for (int i = 0; i < size; i++) {
            if (scorebox.get(i).stock_code.equals(stock_code)) {
                result = String.valueOf(scorebox.get(i).score);
                break;
            }
        }
        return result;
    }

    public int getScorelistsize() {
        return scorebox.size();
    }

    public void loadHistory2Scorebox() {
        MyExcel myexcel = new MyExcel();
        int size = scorebox.size();
        for (int i = 0; i < size; i++) {
            FormatScore onemix = new FormatScore();
            onemix = scorebox.get(i);
            srcdata = myexcel.oa_readItem(onemix.stock_code + ".xls", "CLOSE", false);
            srcdata.add(onemix.cur_price);
            scorebox.get(i).period_price = srcdata;
        }
        benchbox.period_price = myexcel.oa_readItem(benchbox.stock_code + ".xls", "CLOSE", false);
        benchbox.period_price.add(benchbox.cur_price);
        int i = 0;
    }

    public FormatScore loadHistory2Scorebox(FormatScore onemix, int days) {
        MyExcel myexcel = new MyExcel();
        FormatScore temp = onemix;
        srcdata = myexcel.readhistory(onemix.stock_code + ".xls", "CLOSE", days, false);
        temp.period_price = srcdata;
        return temp;
    }

    public int scoring_std(String stock_code) {
        List<String> itemdata = new ArrayList<>();
        int score = 0;

        MyStat mystat = new MyStat();

        // 스코어링할 종목가격을 불러온다
        int index = find_index(stock_code);
        if (index == -1) {
            return score = 0;
        }
        itemdata = scorebox.get(index).period_price;
        //itemdata.add(0,scorebox.get(index).cur_price);
        int size = itemdata.size();
        srcstddata = mystat.oa_standardization(itemdata);

        // 기준종목 가격을 불러온다.
        if (benchstd.size() == 0) {
            benchstd = mystat.oa_standardization(benchbox.period_price);
        }

        // 기준종목과 스코어링종목의 가장 마지막 날 가치를 비교하여 스코어링을 한다
        float diff = 0;
        int i = srcstddata.size() - 1;
        diff = srcstddata.get(i) - benchstd.get(i);

        // -는 sell : 기준보다 가격이 높으면 판다
        if (diff >= 0.5 && diff < 1) score = -1;
        else if (diff >= 1) score = -2;

        // +는 buy : 기준보다 가격이 낮으면 산다
        if (diff <= -0.5 && diff > -1) score = 1;
        else if (diff <= -1) score = 3;

        return score;
    }

    public int scoring_bband(String stock_code) {
        List<String> itemdata = new ArrayList<>();
        int score = 0;

        StockBox kbbank = new StockBox("055550");
        List<Float> kbband_close = kbbank.getClose();
        // 스코어링할 종목가격을 불러온다
        BBandTest bbandtest = new BBandTest("055550",kbband_close);
        RSITest rsitest = new RSITest(kbband_close);
        float bband_score = bbandtest.TodayScore(60);
        float rsi_score = rsitest.TodayScore(60);

        if(bband_score<=30 && bband_score > 20
                && rsi_score<=50 && rsi_score > 40) {
            score = 1;
        } else if( bband_score<=20 && bband_score >10
                && rsi_score<=40 && rsi_score > 30){
            score = 2;
        }else if( bband_score<=10
                && rsi_score<=30 ) {
            score = 3;
        }

        return score;
    }

    public void makeScoreBox(List<String> codelist) {
        scorebox.clear();
        int size = stockcodelist.size();
        for (int i = 0; i < size; i++) {
            FormatScore temp = new FormatScore();
            temp.stock_code = codelist.get(i);
            temp.cur_price = "0";
            temp.score = 0;
            scorebox.add(temp);
        }
    }
    public FormatScore makeScoreBox(String stockcode) {
        FormatScore temp = new FormatScore();
        temp.stock_code = stockcode;
        temp.cur_price = "0";
        temp.score = 0;
        return temp;
    }

    public void makeBenchBox(String code) {
        benchbox.stock_code = code;
        benchbox.cur_price = "0";
        benchbox.score = 0;
    }

    public int find_index(String stock_code) {
        int size = scorebox.size();
        for (int i = 0; i < size; i++) {
            if (stock_code.equals(scorebox.get(i).stock_code)) {
                return i;
            }
        }
        return -1;
    }

    // 아래 메소드는 activity에서 thread로 감싸서 사용해야 한다.
    public void addCurprice2Scorebox() {
        // TODO Auto-generated method stub
        MyWeb myweb = new MyWeb();
        int size = scorebox.size();
        try {
            for (int i = 0; i < size; i++) {
                String stock_code = scorebox.get(i).stock_code;
                String cur_price = myweb.getCurrentStockPrice(stock_code);
                scorebox.get(i).cur_price = cur_price.replaceAll(",", "");
            }
            benchbox.cur_price = myweb.getCurrentStockPrice(benchbox.stock_code);
        } catch (Exception e) {
            return;
            //throw new RuntimeException(e);
        }
    }

    public void getPriceThreadStart() {
        scoring_thread.start();
    }

    public BackgroundThread scoring_thread = new BackgroundThread();

    class BackgroundThread extends Thread {
        public void run() {
            try {
                addCurprice2Scorebox();
                Thread.sleep(1000L);
                //myscoring2();
            } catch (InterruptedException e) {
                System.out.println("인터럽트로 인한 스레드 종료.");
                return;
            }
        }
    }
}
