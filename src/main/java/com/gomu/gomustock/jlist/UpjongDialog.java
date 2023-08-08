package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.MyExcel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UpjongDialog extends Frame implements ActionListener {
    Panel p;
    String code, name;
    MyExcel myexcel = new MyExcel();
    java.util.List<String> codelist = myexcel.readColumn("table_group.xls",0);
    java.util.List<String> namelist = myexcel.readColumn("table_group.xls",1);


   public UpjongDialog() {

        //JFrame frame = input_frame;
        p = new Panel();
        p.setLayout(new GridLayout(8,10));

        List<JButton> btnlist = new ArrayList<>();
        int size = namelist.size();
        for(int i =1;i<size;i++) {
            JButton onebtn = new JButton(namelist.get(i));
            btnlist.add(onebtn);
        }
        size = btnlist.size();
        for(int i=0;i<size;i++) {
            p.add(btnlist.get(i));
            btnlist.get(i).addActionListener(this);
        }
        add(p);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int size = codelist.size();
        for(int i =0;i<size;i++) {
            if(e.getActionCommand().equals(namelist.get(i))) {
                System.out.println(codelist.get(i));
                code = codelist.get(i);
                name = namelist.get(i);
                dispose();
                _cb.callback(code, name);
                break;
            }
        }
    }

    public interface IFCallback {
        void callback(String str1, String str2);
    }
    // 콜백인터페이스를 구현한 클래스 인스턴스
    private UpjongDialog.IFCallback _cb;
    public void setCallback(UpjongDialog.IFCallback cb) {
        this._cb = cb;
    }

}