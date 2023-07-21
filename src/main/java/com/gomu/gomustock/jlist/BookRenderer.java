package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.jlist.Book;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookRenderer extends JPanel implements ListCellRenderer<Book> {

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

	String STOCKDIR = "D:\\MyML\\GomuStockJ\\";;

	public BookRenderer() {
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
	public Component getListCellRendererComponent(JList<? extends Book> list,
			Book book, int index, boolean isSelected, boolean cellHasFocus) {

		System.out.println("index = " + index);
		//String icon_path = STOCKDIR+book.getIconName() + ".jpg";
		BufferedImage img = null;
		try {
			img = getFognimage(book.getStockcode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		lbFognIcon.setIcon( new ImageIcon(img));

		BufferedImage img2 = null;
		try {
			img2 = getAgencyimage(book.getStockcode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		lbAgencyIcon.setIcon( new ImageIcon(img2));
/*
		BufferedImage img3 = null;
		img3 = getLoanBuyMoneyimage(book.getStockcode());
		lbLoanBuyIcon.setIcon( new ImageIcon(img3));

		BufferedImage img4 = null;
		img4 = getLoanSellMoneyimage(book.getStockcode());
		lbLoanSellIcon.setIcon( new ImageIcon(img4));
*/
		lbIndication.setText(book.getIndication());
		lbIndication.setForeground(Color.blue);
		lbIndication.setPreferredSize(new Dimension(100,200));
		lbIndication.setAutoscrolls(true);

		lbNews.setText(book.getNews());
		lbNews.setForeground(Color.blue);
		lbNews.setPreferredSize(new Dimension(300,200));
		lbNews.setAutoscrolls(true);

		lbfninfo.setText(book.getfninfo());
		lbfninfo.setForeground(Color.blue);
		lbfninfo.setPreferredSize(new Dimension(300,200));
		lbfninfo.setLineWrap(true);

		lbCompanyinfo.setText(book.getCompanyInfo());
		lbCompanyinfo.setForeground(Color.blue);
		lbCompanyinfo.setPreferredSize(new Dimension(300,200));
		lbCompanyinfo.setLineWrap(true);

		// set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
		lbChart.removeAll();
		lbChart.add(new XChartPanel(book.getChart()));
		lbChart.setPreferredSize(new Dimension(300,200));
		lbChart.setForeground(Color.blue);
		//System.out.println("book index " + index);

		// set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
		lbTodayChart.removeAll();
		lbTodayChart.add(new XChartPanel(book.getTodayChart()));
		lbTodayChart.setPreferredSize(new Dimension(300,200));
		lbTodayChart.setForeground(Color.blue);

		// set Opaque to change background color of JLabel
		lbIndication.setOpaque(true);
		lbCompanyinfo.setOpaque(true);
		lbfninfo.setOpaque(true);
		lbFognIcon.setOpaque(true);
		lbAgencyIcon.setOpaque(true);
		lbChart.setOpaque(true);

		// when select item
		if (isSelected) {
			lbIndication.setBackground(list.getSelectionBackground());
			lbCompanyinfo.setBackground(list.getSelectionBackground());
			lbfninfo.setBackground(list.getSelectionBackground());
			lbNews.setBackground(list.getSelectionBackground());
			lbChart.setBackground(list.getSelectionBackground());
			lbTodayChart.setBackground(list.getSelectionBackground());
			lbFognIcon.setBackground(list.getSelectionBackground());
			lbAgencyIcon.setBackground(list.getSelectionBackground());
			lbLoanBuyIcon.setBackground(list.getSelectionBackground());
			lbLoanSellIcon.setBackground(list.getSelectionBackground());

		} else { // when don't select
			if(index%2==1) {
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

	BufferedImage getFognimage(String stock_code) throws IOException {

		String path = "https://ssl.pstatic.net/imgfinance/chart/trader/month1/F_"+stock_code+".png";
		URL url = new URL(path);
		BufferedImage img = ImageIO.read(url);
		return img;
	}
	BufferedImage getAgencyimage(String stock_code) throws IOException {

		String path = "https://ssl.pstatic.net/imgfinance/chart/trader/month1/I_"+stock_code+".png";
		URL url = new URL(path);
		BufferedImage img = ImageIO.read(url);
		return img;
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
}
