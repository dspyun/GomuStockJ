package main.java.com.gomu.gomustock.jlist;

import com.intellij.ui.components.JBScrollPane;
import main.java.com.gomu.gomustock.MyExcel;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.network.YFDownload;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class StockSector {

    String stock_code, stock_name,stock_type;

    private JPanel toppanel = new JPanel();
    private JPanel panelChart;
    private List<JPanel> lbChartlist = new ArrayList<>();
    List<String> codelist = new ArrayList<>();
    List<String> namelist = new ArrayList<>();
    public StockSector() {
        MyExcel myexcel = new MyExcel();

        /*
        HashMap<String,String> sectormap = myexcel.readSectorinfo( false);
        // 차례는 지켜지지 않는다. name과 key 싱크는 된다
        // 따라서 정렬을 다시해야 한다. 이렇게 하는 것이 번거로워서 그냥 파일을 두번 읽음
        Set<Map.Entry<String, String>> entrySet = sectormap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            codelist.add(entry.getKey());
            namelist.add(entry.getValue());
        }
        */
        codelist = myexcel.readColumn( "group_sector.xls",0);
        codelist.remove(0); // 헤더는 제거해준다.
        namelist = myexcel.readColumn( "group_sector.xls",1);
        namelist.remove(0); // 헤더는 제거해준다

    }

    public List<String> getSectorCodelist() {
        return codelist;
    }

    public JPanel getPanel() {
        return toppanel;
    }

    public void render() {
        setPaneldata();
        addPanel();
    }

    public void setPaneldata() {
        //renderer_setText(Integer.toString(index));
        //String icon_path = STOCKDIR+book.getIconName() + ".jpg";

        int height = 200;
        int width = 300;
        StockChart schart = new StockChart();

        // 6개월 차트 : set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
        int size = codelist.size();
        for(int i =0;i<15;i++) {
            JPanel onepanel = new JPanel();
            JButton onebutton = new JButton(namelist.get(i));
            onebutton.setPreferredSize(new Dimension(300,20));
            onepanel.add(onebutton);
            onepanel.add(new XChartPanel(schart.GetPeriodChart(codelist.get(i),240)));
            onepanel.setPreferredSize(new Dimension(width,height));
            onepanel.setOpaque(true);
            onepanel.setBackground(getBackground());
            onepanel.setForeground(getBackground());
            lbChartlist.add(onepanel);
        }
    }

    Color getBackground() {
        return Color.lightGray;
    }

    void addPanel() {
        int height1 = 200+30;
        int width1 = 1800;

        panelChart = new JPanel(new GridLayout(3, 5));
        panelChart.setPreferredSize(new Dimension(width1,height1));
        //panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
        int size = lbChartlist.size();
        for(int i =0;i<15;i++) {
            panelChart.add(lbChartlist.get(i));
        }

        toppanel.removeAll();
        toppanel.setLayout(new GridLayout(1, 1));
        toppanel.add(panelChart);
    }
}
