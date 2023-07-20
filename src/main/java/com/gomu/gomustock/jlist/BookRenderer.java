package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.jlist.Book;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookRenderer extends JPanel implements ListCellRenderer<Book> {

	private JLabel lbFognIcon = new JLabel();
	private JLabel lbAgencyIcon = new JLabel();
	private JTextArea  lbIndication = new JTextArea();
	private JTextArea lbCompanyinfo = new JTextArea();
	private JTextArea lbNews = new JTextArea();
	private JPanel lbChart = new JPanel();
	private JPanel panelText;
	private JPanel panelFAIcon;
	private JPanel panelAgencyIcon;
	private JPanel panelChart;
	private JPanel panelNews;

	String STOCKDIR = "D:\\MyML\\GomuStockJ\\";;

	public BookRenderer() {
		setLayout(new BorderLayout(5, 5));
		// panelText는 xml 역할을 하고
		// Renderercomponent는 ID역할을 한다
		panelText = new JPanel(new GridLayout(1, 3));
		panelText.add(lbIndication);
		panelText.add(lbCompanyinfo);
		panelText.add(lbNews);

		panelFAIcon = new JPanel(new GridLayout(1, 2));
		panelFAIcon.add(lbFognIcon);
		panelFAIcon.add(lbAgencyIcon);

		panelChart = new JPanel();
		panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelChart.add(lbChart);

		add(panelText, BorderLayout.WEST);
		add(panelChart, BorderLayout.CENTER);
		add(panelFAIcon, BorderLayout.EAST);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Book> list,
			Book book, int index, boolean isSelected, boolean cellHasFocus) {

		//String icon_path = STOCKDIR+book.getIconName() + ".jpg";
		BufferedImage img = null;
		try {
			img = getFognimage(book.getStockcode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		lbFognIcon.setIcon( new ImageIcon(img));

		BufferedImage img2 = null;
		try {
			img2 = getAgencyimage(book.getStockcode());
		} catch (IOException e) {
			e.printStackTrace();
		}
		lbAgencyIcon.setIcon( new ImageIcon(img2));


		lbIndication.setText(book.getIndication());
		lbIndication.setPreferredSize(new Dimension(100,200));
		lbIndication.setAutoscrolls(true);

		lbNews.setText(book.getNews());
		lbNews.setPreferredSize(new Dimension(300,200));
		lbNews.setAutoscrolls(true);

		lbCompanyinfo.setText(book.getCompanyInfo());
		lbCompanyinfo.setForeground(Color.blue);
		lbCompanyinfo.setPreferredSize(new Dimension(300,200));
		lbCompanyinfo.setLineWrap(true);

		// set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
		lbChart.removeAll();
		lbChart.add(new XChartPanel(book.getChart()));
		lbChart.setPreferredSize(new Dimension(300,200));
		System.out.println("book index " + index);

		// set Opaque to change background color of JLabel
		lbIndication.setOpaque(true);
		lbCompanyinfo.setOpaque(true);
		lbFognIcon.setOpaque(true);
		lbAgencyIcon.setOpaque(true);
		lbChart.setOpaque(true);

		// when select item
		if (isSelected) {
			lbIndication.setBackground(list.getSelectionBackground());
			lbCompanyinfo.setBackground(list.getSelectionBackground());
			lbFognIcon.setBackground(list.getSelectionBackground());
			lbAgencyIcon.setBackground(list.getSelectionBackground());
			lbNews.setBackground(list.getSelectionBackground());
			setBackground(list.getSelectionBackground());
			panelFAIcon.setBackground(list.getSelectionBackground());
			//lbChart.setBackground(list.getSelectionBackground());
;
		} else { // when don't select
			lbIndication.setBackground(list.getBackground());
			lbCompanyinfo.setBackground(list.getBackground());
			lbNews.setBackground(list.getBackground());
			lbFognIcon.setBackground(list.getBackground());
			lbAgencyIcon.setBackground(list.getBackground());
			setBackground(list.getBackground());
			panelFAIcon.setBackground(list.getBackground());
			//lbChart.setBackground(list.getBackground());
		}
		return this;
	}

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

}
