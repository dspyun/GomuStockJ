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

    public static void getNaverpriceByday(String stock_code, int day) {
        List<FormatOHLCV> naverpricelist = new ArrayList<>();
        String page="";
        int index = day/10;
        for(int i =1;i<=index;i++) {
            page = Integer.toString(i);
            naverpricelist.addAll(getPrice10(stock_code,page));
        }
        FormatOHLCV naverheader = new FormatOHLCV();
        naverheader.date = "date";
        naverheader.close = "close";
        naverheader.open = "open";
        naverheader.high = "high";
        naverheader.low = "low";
        naverheader.volume = "volume";
        naverpricelist.add(0,naverheader);
        MyExcel myexcel = new MyExcel();
        myexcel.writeprice(stock_code,naverpricelist);
    }


    public static List<FormatOHLCV> getPrice10(String stock_code, String pageno) {

        List<String> agent = new ArrayList<>();
        List<String> fogn = new ArrayList<>();
        List<FormatOHLCV> naverpricelist = new ArrayList<>();

        try {
            String pagenumber;
            if(pageno.equals("0")) pagenumber="";
            else pagenumber = "&page="+pageno;
            String URL = "https://finance.naver.com/item/sise_day.naver?code="+stock_code+pagenumber;
            Document doc;
            doc = Jsoup.connect(URL).get();
            Elements trlist = doc.select("tr");
            for(int i =2;i<7;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.","");
                naverprice.close = tdlist.get(1).text().replaceAll(",", "");
                naverprice.open = tdlist.get(3).text().replaceAll(",", "");
                naverprice.high = tdlist.get(4).text().replaceAll(",", "");
                naverprice.low = tdlist.get(5).text().replaceAll(",", "");
                naverprice.volume = tdlist.get(6).text().replaceAll(",", "");
                naverpricelist.add(naverprice);
            }
            for(int i =10;i<15;i++ ){
                FormatOHLCV naverprice = new FormatOHLCV();
                Elements tdlist = trlist.get(i).select("td");
                naverprice.date = tdlist.get(0).text().replaceAll("\\.","");
                naverprice.close = tdlist.get(1).text().replaceAll(",", "");
                naverprice.open = tdlist.get(3).text().replaceAll(",", "");
                naverprice.high = tdlist.get(4).text().replaceAll(",", "");
                naverprice.low = tdlist.get(5).text().replaceAll(",", "");
                naverprice.volume = tdlist.get(6).text().replaceAll(",", "");
                naverpricelist.add(naverprice);
            }

            int j = 0;
            ;/*
            System.out.println("per = " + result.per+"\n");
            System.out.println("per12 = " + result.per12+"\n");
            System.out.println("area_per = " + result.area_per+"\n");
            System.out.println("pbr = " + result.pbr+"\n");
            System.out.println("div_rate = " +result.div_rate+"\n");
            System.out.println("fogn_rate = " + result.fogn_rate+"\n");
            System.out.println("beta = " + result.beta+"\n");
            System.out.println("op_profit = " + result.op_profit+"\n");
*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        };
        return naverpricelist;
    }

    private static double[] getRandomWalk(int numPoints) {

        double[] y = new double[numPoints];
        y[0] = 0;
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }
        return y;
    }

    public static int getTagColumn(String tag) {
        // default 값은  종가는읽게 6으로 리턴
        int column=0;
        if(tag.equals("DATE")) column = 0;
        else if(tag.equals("OPEN")) column = 1;
        else if(tag.equals("HIGH")) column = 2;
        else if(tag.equals("LOW")) column = 3;
        else if(tag.equals("CLOSE")) column = 4;
        else if(tag.equals("VOLUME")) column = 5;
        else column = 4;

        return column;
    }

    public static List<String> readhistory(String stock_code, int days) {
        String tag = "CLOSE";
        String extention = ".xls";
        String PathFile = "D:\\gomustockj\\"+stock_code+extention;
        //int days = 60;
        boolean header = false;
        // data 저장순서는 현재>과거순으이다, 60일치를 읽으려면 0부터 60개를 읽으면 된다
        int column = getTagColumn(tag);
        InputStream is=null;
        Workbook wb=null;
        int maxcol;
        List<String> pricebuffer = new ArrayList<String>();

        try {
            is =  new FileInputStream(PathFile);
            wb = Workbook.getWorkbook(is);
            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    // line1, col1에서 contents를 읽는다.
                    int start = 0;
                    if(header != TRUE) start = 1;
                    // -1 : data를 max로 읽으려면 헤더때문에 -1을 해줘야 한다
                    maxcol = sheet.getColumn(column).length-1;
                    // days가 maxcol을 넘어가거나 -1보다 작으면 maxcol로 읽는다
                    if(days >= maxcol || days <= -1) days = maxcol;
                    for(int i=start;i<days+start;i++) {
                        // formatOA class의 구조로 저장된다
                        // 종가는 6번째 컬럼의 값
                        pricebuffer.add(sheet.getCell(column, i).getContents());
                    }
                }
            }
            wb.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //wb.close();
            //is.close();
        }
        /*
        for(int j=0;j<pricebuffer.size();j++)
            System.out.println(pricebuffer.get(j));

         */
        return pricebuffer;
    }
}
