package main.java.com.gomu.gomustock;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.*;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.StockDic;

import org.knowm.xchart.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main extends JFrame{

    public static void main(String[] args) throws IOException {
        MyWeb myweb = new MyWeb();
        MyExcel myexcel = new MyExcel();
        /*
        List<String> codelist = myexcel.readColumn("upjong_table.xls",0);
        List<String> namelist = myexcel.readColumn("upjong_table.xls",1);
        int size = codelist.size();

        for(int i=1;i<size;i++) {
            String code = codelist.get(i);
            String name = namelist.get(i);
            myweb.checkbox_test2(code, name);
        }
        */

        mytest();
    }

    public static void mytest() throws IOException{
        MyWeb myweb = new MyWeb();
        MyExcel myexcel = new MyExcel();
        StockChart schart = new StockChart();
        InfoDownload idown = new InfoDownload();
        InfoRead iread = new InfoRead();
        String monitor_text="";
        int index = 0;
        //trans();
        Dimension dim = new Dimension(1800,800);
        JFrame frame = new JFrame("XChart Swing Demo");
        frame.setPreferredSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel listpanel = new JPanel(new BorderLayout());
        listpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(listpanel,BorderLayout.CENTER);

        // header panel에는 버튼과 텍스트필드를 넣는다
        JPanel HeaderPanel = new JPanel();
        HeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        HeaderPanel.setBounds(20,20,1600,50);
        frame.add(HeaderPanel,BorderLayout.NORTH);

        JLabel TitleLabel = new JLabel("DLStockInfo");
        TitleLabel.setPreferredSize( new Dimension( 200, 24 ) );
        HeaderPanel.add(TitleLabel);


        JButton button2 = new JButton("보유주식");
        HeaderPanel.add(button2);
        JButton button3 = new JButton("Full다운");
        HeaderPanel.add(button3);

        JTextField textfield = new JTextField();
        HeaderPanel.add(textfield);
        textfield.setText("group_candi");
        textfield.setPreferredSize( new Dimension( 200, 24 ) );

        JButton button4 = new JButton("퀵리뷰");
        HeaderPanel.add(button4);
        JButton button1 = new JButton("오늘차트");
        HeaderPanel.add(button1);
        JButton button9 = new JButton("코드읽기");
        HeaderPanel.add(button9);
        JButton button5 = new JButton("종목뽑기");
        HeaderPanel.add(button5);

        JButton DebugButton = new JButton();
        DebugButton.setText("모니터버튼");
        DebugButton.setPreferredSize( new Dimension( 100, 600 ) );
        frame.add(DebugButton,BorderLayout.WEST);

        textfield.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                //System.out.println("removeUpdate");
            }

            public void insertUpdate(DocumentEvent e) {
                String str = textfield.getText();
                DebugButton.setText(str);
                DebugButton.paint(frame.getGraphics());;
            }
            public void changedUpdate(DocumentEvent e) {
                //System.out.println("changedUpdate");
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {


                // 오늘 가격 다운로드 + 정보읽어 보여주기
                if(button1.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    int size = stock_list.size();
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }

                    // 하루 가격 다운로드
                    int hour=3;
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("TodayPrice" + "\n" + "Download" + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +
                                "\n"+ stock_code));
                        myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
                    }


                    // 다운로드 완료하고 지금부터는 파일을 읽기 시작한다
                    web_stockinfo = iread.getStockInfoCustom(filename);

                    // add item to model
                    size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Info/Chart " + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    frame.pack();
                    frame.setVisible(true);
                }

                if(button2.equals(ae.getSource())){

                    String filename = "group_hold";
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    int size = stock_list.size();
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }
                    // 정보 다운로드
                    textfield.setText(htmltext("Downloadd" + "\n" + "Information"));
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

                    for(int i =0;i<size;i++) {
                        FormatStockInfo stockinfo = new FormatStockInfo();
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("Information" + "\n" + "Download" + "\n" + stock_code));
                        stockinfo = idown.downloadStockInfoOne(stock_code); // 1시간을 읽어서 저장한다
                        web_stockinfo.add(stockinfo);
                    }
                    myexcel.writestockinfoCustom(filename,web_stockinfo);

                    // 하루 가격 다운로드
                    int hour=3;
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("TodayPrice" + "\n" + "Download" + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +
                                "\n"+ stock_code));
                        myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
                    }

                    // 년간 가격 다운로드
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("Price" + "\n" + "Download" + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +
                                "\n"+stock_code));
                        new YFDownload(stock_code);
                    }

                    // 다운로드 완료하고 지금부터는 파일을 읽기 시작한다
                    web_stockinfo = iread.getStockInfoCustom(filename);

                    // add item to model
                    size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Info/Chart " + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    frame.pack();
                    frame.setVisible(true);
                }


                // 입력된 파일을 Full로 다운로드하고 읽고 보여주기
                if(button3.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    int size = stock_list.size();
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }
                    // 정보 다운로드
                    textfield.setText(htmltext("Downloadd" + "\n" + "Information"));
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    for(int i =0;i<size;i++) {
                        FormatStockInfo stockinfo = new FormatStockInfo();
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("Information" + "\n" + "Download" + "\n" + stock_code));
                        stockinfo = idown.downloadStockInfoOne(stock_code); // 1시간을 읽어서 저장한다
                        web_stockinfo.add(stockinfo);
                    }
                    myexcel.writestockinfoCustom(filename,web_stockinfo);

                    // 하루 가격 다운로드
                    int hour=3;
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("TodayPrice" + "\n" + "Download" + "\n" + stock_code));
                        myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
                    }

                    // 년간 가격 다운로드
                    for(int i =0;i<size;i++) {
                        String stock_code = stock_list.get(i);
                        textfield.setText(htmltext("Price" + "\n" + "Download" + "\n" + stock_code));
                        new YFDownload(stock_code);
                    }

                    // 다운로드 완료하고 지금부터는 파일을 읽기 시작한다
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Info/Chart " + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    frame.pack();
                    frame.setVisible(true);
                }

                // information만 읽고 보여주기
                if(button4.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }

                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    int size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Info/Chart " + "\n" +
                                Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    frame.pack();
                    frame.setVisible(true);
                }

                if(button9.equals(ae.getSource())){

                    String filename = textfield.getText();
                    trans(filename);
                    //textfield.setText(htmltext("Finish"));

                    textfield.setText(filename);
                }

                if(button5.equals(ae.getSource())){

                    String filename = textfield.getText();
                    filename = "info_kospi";

                    textfield.setText("종목뽑기");

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
                        textfield.setText(htmltext("info_kospi" +"\n"+ Integer.toString(i*50)));
                    }

                    myexcel.writenaverupjong2(filename,upjonglist );
                    List<String> last_pickup = pickup40over(filename);
                    myexcel.writeColumn(filename, last_pickup,1);
                    trans(filename);
                    textfield.setText("info_kospi");
                    System.out.println("finish");
                }
            }
        };
        button1.addActionListener(listener);
        button2.addActionListener(listener);
        button3.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button9.addActionListener(listener);

        frame.pack();
        frame.setVisible(true);
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

    public static String htmltext(String input_str) {
        String result;
        result="<HTML>";
        result+=input_str.replaceAll("\n","<br>");
        result+="</HTML>";
        return result;
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
}
