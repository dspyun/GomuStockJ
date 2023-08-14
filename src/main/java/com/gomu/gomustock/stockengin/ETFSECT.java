package main.java.com.gomu.gomustock.stockengin;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatStockInfo;

import java.util.ArrayList;
import java.util.List;

public class ETFSECT {

    List<String> namelist = new ArrayList<>();
    List<String> companylist = new ArrayList<>();
    List<String> filelist = new ArrayList<>();

    public ETFSECT() {

    }

    public void loadETFinfo() {
        MyExcel myexcel = new MyExcel();
        List<FormatStockInfo> etfinfolist = myexcel.readSectorETFinfo();

        int size = etfinfolist.size();
        for(int i=0;i<size;i++) {
            if(etfinfolist.get(i).stock_type.equals("KETF")) {
                namelist.add(etfinfolist.get(i).stock_name);
                companylist.add(etfinfolist.get(i).etfcompanies);
            }
        }

        StockDic stockdic = new StockDic();
        size = namelist.size();
        for(int i =0;i<size;i++) {
            List<FormatStockInfo> etflist = new ArrayList<>();
            String filename = namelist.get(i).replaceAll(" ", "_");
            String[] complist = companylist.get(i).split(" ");
            int size2 = complist.length;
            for(int j=0;j<size2;j++) {
                FormatStockInfo oneetf = new FormatStockInfo();
                oneetf.stock_name = complist[j];
                oneetf.stock_code = stockdic.getStockcode(oneetf.stock_name);
                etflist.add(oneetf);
            }
            myexcel.writestockinfoCustom(filename,etflist);
            filelist.add(filename);
        }
    }

    public List<String> getFileList() {
        return filelist;
    }

    public static String[] getKRXSectorName() {
        MyExcel myexcel = new MyExcel();
        List<String> result = new ArrayList<>();
        List<FormatStockInfo> stocklist = myexcel.readStockinfoCustom("group_sector",false);
        int size = stocklist.size();
        for(int i=0;i<size;i++) {
            if(stocklist.get(i).stock_type.equals("KETF"))
                result.add(stocklist.get(i).stock_name.replaceAll(" ","_"));
        }
        return result.toArray(new String[result.size()]);
    }

}
