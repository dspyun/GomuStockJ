package main.java.com.gomu.gomustock.jlist;

import org.knowm.xchart.XYChart;

import javax.swing.*;

public class Book {
	private String name;
	private String author;
	private String iconName;
	private XYChart chart;

	public Book(String name, String author, String iconName) {
		super();
		this.name = name;
		this.author = author;
		this.iconName = iconName;
	}

	public Book(String name, String author, String iconName, XYChart chart) {
		super();
		this.name = name;
		this.author = author;
		this.iconName = iconName;
		this.chart = chart;
	}

	public XYChart getChart() { return chart;}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	@Override
	public String toString() {
		return name + " - " + author;
	}
}
