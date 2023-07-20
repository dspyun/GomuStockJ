package main.java.com.gomu.gomustock.jlist;

import main.java.com.gomu.gomustock.jlist.Book;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;

import java.awt.*;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

public class BookRenderer extends JPanel implements ListCellRenderer<Book> {

	private JLabel lbIcon = new JLabel();
	private JLabel lbName = new JLabel();
	private JLabel lbAuthor = new JLabel();
	private JPanel lbChart = new JPanel();
	private JPanel panelText;
	private JPanel panelIcon;
	private JPanel panelChart;

	String STOCKDIR = "D:\\MyML\\GomuStockJ\\";;

	public BookRenderer() {
		setLayout(new BorderLayout(5, 5));
		// panelText는 xml이고 lbName은 id이다
		// Renderercomponent에서 id에 실제 값을 넣어준다
		panelText = new JPanel(new GridLayout(0, 1));
		panelText.add(lbName);
		panelText.add(lbAuthor);

		panelIcon = new JPanel();
		panelIcon.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelIcon.add(lbIcon);

		panelChart = new JPanel();
		panelChart.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelChart.add(lbChart);

		add(panelChart, BorderLayout.WEST);
		add(panelIcon, BorderLayout.EAST);
		add(panelText, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Book> list,
			Book book, int index, boolean isSelected, boolean cellHasFocus) {

		String icon_path = STOCKDIR+book.getIconName() + ".jpg";
		lbIcon.setIcon(new ImageIcon(icon_path));
		lbName.setText(book.getName());
		lbAuthor.setText(book.getAuthor());
		lbAuthor.setForeground(Color.blue);
		lbChart.add(new XChartPanel(book.getChart()));
		lbChart.setPreferredSize(new Dimension(200,200));
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
			setBackground(list.getSelectionBackground());
			panelIcon.setBackground(list.getSelectionBackground());
			//lbChart.setBackground(list.getSelectionBackground());
;
		} else { // when don't select
			lbName.setBackground(list.getBackground());
			lbAuthor.setBackground(list.getBackground());
			lbIcon.setBackground(list.getBackground());
			setBackground(list.getBackground());
			panelIcon.setBackground(list.getBackground());
			//lbChart.setBackground(list.getBackground());
		}
		return this;
	}
}
