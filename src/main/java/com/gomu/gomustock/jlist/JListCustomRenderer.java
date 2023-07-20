package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.Example;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class JListCustomRenderer extends JFrame {
	private int width = 800;
	private int height = 400;
	private JList<Book> listBook;


	public JListCustomRenderer() throws UnsupportedEncodingException {

		add(createMainPanel());

		// set display
		setTitle("JLIstCustomRenderer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel createMainPanel() throws UnsupportedEncodingException {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// create list book and set to scrollpane and add to panel
		panel.add(new JScrollPane(listBook = createListBooks()),
				BorderLayout.CENTER);

		return panel;
	}

	private JList<Book> createListBooks() throws UnsupportedEncodingException {
		// create List model
		DefaultListModel<Book> model = new DefaultListModel<>();
		List<String> stock_list = new ArrayList<>();
		stock_list = getStockList();
		downloadStockInfo(stock_list);
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = getStockInfo();

		// add item to model
		int size = web_stockinfo.size();
		for(int i=0;i<size;i++) {
			//XYChart mychart = new XYChartBuilder().build();
			String stock_code = web_stockinfo.get(i).stock_code;
			XYChart mychart = GetChart(stock_code);
			model.addElement(new Book(web_stockinfo.get(i).toString(), web_stockinfo.get(i).getDSC(), web_stockinfo.get(i).news,"cpp", mychart));
		}

		// create JList with model
		JList<Book> list = new JList<Book>(model);
		// set cell renderer
		list.setCellRenderer(new BookRenderer());
		return list;
	}

	private XYChart GetChart(String stock_code) {

		Color[] colors = {Color.RED, Color.BLUE,Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};

		PriceBox kbbank = new PriceBox(stock_code);
		List<Float> kbband_close = kbbank.getClose(120);
		BBandTest bbtest = new BBandTest(stock_code,kbband_close,120);

		// Create Chart & add first data
		int size = kbband_close.size();
		List<Float> x = new ArrayList<>();
		for(int i =0;i<size;i++) { x.add((float)i); }
		XYChart chart  = new XYChartBuilder().width(300).height(200).build();
		chart.addSeries("price",kbband_close);
		//chart.addSeries("upper_line",bbtest.getUpperLine());
		//chart.addSeries("middle_line",bbtest.getMiddleLine());
		//chart.addSeries("low_line",bbtest.getLowLine());
		List<Float> buyscore = bbtest.scaled_percentb();
		chart.addSeries("buysignal",buyscore);
		chart.getStyler().setMarkerSize(0);
		chart.getStyler().setSeriesColors(colors);
		chart.getStyler().setLegendVisible(false);
		//chart.getStyler().setYAxisTicksVisible(true);
		return chart;
	}

	public List<String> getStockList() {
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

	public void downloadStockInfo(List<String> stock_list) {
		MyExcel myexcel = new MyExcel();
		MyWeb myweb = new MyWeb();
		StockDic stockdic = new StockDic();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

		int size = stock_list.size();
		for(int i =0;i<size;i++) {
			String stock_code = stock_list.get(i);
			FormatStockInfo stockinfo = new FormatStockInfo();
			String news;
			if(stockdic.checkKRStock(stock_code) && (stockdic.getMarket(stock_code)!="")) {

				stockinfo = myweb.getNaverStockinfo(stock_code);
				stockinfo.stock_code = stock_code;

				news = myweb.getNaverStockNews(stock_code);
				stockinfo.news = news;

				web_stockinfo.add(stockinfo);

			} else {
				stockinfo.fill_empty();
				stockinfo.stock_code = stock_code;
				// etf는 stock name 검색이 안된다
				//stockinfo.stock_name = stockdic.getStockname(stock_code);
				web_stockinfo.add(stockinfo);
			}

			new YFDownload(stock_list.get(i));
		}
		myexcel.writestockinfo(0,web_stockinfo);
	}

	public List<FormatStockInfo> getStockInfo() {
		List<String> stock_list = new ArrayList<>();

		MyExcel myexcel = new MyExcel();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = myexcel.readStockinfo(0, false);
		return web_stockinfo;
	}
	/*
	public static void main(String[] args) {
		new JListCustomRenderer();
	}
	*/
}
