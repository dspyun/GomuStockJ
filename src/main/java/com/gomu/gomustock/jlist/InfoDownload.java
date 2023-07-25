package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatETFInfo;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.network.fnGuide;
import main.java.com.gomu.gomustock.stockengin.StockDic;

import java.util.ArrayList;
import java.util.List;

public class InfoDownload {


    public void downloadStockInfoCustom(String filename) {
        MyExcel myexcel = new MyExcel();
        MyWeb myweb = new MyWeb();
        StockDic stockdic = new StockDic();
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

    public void downloadNowPrice(List<String> stock_list) {
        MyExcel myexcel = new MyExcel();
        MyWeb myweb = new MyWeb();
        int hour = 3;
        int size = stock_list.size();
        for(int i =0;i<size;i++) {
            String stock_code = stock_list.get(i);
            myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
        }
    }

    public void downloadYFPrice(List<String> stock_list) {
        int size = stock_list.size();
        for(int i =0;i<size;i++) {
            new YFDownload(stock_list.get(i));
        }
    }


}
