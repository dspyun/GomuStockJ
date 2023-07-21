package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import org.knowm.xchart.XYChart;

import javax.swing.*;

public class Book {
	private String company_info;
	private String fninfo;
	private String stock_indication;
	private String stock_code;
	private XYChart chart;
	private XYChart todaychart;
	private String news;
	private FormatStockInfo stock_info;

	public Book(FormatStockInfo stockinfo, XYChart chart, XYChart todaychart) {
		super();
		this.stock_info = stockinfo;
		this.chart = chart;
		this.stock_code = stock_info.stock_code;
		this.news = stock_info.news;
		this.company_info = stock_info.desc;
		this.stock_indication = stock_info.toString();
		this.fninfo = stock_info.fninfo;
		this.todaychart = todaychart;
	}

	public String getNews() {
		return news;
	}
	public XYChart getChart() { return chart;}
	public XYChart getTodayChart() { return todaychart;}
	public String getIndication() {
		return stock_indication;
	}
	public String getCompanyInfo() {
		return company_info;
	}
	public String getfninfo() {
		return fninfo;
	}
	public String getStockcode() {
		return stock_code;
	}

}
