package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.MyStat;
import main.java.com.gomu.gomustock.network.PriceBox;
import main.java.com.gomu.gomustock.stockengin.BBandTest;
import main.java.com.gomu.gomustock.stockengin.IchimokuTest;
import main.java.com.gomu.gomustock.stockengin.RSITest;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockChart {


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

    public XYChart GetTodayChart(String stock_code, float input_target) {

        Color[] colors = {Color.RED, Color.BLUE,Color.BLACK, Color.GRAY, Color.LIGHT_GRAY,Color.BLUE};

        int hour = 60*4;
        MyExcel myexcel = new MyExcel();
        java.util.List<String> dealprice = myexcel.readtodayprice(stock_code+"today","DEAL",-1,false);
        java.util.List<String> sellprice = myexcel.readtodayprice(stock_code+"today","SELL",-1,false);
        java.util.List<String> buyprice = myexcel.readtodayprice(stock_code+"today","BUY",-1,false);
        java.util.List<String> volume = myexcel.readtodayprice(stock_code+"today","VOLUME",-1,false);
        java.util.List<Float> kbband_deal = myexcel.string2float_fillpre(dealprice,1);
        java.util.List<Float> kbband_sell = myexcel.string2float_fillpre(sellprice,1);
        java.util.List<Float> kbband_buy = myexcel.string2float_fillpre(buyprice,1);
        java.util.List<Float> kbband_vol = myexcel.string2float_fillpre(volume,1);
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
        float low_price = Collections.min(kbband_buy);
        MyStat mystat = new MyStat();
        List<Float> vol2 = mystat.scaling_float2(kbband_vol,low_price);
        XYSeries series_v = chart.addSeries("vol",vol2);
        series_v.setLineWidth(linewidth);

        XYSeries series_t = chart.addSeries("target",targetlist);
        series_t.setLineWidth(linewidth);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setSeriesColors(colors);
        chart.getStyler().setLegendVisible(false);
        //chart.getStyler().setYAxisTicksVisible(true);
        return chart;
    }


    public XYChart GetPeriodChart(String stock_code) {

        if(stock_code.equals("278240")) {
            int test = 1;
        }
        int test_period = 120;
        Color[] colors = {Color.RED, Color.GRAY, Color.GRAY, Color.BLUE,Color.GREEN,Color.ORANGE,Color.BLUE};
        MyStat mystat = new MyStat();
        PriceBox kbbank = new PriceBox(stock_code);
        List<Float> kbband_close = kbbank.getClose(test_period);
        if(kbband_close.get(0)==0 || kbband_close.size() < test_period) {
            XYChart chart  = new XYChartBuilder().width(300).height(200).build();
            return chart;
        }
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
}
