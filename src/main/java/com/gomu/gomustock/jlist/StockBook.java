package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import org.knowm.xchart.XYChart;

public class StockBook {
    private String company_info;
    private String fninfo;
    private String stock_naverinfo;
    private String stock_code;
    private XYChart chart;
    private XYChart todaychart;
    private String news;
    private String stock_type;
    private String etfinfo;
    private String etfcompanies;
    private FormatStockInfo stock_info;

    public StockBook(FormatStockInfo stockinfo, XYChart chart, XYChart todaychart) {
        super();
        this.stock_info = stockinfo;
        this.chart = chart;
        this.stock_code = stock_info.stock_code;
        this.news = stock_info.news;
        this.company_info = stock_info.desc;
        this.stock_naverinfo = stock_info.toString();
        this.fninfo = stock_info.fninfo;
        this.todaychart = todaychart;
        this.stock_type= stock_info.stock_type;
        this.etfinfo = stock_info.etfinfo;
        this.etfcompanies = stock_info.etfcompanies;
    }

    public String getNews() {
        return news;
    }
    public XYChart getChart() { return chart;}
    public XYChart getTodayChart() { return todaychart;}
    public String getStockInfo() {
        return stock_naverinfo;
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
    public String getStocktype() {
        return stock_type;
    }
    public String getETFInfo() {
        return etfinfo;
    }
    public String getETFCompanies() {
        return etfcompanies;
    }
}
