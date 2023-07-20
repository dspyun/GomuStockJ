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

	private JLabel lbIcon = new JLabel();
	private JTextArea  lbName = new JTextArea();
	private JTextArea lbAuthor = new JTextArea();
	private JTextArea lbNews = new JTextArea();
	private JPanel lbChart = new JPanel();
	private JPanel panelText;
	private JPanel panelIcon;
	private JPanel panelChart;
	private JPanel panelNews;

	String STOCKDIR = "D:\\MyML\\GomuStockJ\\";;

	public BookRenderer() {
		setLayout(new BorderLayout(5, 5));
		// panelText는 xml 역할을 하고
		// Renderercomponent는 ID역할을 한다
		panelText = new JPanel(new GridLayout(1, 3));
		panelText.add(lbName);
		panelText.add(lbAuthor);
		panelText.add(lbNews);

		panelIcon = new JPanel();
		panelIcon.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelIcon.add(lbIcon);

		panelChart = new JPanel();
		panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelChart.add(lbChart);

		add(panelChart, BorderLayout.CENTER);
		add(panelIcon, BorderLayout.EAST);
		add(panelText, BorderLayout.WEST);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Book> list,
			Book book, int index, boolean isSelected, boolean cellHasFocus) {

		String icon_path = STOCKDIR+book.getIconName() + ".jpg";
		try {
			URL url = new URL("https://ssl.pstatic.net/imgfinance/chart/trader/month1/F_000660.png");
			BufferedImage img = ImageIO.read(url);
			lbIcon.setIcon( new ImageIcon(img));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		lbName.setText(book.getName());
		lbName.setPreferredSize(new Dimension(100,200));
		lbName.setAutoscrolls(true);

		lbNews.setText(book.getNews());
		lbNews.setPreferredSize(new Dimension(300,200));
		lbNews.setAutoscrolls(true);

		lbAuthor.setText(book.getAuthor());
		lbAuthor.setForeground(Color.blue);
		lbAuthor.setPreferredSize(new Dimension(300,200));
		lbAuthor.setLineWrap(true);

		// set이 아니고 add를 쓰기 때문에 이전 데이터를 지워줘야 한다
		lbChart.removeAll();
		lbChart.add(new XChartPanel(book.getChart()));
		lbChart.setPreferredSize(new Dimension(300,200));
		System.out.println("book index " + index);

		// set Opaque to change background color of JLabel
		lbName.setOpaque(true);
		lbAuthor.setOpaque(true);
		lbIcon.setOpaque(true);
		lbChart.setOpaque(true);

		// when select item
		if (isSelected) {
			lbName.setBackground(list.getSelectionBackground());
			lbAuthor.setBackground(list.getSelectionBackground());
			lbIcon.setBackground(list.getSelectionBackground());
			lbNews.setBackground(list.getSelectionBackground());
			setBackground(list.getSelectionBackground());
			panelIcon.setBackground(list.getSelectionBackground());
			//lbChart.setBackground(list.getSelectionBackground());
;
		} else { // when don't select
			lbName.setBackground(list.getBackground());
			lbAuthor.setBackground(list.getBackground());
			lbNews.setBackground(list.getBackground());
			lbIcon.setBackground(list.getBackground());
			setBackground(list.getBackground());
			panelIcon.setBackground(list.getBackground());
			//lbChart.setBackground(list.getBackground());
		}
		return this;
	}
}
