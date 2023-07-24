package main.java.com.gomu.gomustock;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import main.java.com.gomu.gomustock.format.FormatETFInfo;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.Book;
import main.java.com.gomu.gomustock.jlist.BookRenderer;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

public class Main extends JFrame{

    public static void main(String[] args) throws IOException {

        //Example exam = new Example();
        //exam.ichimoku_test2();
        String monitor_text="";
        int index = 0;
        //trans();
        Dimension dim = new Dimension(1800,800);
        JFrame frame = new JFrame("XChart Swing Demo");
        frame.setPreferredSize(dim);
        JLabel lblNum;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // header panel에는 버튼과 텍스트필드를 넣는다
        JPanel HeaderPanel = new JPanel();
        HeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.add(HeaderPanel,BorderLayout.NORTH);

        JButton button1 = new JButton("DLStockInfo");
        HeaderPanel.add(button1);
        JButton button2 = new JButton("DLYFPrice");
        HeaderPanel.add(button2);
        JButton button3 = new JButton("DLNowPrice");
        HeaderPanel.add(button3);
        JButton button4 = new JButton("DLFullInfo");
        HeaderPanel.add(button4);
        JButton button5 = new JButton("DLFullPrice");
        HeaderPanel.add(button5);
        JButton button6 = new JButton("StockInfo");
        HeaderPanel.add(button6);
        JButton button7 = new JButton("GroupInfo");
        HeaderPanel.add(button7);

        JTextField textfield = new JTextField();
        HeaderPanel.add(textfield);
        textfield.setText("기본 텍스트");

        JButton DebugButton = new JButton();
        DebugButton.setText("텍스트영역");
        //txtfield.setLineWrap(true);
        frame.add(DebugButton,BorderLayout.WEST);

        JListCustomRenderer stockinfolist = new JListCustomRenderer();

        textfield.getDocument().addDocumentListener(new DocumentListener() {
            int index =0;
            public void removeUpdate(DocumentEvent e) {
                //System.out.println("removeUpdate");
            }

            public void insertUpdate(DocumentEvent e) {
                String str = textfield.getText();
                DebugButton.setText(str);
                index++;

                //txtfield.revalidate();
                //txtfield.validate();
                DebugButton.paint(frame.getGraphics());
                //System.out.println("insertUpdate");
            }
            public void changedUpdate(DocumentEvent e) {
                //System.out.println("changedUpdate");
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                // btnPlus를 선택했을때
                if(button1.equals(ae.getSource())){
                    List<String> stock_list = new ArrayList<>();
                    stock_list = getStockList();
                    MyExcel myexcel = new MyExcel();
                    MyWeb myweb = new MyWeb();
                    StockDic stockdic = new StockDic();
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

                    int size = stock_list.size();
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        FormatStockInfo stockinfo = new FormatStockInfo();
                        FormatETFInfo etfinfo = new FormatETFInfo();
                        fnGuide myfnguide = new fnGuide();
                        String news;
                        textfield.setText("INFO " + stock_code);
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
                    myexcel.writestockinfo(0,web_stockinfo);
                    frame.revalidate();
                    frame.validate();
                    frame.paint(frame.getGraphics());
                }
                if(button2.equals(ae.getSource())){
                    List<String> stock_list = new ArrayList<>();
                    stock_list = getStockList();
                    int size = stock_list.size();
                    String stock_code;
                    for(int i =0;i<size;i++) {
                        stock_code = stock_list.get(i);
                        textfield.setText("YF " + stock_code);
                        new YFDownload(stock_code);
                    }
                }
                if(button3.equals(ae.getSource())){

                    MyExcel myexcel = new MyExcel();
                    MyWeb myweb = new MyWeb();
                    List<String> stock_list = new ArrayList<>();
                    stock_list = getStockList();
                    int hour = 3;
                    int size = stock_list.size();
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(Integer.toString(i) + "\n" + "NOW " + stock_code);
                        myweb.getNaverpriceByToday(stock_code, 6 * hour); // hour시간을 읽어서 저장한다
                    }
                    frame.revalidate();
                    frame.validate();
                    frame.paint(frame.getGraphics());
                }
                if(button4.equals(ae.getSource())){
                    List<String> stock_list = new ArrayList<>();

                    MyExcel myexcel = new MyExcel();
                    MyWeb myweb = new MyWeb();
                    StockDic stockdic = new StockDic();
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = myexcel.readStockinfo(1,false);
                    int size = web_stockinfo.size();
                    for(int i =0;i<size;i++) {
                        stock_list.add(web_stockinfo.get(i).stock_code);
                    }
                    int i=0;
                    for(i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        FormatStockInfo stockinfo = new FormatStockInfo();
                        FormatETFInfo etfinfo = new FormatETFInfo();
                        fnGuide myfnguide = new fnGuide();
                        String news;
                        String newLine = System.getProperty("line.separator"); //시스템의 줄바꿈 기호 얻어오기
                        //textArea.append(newLine); //JTextArea에 줄바꿈 추가

                        textfield.setText("FULL" + Integer.toString(i) + newLine + stock_code);
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
                    textfield.setText("FULL" + Integer.toString(i) + " Finish");
                    myexcel.writestockinfo(1,web_stockinfo);
                }

                if(button5.equals(ae.getSource())){
                    MyExcel myexcel = new MyExcel();
                    List<String> stock_list = new ArrayList<>();
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = myexcel.readStockinfo(1,false);
                    int size = web_stockinfo.size();
                    for(int i =0;i<size;i++) {
                        stock_list.add(web_stockinfo.get(i).stock_code);
                    }

                    String stock_code;
                    for(int i =0;i<size;i++) {
                        stock_code = stock_list.get(i);
                        textfield.setText("YF " + Integer.toString(i) + stock_code);
                        new YFDownload(stock_code);
                    }
                }
                if(button6.equals(ae.getSource())){

                    try {
                        JListCustomRenderer jcustom = new JListCustomRenderer();

                        DefaultListModel<Book> model = new DefaultListModel<>();
                        JList<Book> listBook = new JList<Book>(model);
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                        List<String> stock_list = new ArrayList<>();
                        stock_list = jcustom.getStockList();
                        //downloadStockInfo(stock_list);
                        //downloadYFrice(stock_list);
                        jcustom.downloadNowPrice(stock_list);
                        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                        web_stockinfo = jcustom.getStockInfo();

                        // add item to model
                        int size = web_stockinfo.size();
                        for(int i=0;i<size;i++) {
                            String stock_code = web_stockinfo.get(i).stock_code;
                            String target = web_stockinfo.get(i).score;
                            if(target.equals("")) target="1";
                            //System.out.println(stock_code);
                            textfield.setText("<HTML>" + "Load " + Integer.toString(i) +"/" + Integer.toString(size) + "<br>" +stock_code+"</HTML>");
                            XYChart mychart = jcustom.GetChart(stock_code);
                            XYChart todaychart = jcustom.GetTodayChart(stock_code,Float.valueOf(target));
                            model.addElement(new main.java.com.gomu.gomustock.jlist.Book(web_stockinfo.get(i), mychart, todaychart));
                        }

                        // set cell renderer
                        listBook.setCellRenderer(new BookRenderer());
                        panel.add(new JScrollPane(listBook),
                                BorderLayout.CENTER);
                        Dimension dim1 = new Dimension(1200,500);
                        panel.setPreferredSize(dim);
                        frame.add(panel,BorderLayout.CENTER);

                        frame.pack();
                        frame.setVisible(true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if(button7.equals(ae.getSource())){

                    String filename = textfield.getText();
                    try {
                        JListCustomRenderer jcustom = new JListCustomRenderer();

                        DefaultListModel<Book> model = new DefaultListModel<>();

                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

                        List<String> stock_list = new ArrayList<>();
                        stock_list = jcustom.getStockListCustom(filename);
                        if(stock_list.size()==0) {
                            frame.pack();
                            frame.setVisible(true);
                            return;
                        }
                        // 로딩하는데 시간이 많이 걸린다
                        jcustom.downloadStockInfoCustom(filename);
                        jcustom.downloadYFrice(stock_list);
                        jcustom.downloadNowPrice(stock_list);
                        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                        web_stockinfo = jcustom.getStockInfoCustom(filename);

                        // add item to model
                        int size = web_stockinfo.size();
                        for(int i=0;i<size;i++) {
                            String stock_code = web_stockinfo.get(i).stock_code;
                            String target = web_stockinfo.get(i).score;
                            if(target.equals("")) target="1";
                            //System.out.println(stock_code);
                            textfield.setText("<HTML>" + "Load " + Integer.toString(i) +"/" + Integer.toString(size) + "<br>" +stock_code+"</HTML>");
                            XYChart mychart = jcustom.GetChart(stock_code);
                            XYChart todaychart = jcustom.GetTodayChart(stock_code,Float.valueOf(target));
                            model.addElement(new main.java.com.gomu.gomustock.jlist.Book(web_stockinfo.get(i), mychart, todaychart));
                        }
                        JList<Book> listBook = new JList<Book>(model);
                        // set cell renderer
                        listBook.setCellRenderer(new BookRenderer());
                        panel.add(new JScrollPane(listBook),
                                BorderLayout.CENTER);
                        Dimension dim1 = new Dimension(1200,500);
                        panel.setPreferredSize(dim);
                        frame.add(panel,BorderLayout.CENTER);

                        frame.pack();
                        frame.setVisible(true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        button1.addActionListener(listener);
        button2.addActionListener(listener);
        button3.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);
/*
        JPanel mypanel = new JPanel();
        mypanel = stockinfolist.createMainPanel();
        Dimension dim1 = new Dimension(1200,500);
        mypanel.setPreferredSize(dim);
        frame.add(mypanel,BorderLayout.CENTER);
*/
        frame.pack();
        frame.setVisible(true);
    }



    public static void downloadStockInfo(List<String> stock_list) {
        MyExcel myexcel = new MyExcel();
        MyWeb myweb = new MyWeb();
        StockDic stockdic = new StockDic();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

        int hour = 4;
        int size = stock_list.size();
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

                myweb.getNaverpriceByToday(stock_code,6*hour); // 1시간을 읽어서 저장한다
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
                myweb.getNaverpriceByToday(stock_code,6*hour); // 1시간을 읽어서 저장한다
            }

            new YFDownload(stock_list.get(i));
        }
        myexcel.writestockinfo(0,web_stockinfo);
    }

    public static List<String> getStockList() {
        List<String> stock_list = new ArrayList<>();

        MyExcel myexcel = new MyExcel();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
        web_stockinfo = myexcel.readStockinfo(0,false);
        int size = web_stockinfo.size();
        for(int i =0;i<size;i++) {
            stock_list.add(web_stockinfo.get(i).stock_code);
        }
        return stock_list;
    }

    public static void trans() {
        MyExcel myexcel = new MyExcel();
        List<String> name = myexcel.readColumn("trans.xls",1);
        StockDic mydict = new StockDic();
        List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

        int size = name.size();
        for(int i =0;i<size;i++) {
            FormatStockInfo oneinfo = new FormatStockInfo();
            String stock_code = mydict.getStockcode(name.get(i));
            System.out.println(stock_code);
            oneinfo.stock_code = stock_code;
            web_stockinfo.add(oneinfo);
        }
        myexcel.writestockinfo(0,web_stockinfo);
    }
}
