package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatAccount;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class StockOnePage {
    String news;
    String company_info;
    String statinfo;
    String finaninfo,etfinfo;
    String stock_code, stock_name,stock_type;

    private JPanel toppanel = new JPanel();

    private JPanel panelText;
    private JPanel panelFAIcon;
    private JPanel panelChart;

    private JTextArea lbstatic = new JTextArea();
    private JTextArea lbfinan = new JTextArea();
    private JTextArea lbCompanyinfo = new JTextArea();
    private JTextArea lbNews = new JTextArea();
    private JTextArea lbMemo = new JTextArea();
    private JTextArea lbBalance = new JTextArea();

    private JPanel lbChart = new JPanel();
    private JPanel lbTodayChart = new JPanel();
    private JPanel lbYearChart = new JPanel();

    private JLabel lbFognIcon = new JLabel();
    private JLabel lbAgencyIcon = new JLabel();
    private JLabel lbLoanBuyIcon = new JLabel();
    private JLabel lbLoanSellIcon = new JLabel();



    public StockOnePage() {

    }

    public StockOnePage(String filename, String stock_code) {
        MyExcel myexcel = new MyExcel();
        List<FormatStockInfo> stockinfo = myexcel.readStockinfoCustom(filename, false);
        int size = stockinfo.size();
        for(int i =0;i<size;i++) {
            if(stock_code.equals(stockinfo.get(i).stock_code)) {
                FormatStockInfo oneinfo = new FormatStockInfo();
                oneinfo = stockinfo.get(i);
                this.stock_code = oneinfo.stock_code;
                stock_name = oneinfo.stock_name;
                news = oneinfo.news;
                company_info = oneinfo.desc;
                statinfo = oneinfo.toString();
                finaninfo = oneinfo.fninfo;
                etfinfo = oneinfo.etfinfo;
                stock_type = oneinfo.stock_type;
            }
        }

        setPaneldata();
        //toppanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        toppanel.setLayout(new GridLayout(1, 1));
        //toppanel.setPreferredSize(new Dimension(1500,1200));
        addPanel();
    }
    public JPanel getPanel() {
        return toppanel;
    }

    public void setPaneldata() {
        //renderer_setText(Integer.toString(index));
        //String icon_path = STOCKDIR+book.getIconName() + ".jpg";

        int height = 200;
        int width = 300;
        StockChart schart = new StockChart();
        InfoRead iread = new InfoRead();

        if(stock_type.equals("KETF"))lbstatic.setText(etfinfo);
        else lbstatic.setText(statinfo);
        lbstatic.setForeground(Color.blue);
        lbstatic.setAutoscrolls(true);
        lbstatic.setFont(new Font("TextArea.font", Font.BOLD, 12));
        lbstatic.setPreferredSize(new Dimension(width,height));

        lbNews.setText(news);
        //lbNews.setForeground(Color.blue);
        lbNews.setAutoscrolls(true);
        lbNews.setPreferredSize(new Dimension(width,height));

        lbfinan.setText(finaninfo);
        lbfinan.setForeground(Color.blue);
        lbfinan.setLineWrap(true);
        lbfinan.setFont(new Font("TextArea.font", Font.BOLD, 12));
        lbfinan.setPreferredSize(new Dimension(width,height));

        lbCompanyinfo.setText(company_info);
        //lbCompanyinfo.setForeground(Color.blue);
        lbCompanyinfo.setLineWrap(true);
        lbCompanyinfo.setPreferredSize(new Dimension(width,height));

        lbMemo.setText(iread.getStockMemo(stock_code));
        //lbCompanyinfo.setForeground(Color.blue);
        lbMemo.setLineWrap(true);
        lbMemo.setPreferredSize(new Dimension(width,height));

        lbBalance.setText("balance");
        //lbCompanyinfo.setForeground(Color.blue);
        lbBalance.setLineWrap(true);
        lbCompanyinfo.setPreferredSize(new Dimension(width,height));

        // 6개월 차트 : set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
        lbChart.removeAll();
        lbChart.add(new XChartPanel(schart.GetPeriodChart(stock_code,120)));
        lbChart.setPreferredSize(new Dimension(width,height));

        // 1일 차트 : set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
        lbTodayChart.removeAll();
        lbTodayChart.add(new XChartPanel(schart.GetTodayChart(stock_code,1)));
        lbTodayChart.setPreferredSize(new Dimension(width,height));
        //1년 차트
        lbYearChart.removeAll();
        lbYearChart.add(new XChartPanel(schart.GetPeriodChart(stock_code,240)));
        lbYearChart.setPreferredSize(new Dimension(width,height));


        BufferedImage img = schart.getFognimage(stock_code);
        Image img_1 = img.getScaledInstance(250,200, Image.SCALE_SMOOTH);
        lbFognIcon.setIcon( new ImageIcon(img_1));
        lbFognIcon.setPreferredSize(new Dimension(width,height));

        BufferedImage img2 = schart.getAgencyimage(stock_code);
        Image img2_1 = img2.getScaledInstance(250,200, Image.SCALE_SMOOTH);
        lbAgencyIcon.setIcon( new ImageIcon(img2_1));
        lbAgencyIcon.setPreferredSize(new Dimension(width,height));

        BufferedImage img3 = schart.getLoanBuyMoneyimage(stock_code);
        Image img3_1 = img3.getScaledInstance(250,200, Image.SCALE_SMOOTH);
        lbLoanBuyIcon.setIcon( new ImageIcon(img3_1));
        lbLoanBuyIcon.setPreferredSize(new Dimension(width,height));

        BufferedImage img4 = schart.getLoanSellMoneyimage(stock_code);
        Image img4_1 = img4.getScaledInstance(250,200, Image.SCALE_SMOOTH);
        lbLoanSellIcon.setIcon( new ImageIcon(img4_1));
        lbLoanSellIcon.setPreferredSize(new Dimension(width,height));

        // set Opaque to change background color of JLabel
        lbstatic.setOpaque(true);
        lbCompanyinfo.setOpaque(true);
        lbfinan.setOpaque(true);
        lbNews.setOpaque(true);
        lbMemo.setOpaque(true);
        lbChart.setOpaque(true);
        lbTodayChart.setOpaque(true);
        lbYearChart.setOpaque(true);
        lbBalance.setOpaque(true);
        lbFognIcon.setOpaque(true);
        lbAgencyIcon.setOpaque(true);


        lbstatic.setBackground(getBackground());
        lbCompanyinfo.setBackground(getBackground());
        lbfinan.setBackground(getBackground());
        lbNews.setBackground(getBackground());
        lbMemo.setBackground(getBackground());
        lbBalance.setBackground(getBackground());
        lbChart.setBackground(getBackground());
        lbTodayChart.setBackground(getBackground());
        lbYearChart.setBackground(getBackground());
        lbFognIcon.setBackground(getBackground());
        lbAgencyIcon.setBackground(getBackground());
        lbLoanBuyIcon.setBackground(getBackground());
        lbLoanSellIcon.setBackground(getBackground());
    }

    Color getBackground() {
        return Color.lightGray;
    }
    void addPanel() {
        int height1 = 600;
        int width1 = 1800;
        JPanel upperpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //upperpanel.setPreferredSize(new Dimension(1600,300));

        panelText = new JPanel(new GridLayout(3, 2));
        panelText.setPreferredSize(new Dimension(600,height1));
        panelText.add(lbstatic);
        panelText.add(lbCompanyinfo);
        panelText.add(lbfinan);
        panelText.add(lbNews);
        panelText.add(lbMemo);
        panelText.add(lbBalance);

        panelChart = new JPanel(new GridLayout(3, 1));
        panelChart.setPreferredSize(new Dimension(300,height1));
        //panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelChart.add(lbChart);
        panelChart.add(lbTodayChart);
        panelChart.add(lbYearChart);

        panelFAIcon = new JPanel(new GridLayout(3, 2));
        panelFAIcon.setPreferredSize(new Dimension(500,height1));
        panelFAIcon.add(lbFognIcon);
        panelFAIcon.add(lbAgencyIcon);
        panelFAIcon.add(lbLoanBuyIcon);
        panelFAIcon.add(lbLoanSellIcon);

        // flowlayout이라서 왼쪽에서 오른쪽으로 차례대로 보여준다
        toppanel.removeAll();
        upperpanel.removeAll();
        upperpanel.add(panelText);
        upperpanel.add(panelChart);
        upperpanel.add(panelFAIcon);
        toppanel.add(upperpanel);

    }
}
