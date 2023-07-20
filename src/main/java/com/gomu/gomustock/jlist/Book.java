package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import org.knowm.xchart.XYChart;

import javax.swing.*;

public class Book {
	private String company_info;
	private String stock_indication;
	private String stock_code;
	private XYChart chart;
	private String news;
	private FormatStockInfo stock_info;


	public Book(FormatStockInfo stockinfo, XYChart chart) {
		super();
		this.stock_info = stockinfo;
		this.chart = chart;
		this.stock_code = stock_info.stock_code;
		this.news = stock_info.news;
		this.company_info = stock_info.desc;
		this.stock_indication = stock_info.toString();
	}

	public String getNews() {
		return news;
	}

	public XYChart getChart() { return chart;}

	public String getIndication() {
		return stock_indication;
	}

	public String getCompanyInfo() {
		return company_info;
	}

	public String getStockcode() {
		return stock_code;
	}

}
