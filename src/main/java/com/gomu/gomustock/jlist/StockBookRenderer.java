package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.sun.java.accessibility.util.SwingEventMonitor.addListSelectionListener;

public class StockBookRenderer extends JPanel implements ListCellRenderer<StockBook> {

    private JLabel lbFognIcon = new JLabel();
    private JLabel lbAgencyIcon = new JLabel();
    private JLabel lbLoanBuyIcon = new JLabel();
    private JLabel lbLoanSellIcon = new JLabel();

    private JTextArea lbIndication = new JTextArea();
    private JTextArea lbfninfo = new JTextArea();
    private JTextArea lbCompanyinfo = new JTextArea();
    private JTextArea lbNews = new JTextArea();

    private JPanel lbChart = new JPanel();
    private JPanel lbTodayChart = new JPanel();
    private JPanel panelText;
    private JPanel panelFAIcon;
    private JPanel panelChart;

    StockChart schart = new StockChart();
    String STOCKDIR = "D:\\MyML\\GomuStockJ\\";;

    // 콜백인터페이스를 구현한 클래스 인스턴스
    private StockBookRenderer.IFCallback _cb;
    public interface IFCallback {
        public void callback(int target_view, String str);
    }
    public void setCallback(StockBookRenderer.IFCallback cb) {
        this._cb = cb;
    }

    public StockBookRenderer() {
        setLayout(new BorderLayout(5, 5));
        // panelText는 xml 역할을 하고
        // Renderercomponent는 ID역할을 한다
        panelText = new JPanel(new GridLayout(2, 2));
        panelText.add(lbIndication);
        panelText.add(lbCompanyinfo);
        panelText.add(lbfninfo);
        panelText.add(lbNews);

        panelFAIcon = new JPanel(new GridLayout(2, 2));
        panelFAIcon.add(lbFognIcon);
        panelFAIcon.add(lbAgencyIcon);
        panelFAIcon.add(lbLoanBuyIcon);
        panelFAIcon.add(lbLoanSellIcon);

        panelChart = new JPanel(new GridLayout(2, 1));
        //panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelChart.add(lbChart);
        panelChart.add(lbTodayChart);

        add(panelText, BorderLayout.WEST);
        add(panelChart, BorderLayout.CENTER);
        add(panelFAIcon, BorderLayout.EAST);

    }


    @Override
    public Component getListCellRendererComponent(JList<? extends StockBook> list,
                                                  StockBook book, int index, boolean isSelected, boolean cellHasFocus) {

        //renderer_setText(Integer.toString(index));
        //String icon_path = STOCKDIR+book.getIconName() + ".jpg";

        if(book.getStocktype().equals("KSTOCK")) {
            BufferedImage img = schart.getFognimage(book.getStockcode());
            lbFognIcon.setIcon(new ImageIcon(img));

            BufferedImage img2 = schart.getAgencyimage(book.getStockcode());
            lbAgencyIcon.setIcon(new ImageIcon(img2));
        }
/*
		BufferedImage img3 = getLoanBuyMoneyimage(book.getStockcode());
		lbLoanBuyIcon.setIcon( new ImageIcon(img3));

		BufferedImage img4 = getLoanSellMoneyimage(book.getStockcode());
		lbLoanSellIcon.setIcon( new ImageIcon(img4));
*/
        if(book.getStocktype().equals("KETF"))lbIndication.setText(book.getETFInfo());
        else lbIndication.setText(book.getStockInfo());
        lbIndication.setForeground(Color.blue);
        lbIndication.setPreferredSize(new Dimension(100,200));
        lbIndication.setAutoscrolls(true);
        lbIndication.setFont(new Font("TextArea.font", Font.BOLD, 14));

        lbNews.setText(book.getNews());
        //lbNews.setForeground(Color.blue);
        lbNews.setPreferredSize(new Dimension(300,200));
        lbNews.setAutoscrolls(true);

        lbfninfo.setText(book.getfninfo());
        //lbfninfo.setForeground(Color.blue);
        lbfninfo.setPreferredSize(new Dimension(300,200));
        lbfninfo.setLineWrap(true);
        lbfninfo.setFont(new Font("TextArea.font", Font.BOLD, 14));

        lbCompanyinfo.setText(book.getCompanyInfo());
        //lbCompanyinfo.setForeground(Color.blue);
        lbCompanyinfo.setPreferredSize(new Dimension(300,200));
        lbCompanyinfo.setLineWrap(true);

        // set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
        lbChart.removeAll();
        lbChart.add(new XChartPanel(book.getChart()));
        lbChart.setPreferredSize(new Dimension(300,200));
        //lbChart.setForeground(Color.blue);
        //System.out.println("book index " + index);

        // set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
        lbTodayChart.removeAll();
        lbTodayChart.add(new XChartPanel(book.getTodayChart()));
        lbTodayChart.setPreferredSize(new Dimension(300,200));
        //lbTodayChart.setForeground(Color.blue);

        // set Opaque to change background color of JLabel
        lbIndication.setOpaque(true);
        lbCompanyinfo.setOpaque(true);
        lbfninfo.setOpaque(true);
        lbFognIcon.setOpaque(true);
        lbAgencyIcon.setOpaque(true);
        lbChart.setOpaque(true);

        // when select item
        if (isSelected) {
            //

            lbIndication.setBackground(Color.lightGray);
            lbCompanyinfo.setBackground(Color.lightGray);
            lbfninfo.setBackground(Color.lightGray);
            lbNews.setBackground(Color.lightGray);
            lbChart.setBackground(Color.lightGray);
            lbTodayChart.setBackground(Color.lightGray);
            lbFognIcon.setBackground(Color.lightGray);
            lbAgencyIcon.setBackground(Color.lightGray);
            lbLoanBuyIcon.setBackground(Color.lightGray);
            lbLoanSellIcon.setBackground(Color.lightGray);

        } else { // when don't select
            if(index%2==1) {
				/*
				lbIndication.setBackground(Color.lightGray);
				lbCompanyinfo.setBackground(Color.lightGray);
				lbfninfo.setBackground(Color.lightGray);
				lbNews.setBackground(Color.lightGray);
				lbChart.setBackground(Color.lightGray);
				lbTodayChart.setBackground(Color.lightGray);
				lbFognIcon.setBackground(Color.lightGray);
				lbAgencyIcon.setBackground(Color.lightGray);
				lbLoanBuyIcon.setBackground(Color.lightGray);
				lbLoanSellIcon.setBackground(Color.lightGray);

				 */
            } else {
                lbIndication.setBackground(list.getBackground());
                lbCompanyinfo.setBackground(list.getBackground());
                lbfninfo.setBackground(list.getBackground());
                lbNews.setBackground(list.getBackground());
                lbChart.setBackground(list.getBackground());
                lbTodayChart.setBackground(list.getBackground());
                lbFognIcon.setBackground(list.getBackground());
                lbAgencyIcon.setBackground(list.getBackground());
                lbLoanBuyIcon.setBackground(list.getBackground());
                lbLoanSellIcon.setBackground(list.getBackground());
            }
        }

        return this;
    }


