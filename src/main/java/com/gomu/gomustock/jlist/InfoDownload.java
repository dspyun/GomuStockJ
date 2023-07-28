package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatETFInfo;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.network.fnGuide;
import main.java.com.gomu.gomustock.stockengin.StockDic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InfoDownload {

    MyExcel myexcel = new MyExcel();
    MyWeb myweb = new MyWeb();
    StockDic stockdic = new StockDic();
    private String msg;
    void InfoDownload() {

    }

    public interface IFCallback {
        public void callback(String str);
    }
    // 콜백인터페이스를 구현한 클래스 인스턴스
    private InfoDownload.IFCallback _cb;
    public void setCallback(InfoDownload.IFCallback cb) {
        this._cb = cb;
    }


    public void downloadStockInfoCustom(String filename) {

        List<String> stock_list = new ArrayList<>();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfoCustom(filename,false);
        int size = web_stockinfo.size();
        for(int i =0;i<size;i++) {
            stock_list.add(web_stockinfo.get(i).stock_code);
        }
        web_stockinfo.clear();
        size = stock_list.size();
        for(int i =0;i<size;i++) {
            String stock_code = stock_list.get(i);
            FormatStockInfo stockinfo = new FormatStockInfo();
            FormatETFInfo etfinfo = new FormatETFInfo();
            fnGuide myfnguide = new fnGuide();
            String news;


            if(stockdic.checkKRStock(stock_code) && (stockdic.getMarket(stock_code)!="")) {
                // stock_cdoe정보를 포함하고 있는
                // 네이버 정보를 가장 먼저 가져오고 그 다음에 다른 정보를 추가해야 한다
                _cb.callback("info" + "\n"+"download" + "\n" + stock_code);

                stockinfo = myweb.getNaverStockinfo(stock_code);
                stockinfo.stock_code = stock_code;
                stockinfo.stock_type="KSTOCK";
                // 네이버 뉴스를 가져온다
                news = myweb.getNaverStockNews(stock_code);
                stockinfo.news = news;

                // fnguide정보를 가져온다
                stockinfo.fninfo = myfnguide.getFnguideInfo(stock_code);

                web_stockinfo.add(stockinfo);
            } else {
                _cb.callback("info" + "\n"+"download" + "\n" + stock_code);

                etfinfo = myweb.getNaverETFinfo(stock_code);
                stockinfo.stock_type="KETF";
                stockinfo.etfinfo = etfinfo.toString();
                stockinfo.stock_code = etfinfo.stock_code;
                stockinfo.stock_name = etfinfo.stock_name;
                stockinfo.desc = etfinfo.desc;

                news = myweb.getNaverStockNews(stock_code);
                stockinfo.news = news;
                // fnguide정보를 가져온다
                stockinfo.fninfo = myfnguide.getFnguideETFInfo(stock_code);
                web_stockinfo.add(stockinfo);
            }
        }
        myexcel.writestockinfoCustom(filename,web_stockinfo);
    }

    public FormatStockInfo downloadStockInfoOne(String stock_code) {

            FormatStockInfo stockinfo = new FormatStockInfo();
            FormatETFInfo etfinfo = new FormatETFInfo();
            fnGuide myfnguide = new fnGuide();
            String news;

            if(stockdic.checkKRStock(stock_code) && (stockdic.getMarket(stock_code)!="")) {
                // stock_cdoe정보를 포함하고 있는
                // 네이버 정보를 가장 먼저 가져오고 그 다음에 다른 정보를 추가해야 한다

                stockinfo = myweb.getNaverStockinfo(stock_code);
                stockinfo.stock_code = stock_code;
                stockinfo.stock_type="KSTOCK";
                // 네이버 뉴스를 가져온다
                news = myweb.getNaverStockNews(stock_code);
                stockinfo.news = news;

                // fnguide정보를 가져온다
                stockinfo.fninfo = myfnguide.getFnguideInfo(stock_code);

            } else {

                etfinfo = myweb.getNaverETFinfo(stock_code);
                stockinfo.stock_type = "KETF";
                stockinfo.etfinfo = etfinfo.toString();
                stockinfo.stock_code = etfinfo.stock_code;
                stockinfo.stock_name = etfinfo.stock_name;
                stockinfo.desc = etfinfo.desc;

                news = myweb.getNaverStockNews(stock_code);
                stockinfo.news = news;
                // fnguide정보를 가져온다
                stockinfo.fninfo = myfnguide.getFnguideETFInfo(stock_code);

            }
        return stockinfo;

    }

    public void downloadNowPrice(List<String> stock_list, int hour) {
        int size = stock_list.size();
        for(int i =0;i<size;i++) {
            String stock_code = stock_list.get(i);
            _cb.callback("오늘가격" + "\n"+"다운로드" + "\n" + stock_code);
            myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
        }
    }

    public void downloadYFPrice(List<String> stock_list) {
        int size = stock_list.size();
        for(int i =0;i<size;i++) {
            String stock_code = stock_list.get(i);
            _cb.callback("일년가격" + "\n"+"다운로드" + "\n" + stock_code);
            new YFDownload(stock_code);
        }
    }


    public void pickupNewstock() {
        String filename = "info_kospi";


        _cb.callback("종목뽑기");

        int size = 10; // 1페이지 50개, 10페이지면 500개
        List<List<String>> upjonglist = new ArrayList<List<String>>();
        upjonglist = myweb.getNaverGoodEarningStock("1", true);
        for(int i=2;i<size;i++) {
            List<List<String>> temp = new ArrayList<List<String>>();
            temp = myweb.getNaverGoodEarningStock(Integer.toString(i), false);
            int size2 = temp.size();
            for(int j =0;j<size2;j++) {
                upjonglist.add(temp.get(j));
            }
            _cb.callback("info_kospi" +"\n"+ Integer.toString(i*50));
        }

        myexcel.writenaverupjong2(filename,upjonglist );
        List<String> last_pickup = pickup40over(filename);
        myexcel.writeColumn(filename, last_pickup,1);
        trans(filename);
        _cb.callback("info_kospi");
    }

    public void downloadTotalInformation(String filename, List<String> stock_list) {
        // 통계재무정보 다운로드
        downloadStockInfoCustom(filename);
        // 하루 가격 다운로드
        downloadNowPrice(stock_list, 3);
        // 년간 가격 다운로드
        downloadYFPrice(stock_list);
    }


    public static List<String> pickup40over(String filename) {

        int profit_level = 40;

        MyExcel myexcel = new MyExcel();
        List<String> name = myexcel.readColumn(filename+".xls",1);
        List<String> profit_ratio = myexcel.readColumn(filename +".xls",11);
        List<String> revenue_ratio = myexcel.readColumn(filename+".xls",10);
        List<Float> profitratio_f = new ArrayList<>();
        List<Float> revenueratio_f = new ArrayList<>();

        List<String> temp = new ArrayList<>();
        int size = profit_ratio.size();
        for(int i=1;i<size;i++) {
            String one = profit_ratio.get(i).replace(",", "");
            if(one.equals("") || one.equals("N/A")) one = "0";
            //System.out.println(i + " " + one);
            profitratio_f.add(Float.valueOf(one));
        }
        List<String> profit_sort = new ArrayList<>();
        size = profitratio_f.size();
        for(int i =0;i<size;i++) {
            if(profitratio_f.get(i) >= profit_level)  {
                profit_sort.add(name.get(i+1));
            }
        }


        size = revenue_ratio.size();
        for(int i=1;i<size;i++) {
            String one = revenue_ratio.get(i).replace(",", "");
            if(one.equals("") || one.equals("N/A")) one = "0";
            revenueratio_f.add(Float.valueOf(one));
        }
        List<String> revenue_sort = new ArrayList<>();
        size = revenueratio_f.size();
        for(int i =0;i<size;i++) {
            if(revenueratio_f.get(i) >= profit_level)  {
                revenue_sort.add(name.get(i+1));
            }
        }

        List<String> last_pickup = new ArrayList<>();
        size = profit_sort.size();
        for(int i =0;i<size;i++) {
            int size1 = revenue_sort.size();
            for(int j =0;j<size1;j++) {
                if(profit_sort.get(i).equals(revenue_sort.get(j))) {
                    last_pickup.add(revenue_sort.get(j));
                    System.out.println(revenue_sort.get(j));
                }
            }
        }
        return last_pickup;
    }

    public static void trans(String filename) {
        MyExcel myexcel = new MyExcel();
        List<String> name = myexcel.readColumn(filename+".xls",1);
        StockDic mydict = new StockDic();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

        int size = name.size();
        for(int i =1;i<size;i++) {
            FormatStockInfo oneinfo = new FormatStockInfo();
            String stock_code = mydict.getStockcode(name.get(i));
            System.out.println("trans " + name.get(i) + " " + stock_code);
            if(stock_code.equals("") || mydict.getMarket(stock_code).equals("") ||
                    !mydict.getStocktype(stock_code).equals("주권") ||
                    mydict.getStockbelong(stock_code).contains("SPAC")) continue;
            oneinfo.stock_code = stock_code;
            web_stockinfo.add(oneinfo);
        }
        myexcel.writestockinfoCustom(filename,web_stockinfo);
    }

    public static void checkspac(String filename) {
        MyExcel myexcel = new MyExcel();
        List<String> stocklist = myexcel.readColumn(filename+".xls",0);
        StockDic mydict = new StockDic();
        int size = stocklist.size();
        for(int i =1;i<size;i++) {
            String stock_code = stocklist.get(i);
            System.out.println("trans " + stock_code + " " + mydict.getStockbelong(stock_code) + " " + mydict.getStocktype(stock_code));
            if(stock_code.equals("") || mydict.getMarket(stock_code).equals("") ||
                    !mydict.getStocktype(stock_code).equals("주권") ||
                    mydict.getStockbelong(stock_code).contains("SPAC")) {
                System.out.println("skip " + stock_code);
                continue;
            }
        }
        System.out.println("check finish ");
    }
}
