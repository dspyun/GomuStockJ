package main.java.com.gomu.gomustock;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import main.java.com.gomu.gomustock.format.FormatOHLCV;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.StockBox;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.TAlib;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class Main {

    static Frame frame = new Frame("프레임 이름");
    static Button button = new Button("버튼 이름");

    public static void createFrame()
    {
        frame.add(button);
        frame.setSize(200, 100);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        //프레임 열기
        //createFrame();
        String stock_code = "055550";
        Color[] colors = {Color.RED, Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};
        MyStat mystat = new MyStat();
        MyWeb myweb = new MyWeb();

// save to file
        new YFDownload("055550");
        new YFDownload("105560");
        new YFDownload("005930");
        new YFDownload("^KS11");
        new YFDownload("^GSPC");
        new YFDownload("^IXIC");
        new YFDownload("^DJI");

        StockBox kbbank = new StockBox("105560");
        List<Float> kbband_close = kbbank.getClose();
        // bband 차트를 그릴 데이터를 생성한다.
        TAlib talib = new TAlib();
        List<List<Float>> result = talib.bbands(kbband_close,240);
        BBandTest bbtest = new BBandTest("105560",kbband_close);
        List<Float> loopbacktest = bbtest.loopbacktest_forchart(240);
        //List<Float> normalresult = bbtest.normaltest_forchart(240);

        int size = kbband_close.size();
        List<Float> x = new ArrayList<>();

        for(int i =0;i<size;i++) {
            x.add((float)i);
        }
        // Create Chart
        XYChart chart  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, kbband_close);
        chart.addSeries("upper",result.get(0));
        chart.addSeries("middle",result.get(1));
        chart.addSeries("low",result.get(2));
        chart.addSeries("buysignal",loopbacktest);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setSeriesColors(colors);


        StockBox yfkospi = new StockBox("^KS11");
        List<Float> yfkospi_close = yfkospi.getClose();
        List<List<Float>> result1 = talib.bbands(yfkospi_close,240);
        BBandTest bbtest1 = new BBandTest("^KS11",yfkospi_close);
        List<Float> loopbacktest1 = bbtest1.loopbacktest_forchart(240);

        XYChart chart2  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, yfkospi_close);
        chart2.addSeries("upper",result1.get(0));
        chart2.addSeries("middle",result1.get(1));
        chart2.addSeries("low",result1.get(2));
        chart2.addSeries("buysignal",loopbacktest1);
        chart2.getStyler().setMarkerSize(0);
        chart2.getStyler().setSeriesColors(colors);

        StockBox sp500 = new StockBox("^GSPC");
        List<Float> sp500_close = sp500.getClose();
        List<List<Float>> result2 = talib.bbands(sp500_close,240);
        BBandTest bbtest2 = new BBandTest("^GSPC",sp500_close);
        List<Float> loopbacktest2 = bbtest2.loopbacktest_forchart(240);

        XYChart chart3  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, sp500_close);
        chart3.addSeries("upper",result2.get(0));
        chart3.addSeries("middle",result2.get(1));
        chart3.addSeries("low",result2.get(2));
        chart3.addSeries("buysignal",loopbacktest2);
        chart3.getStyler().setMarkerSize(0);
        chart3.getStyler().setSeriesColors(colors);


        StockBox nasdaq = new StockBox("^IXIC");
        List<Float> nasdaq_close = nasdaq.getClose();
        List<List<Float>> result3 = talib.bbands(nasdaq_close,240);
        BBandTest bbtest3 = new BBandTest("^IXIC",nasdaq_close);
        List<Float> loopbacktest3 = bbtest3.loopbacktest_forchart(240);

        XYChart chart4  = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, nasdaq_close);
        chart4.addSeries("upper",result3.get(0));
        chart4.addSeries("middle",result3.get(1));
        chart4.addSeries("low",result3.get(2));
        chart4.addSeries("buysignal",loopbacktest3);
        chart4.getStyler().setMarkerSize(0);
        chart4.getStyler().setSeriesColors(colors);

        List<XYChart> charts = new ArrayList<XYChart>();
        charts.add(chart);
        charts.add(chart2);
        charts.add(chart3);
        charts.add(chart4);

        new SwingWrapper<XYChart>(charts).displayChartMatrix();
        // Show it
        // new SwingWrapper(chart).displayChart();
    }
}
