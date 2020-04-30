package com.incquerylabs.arrowhead.pd;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ConverterGui extends JFrame {
	
	private FileFilter filter = new FileNameExtensionFilter("JSON files", "JSON");
	
	public ConverterGui() throws IOException {
		setTitle("AHX-PD converter");
		setIconImage(ImageIO.read(getClass().getResource("/arrowhead_blue.png")));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1, 2));
		setResizable(false);
		setMinimumSize(new Dimension(480, 70));
		
		JButton fromAhx = new JButton("Convert from AHX to PD");
		fromAhx.setFocusPainted(false);
		fromAhx.addActionListener(new FromAhxAction(this));
		
		JButton fromPd = new JButton("Convert from PD to AHX");
		fromPd.setFocusPainted(false);
		fromPd.addActionListener(new FromPdAction(this));
		
		add(fromAhx);
		add(fromPd);
	}
	
	public static void main(String[] args)
		throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException,
		       IOException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		ConverterGui gui = new ConverterGui();
		gui.setVisible(true);
	}
	
	public FileFilter getFilter() {
		return filter;
	}
	
	public File conform(File file) {
		if(filter.accept(file)) {
			return file;
		} else {
			return new File(file.toString() + ".json");
		}
	}
}
