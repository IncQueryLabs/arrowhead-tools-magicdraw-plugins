package com.incquerylabs.arrowhead.pd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

public class FromAhxAction implements ActionListener {
	
	private ConverterGui window;
	
	public FromAhxAction(ConverterGui window) {
		this.window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser ahxChooser = new JFileChooser();
		ahxChooser.setDialogTitle("Choose AHX file to convert");
		ahxChooser.addChoosableFileFilter(window.getFilter());
		ahxChooser.setFileFilter(window.getFilter());
		int gotAhx = ahxChooser.showDialog(window, "Choose");
		if(gotAhx == JFileChooser.APPROVE_OPTION) {
			File ahxFile = ahxChooser.getSelectedFile();
			JFileChooser pdChooser = new JFileChooser();
			pdChooser.setDialogTitle("Choose the location of the output PD file");
			pdChooser.addChoosableFileFilter(window.getFilter());
			pdChooser.setFileFilter(window.getFilter());
			int gotPd = pdChooser.showDialog(window, "Choose");
			if(gotPd == JFileChooser.APPROVE_OPTION) {
				try {
					AhxPdConverter.fromAhx(ahxFile, window.conform(pdChooser.getSelectedFile()));
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
