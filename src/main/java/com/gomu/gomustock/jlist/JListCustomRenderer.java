package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.Example;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;
import main.java.com.gomu.gomustock.format.FormatETFInfo;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.network.fnGuide;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.IchimokuTest;
import main.java.com.gomu.gomustock.stockengin.RSITest;
import main.java.com.gomu.gomustock.stockengin.StockDic;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class JListCustomRenderer extends JFrame {
	private int width = 800;
	private int height = 400;
	private JList<Book> listBook;


	public JListCustomRenderer() throws UnsupportedEncodingException {

		//add(createMainPanel());

		// set display
		//setTitle("JLIstCustomRenderer");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setSize(width, height);
		//setLocationRelativeTo(null);
		//setVisible(true);
	}

	public JPanel createMainPanel() throws UnsupportedEncodingException {


		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// create list book and set to scrollpane and add to panel
		panel.add(new JScrollPane(listBook = createListBooks()),
				BorderLayout.CENTER);

		return panel;
	}

	public JPanel createMainPanelCustom(String filename) throws UnsupportedEncodingException {


		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// create list book and set to scrollpane and add to panel
		panel.add(new JScrollPane(listBook = createListBooksCustom(filename)),
				BorderLayout.CENTER);

		return panel;
	}

	private JList<Book> createListBooks() throws UnsupportedEncodingException {
		// create List model
		DefaultListModel<Book> model = new DefaultListModel<>();
		List<String> stock_list = new ArrayList<>();
		stock_list = getStockList();
		//downloadStockInfo(stock_list);
		//downloadYFrice(stock_list);
		downloadNowPrice(stock_list);
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = getStockInfo();

		// add item to model
		int size = web_stockinfo.size();
		for(int i=0;i<size;i++) {
			String stock_code = web_stockinfo.get(i).stock_code;
			String target = web_stockinfo.get(i).score;
			if(target.equals("")) target="1";
			//System.out.println(stock_code);
			XYChart mychart = GetChart(stock_code);
			XYChart todaychart = GetTodayChart(stock_code,Float.valueOf(target));
			model.addElement(new Book(web_stockinfo.get(i), mychart, todaychart));
		}

		// create JList with model
		JList<Book> list = new JList<Book>(model);
		// set cell renderer
		list.setCellRenderer(new BookRenderer());
		return list;
	}


	private JList<Book> createListBooksCustom(String filename) throws UnsupportedEncodingException {
		// create List model
		DefaultListModel<Book> model = new DefaultListModel<>();
		List<String> stock_list = new ArrayList<>();
		stock_list = getStockListCustom(filename);
		//downloadStockInfo(stock_list);
		//downloadYFrice(stock_list);
		downloadNowPrice(stock_list);
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = getStockInfo();

		// add item to model
		int size = web_stockinfo.size();
		for(int i=0;i<size;i++) {
			String stock_code = web_stockinfo.get(i).stock_code;
			String target = web_stockinfo.get(i).score;
			if(target.equals("")) target="1";
			//System.out.println(stock_code);
			XYChart mychart = GetChart(stock_code);
			XYChart todaychart = GetTodayChart(stock_code,Float.valueOf(target));
			model.addElement(new Book(web_stockinfo.get(i), mychart, todaychart));
		}

		// create JList with model
		JList<Book> list = new JList<Book>(model);
		// set cell renderer
		list.setCellRenderer(new BookRenderer());
		return list;
	}

	public XYChart GetChart(String stock_code) {

        int test_period = 120;
		Color[] colors = {Color.RED, Color.GRAY, Color.GRAY, Color.BLUE,Color.GREEN,Color.ORANGE,Color.BLUE};
		MyStat mystat = new MyStat();
		PriceBox kbbank = new PriceBox(stock_code);
		List<Float> kbband_close = kbbank.getClose(test_period);
		BBandTest bbtest = new BBandTest(stock_code,kbband_close,test_period);
		RSITest rsitest = new RSITest(stock_code,kbband_close,test_period);
		List<Float> rsi_line = rsitest.test_line();


		// Create Chart & add first data
		float linewidth=1.5f;
		int size = kbband_close.size();
		List<Float> x = new ArrayList<>();
		for(int i =0;i<size;i++) { x.add((float)i); }
		XYChart chart  = new XYChartBuilder().width(300).height(200).build();
		XYSeries series = chart.addSeries("price",kbband_close);
		series.setLineWidth(linewidth);
		XYSeries series_u = chart.addSeries("upper_line",bbtest.getUpperLine());
		series_u.setLineWidth(linewidth);
		//chart.addSeries("middle_line",bbtest.getMiddleLine());
		XYSeries series_l = chart.addSeries("low_line",bbtest.getLowLine());
		series_l.setLineWidth(linewidth);
		List<Float> buyscore = bbtest.scaled_percentb();
		XYSeries series_b = chart.addSeries("buysignal",buyscore);
		series_b.setLineWidth(linewidth);

        IchimokuTest ichi = new IchimokuTest("005930", kbband_close, test_period);
        List<Float> prospan1line = ichi.getProspan1();
        XYSeries series_p1 = chart.addSeries("prospan1line",mystat.leveling_float(prospan1line));
        series_p1.setLineWidth(linewidth);

        List<Float> prospan2line = ichi.getProspan2();
        XYSeries series_p2 = chart.addSeries("prospan2line",mystat.leveling_float(prospan2line));
        series_p2.setLineWidth(linewidth);

		//rsi_line = mystat.leveling_float(rsi_line,  Collections.min(bbtest.getLowLine()));
		//XYSeries series_r = chart.addSeries("rsisignal",rsi_line);
		//series_r.setLineWidth(linewidth);

		chart.getStyler().setMarkerSize(0);
		chart.getStyler().setSeriesColors(colors);
		chart.getStyler().setLegendVisible(false);
		//chart.getStyler().setYAxisTicksVisible(true);
		return chart;
	}

	public XYChart GetTodayChart(String stock_code, float input_target) {

		Color[] colors = {Color.RED, Color.BLUE,Color.BLACK, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};

        int hour = 60*4;
		MyExcel myexcel = new MyExcel();
		List<String> dealprice = myexcel.readtodayprice(stock_code+"today","DEAL",-1,false);
        List<String> sellprice = myexcel.readtodayprice(stock_code+"today","SELL",-1,false);
        List<String> buyprice = myexcel.readtodayprice(stock_code+"today","BUY",-1,false);
		List<Float> kbband_deal = myexcel.string2float_fillpre(dealprice,1);
        List<Float> kbband_sell = myexcel.string2float_fillpre(sellprice,1);
        List<Float> kbband_buy = myexcel.string2float_fillpre(buyprice,1);
		List<Float> targetlist = new ArrayList<>();

		float target;
		if(input_target==1) target = kbband_buy.get(0);
		else target = input_target;
		int size = kbband_buy.size();

		for(int i =0;i<size;i++) {
			targetlist.add(target);
		}

		// Create Chart & add first data
		float linewidth=1.5f;
		XYChart chart  = new XYChartBuilder().width(300).height(200).build();
        XYSeries series_s = chart.addSeries("sell",kbband_sell);
        series_s.setLineWidth(linewidth);
        XYSeries series_b = chart.addSeries("buy",kbband_buy);
        series_b.setLineWidth(linewidth);

		XYSeries series_t = chart.addSeries("target",targetlist);
		series_t.setLineWidth(linewidth);
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

	public List<String> getStockListCustom(String filename) {
		List<String> stock_list = new ArrayList<>();

		MyExcel myexcel = new MyExcel();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = myexcel.readStockinfoCustom(filename, false);
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

		int hour = 3;
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
	}


	public void downloadStockInfoCustom(String filename) {
		MyExcel myexcel = new MyExcel();
		MyWeb myweb = new MyWeb();
		StockDic stockdic = new StockDic();
		List<String> stock_list = new ArrayList<>();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = myexcel.readStockinfoCustom(filename,false);
		int size = web_stockinfo.size();
		for(int i =0;i<size;i++) {
			stock_list.add(web_stockinfo.get(i).stock_code);
		}
		web_stockinfo.clear();
		size = stock_list.size();
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
		myexcel.writestockinfoCustom(filename,web_stockinfo);
	}

	public void downloadNowPrice(List<String> stock_list) {
		MyExcel myexcel = new MyExcel();
		MyWeb myweb = new MyWeb();
		int hour = 3;
		int size = stock_list.size();
		for(int i =0;i<size;i++) {
			String stock_code = stock_list.get(i);
			myweb.getNaverpriceByToday(stock_code, 6 * hour); // 1시간을 읽어서 저장한다
		}
	}

	public void downloadYFrice(List<String> stock_list) {
		int size = stock_list.size();
		for(int i =0;i<size;i++) {
			new YFDownload(stock_list.get(i));
		}
	}

	public List<FormatStockInfo> getStockInfo() {
		List<String> stock_list = new ArrayList<>();

		MyExcel myexcel = new MyExcel();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = myexcel.readStockinfo(0, false);
		return web_stockinfo;
	}

	public List<FormatStockInfo> getStockInfoCustom(String filename) {
		List<String> stock_list = new ArrayList<>();

		MyExcel myexcel = new MyExcel();
		List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
		web_stockinfo = myexcel.readStockinfoCustom(filename, false);
		return web_stockinfo;
	}
	/*
	public static void main(String[] args) {
		new JListCustomRenderer();
	}
	*/
}
