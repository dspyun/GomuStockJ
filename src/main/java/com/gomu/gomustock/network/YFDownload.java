package main.java.com.gomu.gomustock.network;

import main.java.com.gomu.gomustock.MyDate;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YFDownload {
    String STOCK_CODE;
    String STOCK_CODE_ENC;
    int ONEYEAR = 240;

    StockDic stockdic = new StockDic();
    public YFDownload (String stock_code)  {

        STOCK_CODE = stock_code;
        String market = stockdic.getMarket(stock_code);
        STOCK_CODE_ENC = encodingURL(stock_code, market);
        download();
    }

    public void download() {

        String stock_code = STOCK_CODE_ENC;
        MyDate mydate = new MyDate();
        MyExcel myexcel = new MyExcel();
        String csvdata="";
        try {
            long mytoday = mydate.yf_epoch_today();
            long oneyearago = mydate.yf_epoch_1yearago();
            String today = Long.toString(mytoday);
            String oneyearbefore = Long.toString(oneyearago);


            String path = "https://query1.finance.yahoo.com/v7/finance/download/" + STOCK_CODE_ENC +
                    "?period1=" + oneyearbefore + "&period2=" + today + "&interval=1d&events=history&includeAdjustedClose=true";

            if(stock_code.equals("247540")) {
                int i = 0;
                int j = 0;
                //https://query1.finance.yahoo.com/v7/finance/download/247540.KQ?period1=1657586267&period2=1689122267&interval=1d&events=history&includeAdjustedClose=true
                //https://query1.finance.yahoo.com/v7/finance/download/000660.KS?period1=1657589689&period2=1689125689&interval=1d&events=history&includeAdjustedClose=true
            }
            //https://query1.finance.yahoo.com/v7/finance/download/%5EKS11?period1=1657529392&period2=1689065392&interval=1d&events=history&includeAdjustedClose=true
            //https://query1.finance.yahoo.com/v7/finance/download/%5EKS11?period1=1657529546&period2=1689065546&interval=1d&events=history&includeAdjustedClose=true

            Connection connection = Jsoup.connect(path);
            connection.timeout(1500);
            Connection.Response resultImageResponse = connection.ignoreContentType(true).execute();
            csvdata = resultImageResponse.parse().body().text();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<FormatOHLCV> ohlcvlist = new ArrayList<>();
        ohlcvlist = formatNheader(csvdata);
        myexcel.write_ohlcv(STOCK_CODE,ohlcvlist);
    }

    List<FormatOHLCV> formatNheader(String csvdata) {

        List<FormatOHLCV> ohlcvlist = new ArrayList<>();
        String[] csvdata_array = csvdata.split(" ");
        String line;
        String temp;
        int size = csvdata_array.length;
        for(int i =2;i< size;i++) {
            line = csvdata_array[i];
            FormatOHLCV oneline = new FormatOHLCV();
            String[] splitstring = line.split(",");
            temp = splitstring[0];
            oneline.date = temp. replaceAll("-","");
            oneline.open = splitstring[1];
            oneline.high = splitstring[2];
            oneline.low = splitstring[3];
            oneline.close = splitstring[4];
            oneline.adjclose = splitstring[5];
            oneline.volume = splitstring[6];
            ohlcvlist.add(oneline);
        }
        FormatOHLCV header = new FormatOHLCV();
        header.setheader();
        ohlcvlist.add(0,header);
        return ohlcvlist;
    }

    public String encodingURL(String stock_code, String market) {
        String[] index_table = {"^KS11", "^GSPC", "^IXIC", "^DJI", "^SOX", "^KS200"};
        String[] korea_index_table = {"069500", "429000", "102110", "305540", "210780", "133690"};
        boolean isExist = Arrays.stream(index_table).anyMatch(stock_code::equals);
        boolean ksisExist = Arrays.stream(korea_index_table).anyMatch(stock_code::equals);
        if (checkKRStock(stock_code)) {
            // 지수테이블에 포함되어 있는 경우
            // 코스피이면 KS를 붙이고, 코스닥이면 KQ를 붙인다
            if (market.equals("KOSPI")) return stock_code + ".KS";
            else if (market.equals("KONEX") || market.equals("KOSDAQ GLOBAL") || market.equals("KOSDAQ")) {
                return stock_code + ".KQ"; // KONEX, KOSDAQ, KOSDAQ GLOBAL은 모두 KQ를 달아준다
            } else {
                // 여기에 걸리는 것들은 ETF 상품들이다
                // ETF상품은 table에서 KOSPI, KOSDAQ등으로 검샘되지 않는다
                // 야후에서는 .KS를 붙이면 데이터 다운로드 가능하다
                return stock_code + ".KS";
            }
        } else {
            // 지수테이블에 포함되어 있는 경우
            return URLEncoder.encode(stock_code);
        }
    }

    public boolean checkKRStock(String stock_code) {
        // 숫자 스트링이면 true, 문자가 있으면 false를 반환한다.
        // 즉 한국주식이면 true, 외국주식이면 false 반환
        boolean isNumeric =  stock_code.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }
}
