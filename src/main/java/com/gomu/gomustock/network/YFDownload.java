package main.java.com.gomu.gomustock.network;

import main.java.com.gomu.gomustock.MyDate;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
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

    public YFDownload (String stock_code)  {
        STOCK_CODE = stock_code;
        STOCK_CODE_ENC = encodingURL(stock_code);
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

            String path = "https://query1.finance.yahoo.com/v7/finance/download/"+STOCK_CODE_ENC+
                    "?period1="+oneyearbefore+"&period2="+today+"&interval=1d&events=history&includeAdjustedClose=true";


           // https://query1.finance.yahoo.com/v7/finance/download/^KS11.KS?period1=1657256254&period2=1688792254&interval=1d&events=history&includeAdjustedClose=true
            //https://query1.finance.yahoo.com/v7/finance/download/%5EKS11?period1=1657256346&period2=1688792346&interval=1d&events=history&includeAdjustedClose=true
            //https://query1.finance.yahoo.com/v7/finance/download/055550.KS?period1=1657257539&period2=1688793539&interval=1d&events=history&includeAdjustedClose=true
            //https://query1.finance.yahoo.com/v7/finance/download/055550.KS?period1=1657258574&period2=1688794574&interval=1d&events=history&includeAdjustedClose=true

            //https://query1.finance.yahoo.com/v7/finance/download/%5EKS11.KS?period1=1657256867&period2=1688792867&interval=1d&events=history&includeAdjustedClose=true
            //https://query1.finance.yahoo.com/v7/finance/download/%5EKS11?period1=1657257207&period2=1688793207&interval=1d&events=history&includeAdjustedClose=true
            //String path2 =  "https://query1.finance.yahoo.com/v7/finance/download/%5EIXIC?period1=1657209181&period2=1688745181&interval=1d&events=history&includeAdjustedClose=true";
            Connection connection = Jsoup.connect(path);
            //connection.timeout(5000);
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

    public String encodingURL(String stock_code) {
        String[] index_table = {"^KS11", "^GSPC", "^IXIC","^DJI" };
        boolean isExist = Arrays.stream(index_table).anyMatch(stock_code::equals);
        if(isExist) return URLEncoder.encode(stock_code);
        else return stock_code+".KS";
    }
}
