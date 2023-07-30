package main.java.com.gomu.gomustock;

import main.java.com.gomu.gomustock.format.FormatStockInfo;
import main.java.com.gomu.gomustock.jlist.*;
import main.java.com.gomu.gomustock.network.MyWeb;
import main.java.com.gomu.gomustock.stockengin.CPUID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main extends JFrame{


    public static void main(String[] args) throws IOException {

        /*
        List<String> codelist = myexcel.readColumn("upjong_table.xls",0);
        List<String> namelist = myexcel.readColumn("upjong_table.xls",1);
        int size = codelist.size();

        for(int i=1;i<size;i++) {
            String code = codelist.get(i);
            String name = namelist.get(i);
            myweb.checkbox_test2(code, name);
        }
        */
        //InfoDownload idown = new InfoDownload();
        //idown.pickupNewEtf();
        /*
        CPUID cpuid = new CPUID("c");
        boolean flag = cpuid.checkID();
        if(flag==true) System.out.println("permission is valid");
        else System.out.println("permission invalid");
        */
        mytest();
    }

    public static void mytest() throws IOException{
        MyWeb myweb = new MyWeb();
        MyExcel myexcel = new MyExcel();
        StockChart schart = new StockChart();
        InfoDownload idown = new InfoDownload();
        InfoRead iread = new InfoRead();
        StockBookRenderer renderer = new StockBookRenderer();
        String monitor_text="";
        int index = 0;


        CPUID cpuid = new CPUID("c");
        if(cpuid.checkID()==false) {
            System.out.println("permission invalid");
            return;
        }
        JFrame popupfrm = new JFrame("팝업창 및 알림창 호출");
        setpopup(popupfrm);


        Dimension dim = new Dimension(1700,800);
        JFrame frame = new JFrame("XChart Swing Demo");
        frame.setPreferredSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel listpanel = new JPanel(new BorderLayout());
        frame.add(listpanel,BorderLayout.CENTER);

        // header panel에는 버튼과 텍스트필드를 넣는다
        JPanel HeaderPanel = new JPanel();
        HeaderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        HeaderPanel.setBounds(20,20,1600,50);
        frame.add(HeaderPanel,BorderLayout.NORTH);

        JLabel TitleLabel = new JLabel("DLStockInfo");
        TitleLabel.setPreferredSize( new Dimension( 200, 24 ) );
        HeaderPanel.add(TitleLabel);

        JButton button2 = new JButton("보유주식");
        HeaderPanel.add(button2);
        JButton button3 = new JButton("Full다운");
        HeaderPanel.add(button3);

        JTextField textfield = new JTextField();
        HeaderPanel.add(textfield);
        textfield.setText("group_candi");
        textfield.setPreferredSize( new Dimension( 200, 24 ) );

        JButton button4 = new JButton("퀵리뷰");
        HeaderPanel.add(button4);
        JButton button1 = new JButton("오늘차트");
        HeaderPanel.add(button1);
        JButton button9 = new JButton("이름>코드");
        HeaderPanel.add(button9);
        JButton button5 = new JButton("신규주식");
        HeaderPanel.add(button5);
        JButton button6 = new JButton("신규ETF");
        HeaderPanel.add(button6);
        JButton button7 = new JButton("업종선택");
        HeaderPanel.add(button7);

        JButton DebugButton = new JButton();
        DebugButton.setText("모니터버튼");
        DebugButton.setPreferredSize( new Dimension( 100, 600 ) );
        frame.add(DebugButton,BorderLayout.WEST);

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
            public void callback(String str) {
                DebugButton.setText(htmltext(str));
                DebugButton.paint(DebugButton.getGraphics());;
            }
        });

        UpjongDialog test = new UpjongDialog();

        test.setCallback(new UpjongDialog.IFCallback() {
            @Override
            public void callback(String code, String name) {
                textfield.setText("group_"+name);
                textfield.paint(textfield.getGraphics());
                myweb.getNaverUpjong(code, name);
                JOptionPane.showConfirmDialog(popupfrm, "full down을 누르세요. ","TITTLE", JOptionPane.YES_NO_CANCEL_OPTION);
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
                    idown.downloadNowPrice(stock_list, 3);

                    // 다운로드 완료하고 지금부터는 파일데이터를 리스트에 로딩한다
                    web_stockinfo = iread.getStockInfoCustom(filename);
                    model = renderer.loadInfoChart2List(web_stockinfo);
                    JList<StockBook> listBook = new JList<StockBook>(model); // 생성된 list data를 list에 넣어주고
                    listBook.setCellRenderer(new StockBookRenderer()); // data를 그려줄 custom render를 붙여주고

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
                    frame.pack();
                    frame.setVisible(true);
                }

                // 보유종목 다운로드
                if(button2.equals(ae.getSource())){

                    String filename = "group_hold";
                    DefaultListModel<StockBook> model = new DefaultListModel<>();
                    textfield.setText("group_hold");

                    List<String> stock_list = new ArrayList<>();
                    stock_list = iread.getStockListCustom(filename);
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

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
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

                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
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


                    // 기존의 리스트는 지우고 신규리스트를 추가한다
                    listpanel.removeAll();
                    listpanel.setPreferredSize(dim);
                    listpanel.add(new JScrollPane(listBook), BorderLayout.CENTER);

                    textfield.setText(filename);
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
                    /*
                    String[] upjong = {"test","test1","test2","test3","test4"};

                    //int qut_data = JOptionPane.showConfirmDialog(popupfrm, "질문 알림창입니다 !! ","TITTLE", JOptionPane.YES_NO_CANCEL_OPTION);
                    int qut_data = JOptionPane.showOptionDialog(popupfrm, "먹고싶은 과일은?", "Option"
                            ,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,upjong, null );

                    System.out.println("click number is " + qut_data);
                     */
                    test.setSize(1200, 600);
                    test.setLocation(200,100);
                    test.setVisible(true);

                }

            }
        };
        button1.addActionListener(listener);
        button2.addActionListener(listener);
        button3.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);
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
