package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class JListCustomRenderer extends JFrame {
	private int width = 400;
	private int height = 200;
	private JList<Book> listBook;


	public JListCustomRenderer() {

		add(createMainPanel());

		// set display
		setTitle("JLIstCustomRenderer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		// create list book and set to scrollpane and add to panel
		panel.add(new JScrollPane(listBook = createListBooks()),
				BorderLayout.CENTER);

		return panel;
	}

	private JList<Book> createListBooks() {
		// create List model
		DefaultListModel<Book> model = new DefaultListModel<>();
		XYChart mychart = getChart();
		// add item to model
		model.addElement(new Book("C/C++ Programming", "A", "cpp", mychart));
		model.addElement(new Book("Java Programming", "B", "java",mychart));
		model.addElement(new Book("C# Programming", "C", "cs",mychart));
		model.addElement(new Book("IOS Programming", "D", "ios",mychart));
		model.addElement(new Book("Windows Phone Programming", "E", "wp",mychart));
		model.addElement(new Book("Android Programming", "F", "android",mychart));
		// create JList with model
		JList<Book> list = new JList<Book>(model);
		// set cell renderer
		list.setCellRenderer(new BookRenderer());
		return list;
	}

	private XYChart getChart() {

		Color[] colors = {Color.RED, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};
		new YFDownload("316140");

		PriceBox kbbank = new PriceBox("316140");
		List<Float> kbband_close = kbbank.getClose();
		BBandTest bbtest = new BBandTest("316140",kbband_close);

		// Create Chart & add first data
		int size = kbband_close.size();
		List<Float> x = new ArrayList<>();
		for(int i =0;i<size;i++) { x.add((float)i); }
		XYChart chart  = new XYChartBuilder().width(200).height(200).build();
				//QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, kbband_close);
		chart.addSeries("upper_line",bbtest.getUpperLine());
		chart.addSeries("middle_line",bbtest.getMiddleLine());
		chart.addSeries("low_line",bbtest.getLowLine());
		List<Float> buyscore = bbtest.chartdata_buyscore();
		chart.addSeries("buysignal",buyscore);
		chart.getStyler().setMarkerSize(0);
		chart.getStyler().setSeriesColors(colors);
		chart.getStyler().setLegendVisible(false);
		return chart;
	}

	/*
	public static void main(String[] args) {
		new JListCustomRenderer();
	}
	*/
}
