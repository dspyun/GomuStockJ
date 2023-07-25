package main.java.com.gomu.gomustock;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.*;
import main.java.com.gomu.gomustock.stockengin.StockDic;

import org.knowm.xchart.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

public class Main extends JFrame{

    public static void main(String[] args) throws IOException {


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
        //HeaderPanel.setLocation(20,20);
        //HeaderPanel.setPreferredSize(new Dimension(1800,50));

        HeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        HeaderPanel.setBounds(20,20,1600,50);
        frame.add(HeaderPanel,BorderLayout.NORTH);

        JLabel TitleLabel = new JLabel("DLStockInfo");
        TitleLabel.setPreferredSize( new Dimension( 200, 24 ) );
        HeaderPanel.add(TitleLabel);

        JButton button3 = new JButton("주가갱신");
        HeaderPanel.add(button3);
        JButton button6 = new JButton("보유주식");
        HeaderPanel.add(button6);
        JButton button7 = new JButton("파일읽기");
        HeaderPanel.add(button7);

        JTextField textfield = new JTextField();
        HeaderPanel.add(textfield);
        textfield.setText("group_candi");
        textfield.setPreferredSize( new Dimension( 200, 24 ) );

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

                if(button3.equals(ae.getSource())){

                    String filename = textfield.getText();

                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }
                    // 로딩하는데 시간이 많이 걸린다
                    //jcustom.downloadStockInfoCustom(filename);
                    //jcustom.downloadYFPrice(stock_list);
                    idown.downloadNowPrice(stock_list);
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = iread.getStockInfoCustom(filename);

                    // add item to model
                    int size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText("<HTML>" + "Load " + Integer.toString(i) +"/" + Integer.toString(size) + "<br>" +stock_code+"</HTML>");

                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model);
                    listBook.setCellRenderer(new StockBookRenderer());
                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    //frame.add(listpanel,BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                }

                if(button6.equals(ae.getSource())){

                    String filename = "group_hold";

                    DefaultListModel<StockBook> model = new DefaultListModel<>();


                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }
                    // 로딩하는데 시간이 많이 걸린다
                    idown.downloadStockInfoCustom(filename);
                    idown.downloadYFPrice(stock_list);
                    idown.downloadNowPrice(stock_list);

                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = iread.getStockInfoCustom(filename);

                    // add item to model
                    int size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Load " + Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
                        XYChart mychart = schart.GetPeriodChart(stock_code);
                        XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
                        model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
                    }
                    textfield.setText(filename);

                    JList<StockBook> listBook = new JList<StockBook>(model);
                    listBook.setCellRenderer(new StockBookRenderer());
                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    frame.pack();
                    frame.setVisible(true);
                }

                if(button7.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }
                    // 로딩하는데 시간이 많이 걸린다
                    idown.downloadStockInfoCustom(filename);
                    idown.downloadYFPrice(stock_list);
                    idown.downloadNowPrice(stock_list);
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    web_stockinfo = iread.getStockInfoCustom(filename);

                    // add item to model
                    int size = web_stockinfo.size();
                    for(int i=0;i<size;i++) {
                        String stock_code = web_stockinfo.get(i).stock_code;
                        String target = web_stockinfo.get(i).score;
                        if(target.equals("")) target="1";
                        textfield.setText(htmltext("Load " + Integer.toString(i) +"/" + Integer.toString(size) +"\n" +stock_code));
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
            }
        };
        button3.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);

        frame.pack();
        frame.setVisible(true);
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

    public static String htmltext(String input_str) {
        String result;
        result="<HTML>";
        result+=input_str.replaceAll("\n","<br>");
        result+="</HTML>";
        return result;
    }
}
