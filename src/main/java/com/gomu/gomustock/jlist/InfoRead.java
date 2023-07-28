package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatStockInfo;

import java.util.ArrayList;
import java.util.List;

public class InfoRead {

    MyExcel myexcel = new MyExcel();

    public InfoRead() {

    }


    public interface IFCallback {
        public void callback(String str);
    }
    // 콜백인터페이스를 구현한 클래스 인스턴스
    private InfoRead.IFCallback _cb;
    public void setCallback(InfoRead.IFCallback cb) {
        this._cb = cb;
    }

    public List<String> getStockListCustom(String filename) {
        List<String> stock_list = new ArrayList<>();

        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfoCustom(filename, false);
        int size = web_stockinfo.size();
        for(int i =0;i<size;i++) {

            stock_list.add(web_stockinfo.get(i).stock_code);
        }
        return stock_list;
    }

    public List<FormatStockInfo> getStockInfoCustom(String filename) {

        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfoCustom(filename, false);
        _cb.callback("complete" + "\n"+"+read stockinfo");
        return web_stockinfo;
    }
}
