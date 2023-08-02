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
    JList<JPanel> listChart = new JList<JPanel>(); //
    JList<JPanel> mylist;

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
        for(int i =0;i<size;i++) {
            JPanel onepanel = new JPanel();
            JButton onebutton = new JButton(namelist.get(i));
            onebutton.setPreferredSize(new Dimension(300,20));
            onepanel.add(onebutton);
            onepanel.add(new XChartPanel(schart.GetPeriodChart(codelist.get(i),240)));
            onepanel.setPreferredSize(new Dimension(300,230));
            onepanel.setOpaque(true);
            onepanel.setBackground(getBackground());
            onepanel.setForeground(getBackground());
            lbChartlist.add(onepanel);
        }

    }

    Color getBackground() {
        return Color.lightGray;
    }

    class PanelRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel renderer = (JPanel) value;
            renderer.setBackground(isSelected ? Color.lightGray : list.getBackground());
            return renderer;
        }
    }

    void addPanel() {
        int height1 = (200+30);
        int width1 = 1500;
        int row = 6;
        int col = 5;

        panelChart = new JPanel(new GridLayout(row, col));
        panelChart.setPreferredSize(new Dimension(width1,height1*row));
        int size = lbChartlist.size();
        for(int i =0;i<size;i++) {
            panelChart.add(lbChartlist.get(i));
        }

        DefaultListModel<JPanel> model = new DefaultListModel<>();
        model.addElement(panelChart);
        mylist = new JList<JPanel>(model);
        mylist.setCellRenderer(new StockSector.PanelRenderer());

        JPanel onepanel = new JPanel(new GridLayout(1, 1));
        onepanel.setPreferredSize(new Dimension(width1,height1*row));
        onepanel.add(new JScrollPane(mylist), BorderLayout.CENTER);

        toppanel.removeAll();
        toppanel.setLayout(new GridLayout(1, 1));
        toppanel.add(onepanel);
    }
}
