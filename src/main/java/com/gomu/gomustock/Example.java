package main.java.com.gomu.gomustock;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.Balance;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Example {


    public void getStockInfo() {
        // 네이버에서 종목 정보를 읽어서 프린트한다.
        MyWeb myweb = new MyWeb();
        FormatStockInfo stockinfo = new FormatStockInfo();
        stockinfo = myweb.getNaverStockinfo("005930");
        System.out.println(stockinfo.toString());
    }

    public static void BetaNKospi200() throws UnsupportedEncodingException {
        // 선별된 베타 리스트에서 코스피200 종목이 있으면 프린트 한다.
        MyExcel myexcel = new MyExcel();
        List<String> beta = new ArrayList<>();
        List<String> kospi200 = new ArrayList<>();
        beta = myexcel.readColumn("BETA.xls",1);
        kospi200 = myexcel.readColumn("BETA.xls",3);
        beta.remove(0);
        kospi200.remove(0);
        int kospisize = kospi200.size();
        int betasize = beta.size();
        for(int i =0;i<kospisize;i++) {
            for(int j=0;j<betasize;j++) {
                if(kospi200.get(i).equals(beta.get(j))) {
                    String result = URLDecoder.decode(kospi200.get(i),"CP949");
                    System.out.println(result + " " + i);
                }
            }
        }
    }
    public void balance_singlechart_test() {
        Color[] colors = {Color.RED, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};
        new YFDownload("316140");

        PriceBox kbbank = new PriceBox("316140");
        List<Float> kbband_close = kbbank.getClose();
        BBandTest bbtest = new BBandTest("316140",kbband_close);

        // Create Chart & add first data
        int size = kbband_close.size();
        List<Float> x = new ArrayList<>();
        for(int i =0;i<size;i++) { x.add((float)i); }
        XYChart chart  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, kbband_close);
        chart.addSeries("upper_line",bbtest.getUpperLine());
        chart.addSeries("middle_line",bbtest.getMiddleLine());
        chart.addSeries("low_line",bbtest.getLowLine());
        List<Float> buyscore = bbtest.chartdata_buyscore();
        chart.addSeries("buysignal",buyscore);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setSeriesColors(colors);

        new SwingWrapper(chart).displayChart();

        Balance kbbank_balance = new Balance("316140",0);
        float profitrate = kbbank_balance.getProfitRate();
        System.out.println("profit rate : " + profitrate);
        float profit = kbbank_balance.getProfit();
        System.out.println("profit : " + profit);
        float averprice = kbbank_balance.getAVERPrice();
        System.out.println("average buy : " + averprice);
        float total_buy_price = kbbank_balance.getTotalBuyCost();
        System.out.println("total buy : " + total_buy_price);
    }



    public void multi_chart() {

        Color[] colors = {Color.RED, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};

        new YFDownload("316140");
        new YFDownload("105560");
        new YFDownload("005930");
        new YFDownload("^KS11");
        new YFDownload("^GSPC");
        new YFDownload("^IXIC");
        new YFDownload("^DJI");

        PriceBox kbbank = new PriceBox("316140");
        List<Float> kbband_close = kbbank.getClose();
        BBandTest bbtest = new BBandTest("316140",kbband_close);

        // Create Chart & add first data
        int size = kbband_close.size();
        List<Float> x = new ArrayList<>();
        for(int i =0;i<size;i++) { x.add((float)i); }
        XYChart chart  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, kbband_close);
        chart.addSeries("upper_line",bbtest.getUpperLine());
        chart.addSeries("middle_line",bbtest.getMiddleLine());
        chart.addSeries("low_line",bbtest.getLowLine());
        List<Float> buyscore = bbtest.chartdata_buyscore();
        chart.addSeries("buysignal",buyscore);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setSeriesColors(colors);

        PriceBox yfkospi = new PriceBox("^KS11");
        List<Float> yfkospi_close = yfkospi.getClose();
        BBandTest bbtest1 = new BBandTest("^KS11",yfkospi_close);

        size = yfkospi_close.size();
        x = new ArrayList<>();
        for(int i =0;i<size;i++) { x.add((float)i); }
        XYChart chart2  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, yfkospi_close);
        chart2.addSeries("upper_line1",bbtest1.getUpperLine());
        chart2.addSeries("middle_line1",bbtest1.getMiddleLine());
        chart2.addSeries("low_line1",bbtest1.getLowLine());
        List<Float> buyscore1 = bbtest1.chartdata_buyscore();
        chart2.addSeries("buysignal1",buyscore1);
        chart2.getStyler().setMarkerSize(0);
        chart2.getStyler().setSeriesColors(colors);

        PriceBox sp500 = new PriceBox("^GSPC");
        List<Float> sp500_close = sp500.getClose();
        BBandTest bbtest2 = new BBandTest("^GSPC",sp500_close);

        size = sp500_close.size();
        x = new ArrayList<>();
        for(int i =0;i<size;i++) { x.add((float)i); }
        XYChart chart3  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, sp500_close);
        chart3.addSeries("upper_line2",bbtest2.getUpperLine());
        chart3.addSeries("middle_line2",bbtest2.getMiddleLine());
        chart3.addSeries("low_line2",bbtest2.getLowLine());
        List<Float> buyscore2 = bbtest2.chartdata_buyscore();
        chart3.addSeries("buysignal2",buyscore2);
        chart3.getStyler().setMarkerSize(0);
        chart3.getStyler().setSeriesColors(colors);

        PriceBox nasdaq = new PriceBox("^IXIC");
        List<Float> nasdaq_close = nasdaq.getClose();
        BBandTest bbtest3 = new BBandTest("^IXIC",nasdaq_close);

        XYChart chart4  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, nasdaq_close);
        chart4.addSeries("upper_line3",bbtest3.getUpperLine());
        chart4.addSeries("middle_line3",bbtest3.getMiddleLine());
        chart4.addSeries("low_line3",bbtest3.getLowLine());
        List<Float> buyscore3 = bbtest3.chartdata_buyscore();
        chart4.addSeries("buysignal3",buyscore3);
        chart4.getStyler().setMarkerSize(0);
        chart4.getStyler().setSeriesColors(colors);

        List<XYChart> charts = new ArrayList<XYChart>();
        charts.add(chart);
        charts.add(chart2);
        charts.add(chart3);
        charts.add(chart4);

        new SwingWrapper<XYChart>(charts).displayChartMatrix();
    }


    public void xchart_jpanel() {

        Color[] colors = {Color.RED, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};
        new YFDownload("316140");

        PriceBox kbbank = new PriceBox("316140");
        List<Float> kbband_close = kbbank.getClose();
        BBandTest bbtest = new BBandTest("316140",kbband_close);

        // Create Chart & add first data
        int size = kbband_close.size();
        List<Float> x = new ArrayList<>();
        for(int i =0;i<size;i++) { x.add((float)i); }
        XYChart chart  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, kbband_close);
        chart.addSeries("upper_line",bbtest.getUpperLine());
        chart.addSeries("middle_line",bbtest.getMiddleLine());
        chart.addSeries("low_line",bbtest.getLowLine());
        List<Float> buyscore = bbtest.chartdata_buyscore();
        chart.addSeries("buysignal",buyscore);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setSeriesColors(colors);

       // new SwingWrapper(chart).displayChart();;

        // Create and set up the window.
        JFrame frame = new JFrame("XChart Swing Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Add content to the window.
        //JPanel chartPanel = new XChartPanel(new AreaChart01().getChart());
        JPanel chartPanel = new XChartPanel(chart);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

    }
}