    public void renderer_setText(String str) {
        _cb.callback(1,str);
    }


    BufferedImage getLoanBuyMoneyimage(String stock_code)  {
        // 대차잔고현황. 대출받아 매수한 주식
        StockDic dict = new StockDic();
        BufferedImage img=null;
        try {
            URL url;
            if(dict.checkKRStock(stock_code) && (dict.getMarket(stock_code).equals(""))) {
                //url = new URL(STOCKDIR+"android.jpg");
                File imageFile = new File(STOCKDIR+"android.jpg");
                img = ImageIO.read(imageFile);
                return img;
            } else{
                String path = "https://cdn.fnguide.com/SVO2/chartImg/11_01/A" + stock_code + "_BALANCE_01.png";
                url = new URL(path);
                img = ImageIO.read(url);
                return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    BufferedImage getLoanSellMoneyimage(String stock_code) {
        // 차입공매도비중. 주식빌려서 매도한 주식

        StockDic dict = new StockDic();
        BufferedImage img=null;
        try {
            URL url;
            if(dict.checkKRStock(stock_code) && (dict.getMarket(stock_code).equals(""))) {
                //url = new URL(STOCKDIR+"android.jpg");
                File imageFile = new File(STOCKDIR+"android.jpg");
                img = ImageIO.read(imageFile);
                return img;
            } else{
                String path = "https://cdn.fnguide.com/SVO2/chartImg/11_01/A"+stock_code+"_SELL_01.png";
                url = new URL(path);
                img = ImageIO.read(url);
                return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public DefaultListModel<StockBook> loadInfoChart2List(List<FormatStockInfo> web_stockinfo) {
        DefaultListModel<StockBook> model = new DefaultListModel<>();
        int size = web_stockinfo.size();
        String sizestr = Integer.toString(size);
        for(int i=0;i<size;i++) {
            String stock_code = web_stockinfo.get(i).stock_code;
            String target = web_stockinfo.get(i).score;
            if(web_stockinfo.get(i).stock_type.equals("KETF")) target = web_stockinfo.get(i).nav.replace(",","");
            if(target.equals("")) target="1";
            _cb.callback(0,"loading" + "\n" + "info/chart" + "\n" + "to List" + "\n" + Integer.toString(i) + "/"+  sizestr+"\n"+ stock_code);
            XYChart mychart = schart.GetPeriodChart(stock_code);
            XYChart todaychart = schart.GetTodayChart(stock_code,Float.valueOf(target));
            model.addElement(new StockBook(web_stockinfo.get(i), mychart, todaychart));
        }
        return model;
    }
}
