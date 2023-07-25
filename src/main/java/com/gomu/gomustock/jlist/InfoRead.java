package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatStockInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoRead {

    public List<String> getStockListCustom(String filename) {
        List<String> stock_list = new ArrayList<>();

        MyExcel myexcel = new MyExcel();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfoCustom(filename, false);
        int size = web_stockinfo.size();
        for(int i =0;i<size;i++) {
            stock_list.add(web_stockinfo.get(i).stock_code);
        }
        return stock_list;
    }

    public List<FormatStockInfo> getStockInfoCustom(String filename) {
        List<String> stock_list = new ArrayList<>();

        MyExcel myexcel = new MyExcel();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfoCustom(filename, false);
        return web_stockinfo;
    }
}
