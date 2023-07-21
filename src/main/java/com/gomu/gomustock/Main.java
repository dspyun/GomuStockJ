package main.java.com.gomu.gomustock;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.JListCustomRenderer;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.network.fnGuide;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.Balance;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import main.java.com.gomu.gomustock.stockengin.TAlib;

import org.knowm.xchart.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.Book;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

public class Main extends JFrame{
    public static JScrollPane scrollPane;
    public static JList chartList;

    private static final int W = 200;
    private static final int H = W;
    private static final int N = 100;
    private static final int NumberCharts = 20;
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        //fnGuide myfnguide = new fnGuide();
        //String result = myfnguide.getFnguideInfo("005930");
        //System.out.println(result);
        //trans();
        new JListCustomRenderer();
    }


    public static void trans() {
        MyExcel myexcel = new MyExcel();
        List<String> name = myexcel.readColumn("pension.xls",2);
        StockDic mydict = new StockDic();
        int size = name.size();
        for(int i =0;i<size;i++) {
            String stock_code = mydict.getStockcode(name.get(i));
            System.out.println(stock_code);
        }
    }
}
