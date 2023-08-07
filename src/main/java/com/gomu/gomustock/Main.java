package main.java.com.gomu.gomustock;

import com.intellij.ui.components.JBScrollPane;
import jxl.biff.drawing.ComboBox;
import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.*;
import main.java.com.gomu.gomustock.network.Ebest;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.network.YFDownload;
import main.java.com.gomu.gomustock.stockengin.CPUID;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Main extends JFrame{

    public static void main(String[] args) throws IOException {
       Ebest ebest = new Ebest();
       ebest.testmain();
       // mytest();
    }

    public static void mytest() throws IOException{
        MyWeb myweb = new MyWeb();
        MyExcel myexcel = new MyExcel();
        InfoDownload idown = new InfoDownload();
        InfoRead iread = new InfoRead();
        StockBookRenderer renderer = new StockBookRenderer();
        JPanel listpanel = new JPanel(new BorderLayout());

        // hdd id를 읽고 등록되어 있으면 실행한다
        CPUID cpuid = new CPUID("c");
        if(cpuid.checkID()==false) {
            System.out.println("permission invalid");
            return;
        }
        // 팝업창 셋업
        JFrame popupfrm = new JFrame("팝업창 및 알림창 호출");
        setpopup(popupfrm);

        // main frame을 center에 setup
        Dimension dim = new Dimension(1700,800);
        JFrame frame = new JFrame("XChart Swing Demo");
        frame.setPreferredSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // debug 버튼은 좌측헤 setup
        JButton DebugButton = new JButton();
        DebugButton.setText("모니터버튼");
        DebugButton.setPreferredSize( new Dimension( 100, 600 ) );
        frame.add(DebugButton,BorderLayout.WEST);

        // 컨트롤 버튼과 텍스트필드는 윗쪽에 셋업
        JPanel HeaderPanel = new JPanel();
        HeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //HeaderPanel.setBounds(20,20,1600,50);
        frame.add(HeaderPanel,BorderLayout.NORTH);

        JButton button9 = new JButton("이름>코드");
        button9.setPreferredSize( new Dimension( 95, 25 ) );
        button9.setMargin(new Insets(1,1,1,1));
        HeaderPanel.add(button9);

        String[] stcokgroup = {"group_manual", "group_hold",  "group_newstock", "group_newetf", "group_sector"};
        JComboBox namecombo = new JComboBox(stcokgroup);
        HeaderPanel.add(namecombo);

        JTextField textfield = new JTextField();
        HeaderPanel.add(textfield);
        textfield.setText("group_manual");
        textfield.setPreferredSize( new Dimension( 150, 27 ) );

        JButton button4 = new JButton("퀵리뷰");
        HeaderPanel.add(button4);
        JButton button1 = new JButton("오늘차트");
        HeaderPanel.add(button1);
        JButton button3 = new JButton("Full다운");
        HeaderPanel.add(button3);
        String[] sectors = {"sector240", "sector120",  "sector60", "sector30"};
        JComboBox sectorcombo = new JComboBox(sectors);
        HeaderPanel.add(sectorcombo);
        JTextField textfield2 = new JTextField();
        HeaderPanel.add(textfield2);
        textfield2.setText("stock_code");
        textfield2.setPreferredSize( new Dimension( 100, 27 ) );
        JButton button8 = new JButton("종목상세");
        HeaderPanel.add(button8);

        // 신규종목 발굴
        JButton button5 = new JButton("신규종목");
        HeaderPanel.add(button5);
        JButton button6 = new JButton("신규ETF");
        HeaderPanel.add(button6);
        JButton button7 = new JButton("업종선택");
        HeaderPanel.add(button7);

        namecombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb=(JComboBox<String>)e.getSource();
                int index=cb.getSelectedIndex();
                textfield.setText(stcokgroup[index]);
                textfield.paint(textfield.getGraphics());
            }
        });


        iread.setCallback(new InfoRead.IFCallback() {
            @Override
            public void callback(String str) {
                DebugButton.setText(htmltext(str));
                DebugButton.paint(DebugButton.getGraphics());;
            }
        });

        idown.setCallback(new InfoDownload.IFCallback() {
            @Override
            public void callback(String str) {
                DebugButton.setText(htmltext(str));
                DebugButton.paint(DebugButton.getGraphics());;
            }
        });

        renderer.setCallback(new StockBookRenderer.IFCallback() {
            @Override
            public void callback(int target, String str) {
                if(target==0) {
                    DebugButton.setText(htmltext(str));
                    DebugButton.paint(DebugButton.getGraphics());

                } else {
                    textfield2.setText(str);
                    textfield2.paint(textfield2.getGraphics());
                }
            }
        });

        UpjongDialog multi_sector_dlg = new UpjongDialog();

        multi_sector_dlg.setCallback(new UpjongDialog.IFCallback() {
            @Override
            public void callback(String code, String name) {
                textfield.setText("group_"+name);
                textfield.paint(textfield.getGraphics());
                myweb.getNaverUpjong(code, name, 50);
                JOptionPane.showConfirmDialog(popupfrm, "full down을 누르세요. ","TITTLE", JOptionPane.YES_OPTION);
            }
        });

        myweb.setCallback(new MyWeb.IFCallback() {
            @Override
            public void callback(String str) {
                DebugButton.setText(htmltext(str));
                DebugButton.paint(DebugButton.getGraphics());
            }
        });


        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                //System.out.println("First index: " + listSelectionEvent.getFirstIndex());
                //System.out.println(", Last index: " + listSelectionEvent.getLastIndex());
                boolean adjust = listSelectionEvent.getValueIsAdjusting();
                //System.out.println(", Adjusting? " + adjust);
                if (!adjust) {
                    JList list = (JList) listSelectionEvent.getSource();
                    int selections[] = list.getSelectedIndices();
                    Object selectionValues[] = list.getSelectedValues();
                    for (int i = 0, n = selections.length; i < n; i++) {
                        if (i == 0) {
                            System.out.println(" Selections: ");
                        }
                        System.out.println(selections[i] + "/" + selectionValues[i] + " ");
                        List<String> stocklist = myexcel.readColumn(textfield.getText()+".xls",0);
                        int index = selections[i];
                        textfield2.setText(stocklist.get(index+1));
                        textfield2.paint(textfield2.getGraphics());
                    }
                }
            }
        };

        sectorcombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (sectorcombo.equals(ae.getSource())) {
                    JComboBox<String> cb = (JComboBox<String>) ae.getSource();
                    int index = cb.getSelectedIndex();
                    int period;
                    if (index == 0) period = 240;
                    else if (index == 1) period = 120;
                    else if (index == 2) period = 60;
                    else {
                        period = 30;
                    }
                    StockSector sector = new StockSector(period);
                    JPanel panel = sector.simpleChart();

                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(panel), BorderLayout.CENTER);

                    frame.add(listpanel, BorderLayout.CENTER);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        });


        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // 오늘 가격 다운로드 + 정보읽어 보여주기
                if(button1.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();
                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
                    int size = stock_list.size();
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }

                    // 하루 가격 다운로드
                    idown.downloadNowPrice(stock_list, 6);

                    // 다운로드 완료하고 지금부터는 파일데이터를 리스트에 로딩한다
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    model = renderer.loadInfoChart2List(web_stockinfo);
                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    //ListSelectionListener listSelectionListener = getListListner();
                    listBook.addListSelectionListener(listSelectionListener);

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
                    frame.add(listpanel,BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                }

                // 입력된 파일을 Full로 다운로드하고 읽고 보여주기
                if(button3.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = myexcel.readColumn(filename+".xls",0);
                    stock_list.remove(0);
                    int size = stock_list.size();
                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }

                    // 통계재무정보 + 하루가겪 + 년간가격 다운로드
                    idown.downloadTotalInformation(filename, stock_list);

                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    // 다운로드 완료하고 지금부터는 파일데이터를 리스트에 로딩한다
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    model = renderer.loadInfoChart2List(web_stockinfo);
                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고
                    listBook.addListSelectionListener(listSelectionListener);

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
                    frame.add(listpanel,BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                }

                // 퀵리뷰 information만 읽고 보여주기
                if(button4.equals(ae.getSource())){

                    String filename = textfield.getText();
                    DefaultListModel<StockBook> model = new DefaultListModel<>();

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);

                    if(stock_list.size()==0) {
                        frame.pack();
                        frame.setVisible(true);
                        return;
                    }

                    List<FormatStockInfo> web_stockinfo = new ArrayList<FormatStockInfo>();
                    // 다운로드 완료하고 지금부터는 파일데이터를 리스트에 로딩한다
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    model = renderer.loadInfoChart2List(web_stockinfo);
                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고
                    listBook.addListSelectionListener(listSelectionListener);

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
                    frame.add(listpanel,BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                }

                if(button9.equals(ae.getSource())){

                    String filename = textfield.getText();
                    idown.trans(filename);
                    textfield.setText(filename);
                }

                if(button5.equals(ae.getSource())){
                    String filename = "group_newstock";
                    idown.pickupNewStock(filename);
                    textfield.setText(filename);
                }

                if(button6.equals(ae.getSource())){
                    String filename = "group_newetf";
                    idown.pickupNewEtf(filename);
                    textfield.setText(filename);
                }

                if(button7.equals(ae.getSource())){
                    multi_sector_dlg.setSize(1200, 600);
                    multi_sector_dlg.setLocation(200,100);
                    multi_sector_dlg.setVisible(true);

                }

                // 개별종목 상세내용 보여주기
                if(button8.equals(ae.getSource())){
                    JPanel temppanel = new JPanel(new BorderLayout());
                    // 개별종목 UX 구현
                    // bbchart, daily chart, 메모, sector chart,
                    String filename = textfield.getText();
                    String stock_code = textfield2.getText();
                    StockOnePage onepage = new StockOnePage(filename, stock_code);

                    JList<JPanel> renderlist = new JList<JPanel>();
                    renderlist = onepage.getRenderList();
                    temppanel.removeAll();
                    temppanel.setPreferredSize(dim);
                    temppanel.add(new JScrollPane(renderlist), BorderLayout.CENTER);

                    //temppanel.paint(temppanel.getGraphics());
                    frame.add(temppanel,BorderLayout.CENTER);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        };

        button1.addActionListener(listener);
        button3.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);
        button8.addActionListener(listener);
        button9.addActionListener(listener);


        frame.pack();
        frame.setVisible(true);
    }


    public static String htmltext(String input_str) {
        String result;
        result="<HTML>";
        result+=input_str.replaceAll("\n","<br>");
        result+="</HTML>";
        return result;
    }


    public static void setpopup(JFrame frm) {

        //TODO 부모 프레임 크기 설정 (가로, 세로)
        frm.setSize(500, 500);
        //TODO 부모 프레임을 화면 가운데에 배치
        frm.setLocationRelativeTo(null);
        //TODO 부모 프레임을 닫았을 때 메모리에서 제거되도록 설정
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //TODO 부모 프레임 창 크기 고정 실시
        frm.setResizable(false);
        //TODO 부모 레이아웃 설정
        frm.getContentPane().setLayout(null);
    }

}
