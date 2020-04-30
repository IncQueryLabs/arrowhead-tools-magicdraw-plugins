package com.incquerylabs.arrowhead.pd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFileChooser;

public class FromPdAction implements ActionListener {
	
	private ConverterGui window;
	
	public FromPdAction(ConverterGui window) {
		this.window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser pdChooser = new JFileChooser();
		pdChooser.setDialogTitle("Choose PD files to convert");
		pdChooser.addChoosableFileFilter(window.getFilter());
		pdChooser.setFileFilter(window.getFilter());
		pdChooser.setMultiSelectionEnabled(true);
		int gotPd = pdChooser.showDialog(window, "Choose");
		if(gotPd == JFileChooser.APPROVE_OPTION) {
			JFileChooser ahxChooser = new JFileChooser();
			ahxChooser.setDialogTitle("Choose the location of the output AHX file");
			ahxChooser.addChoosableFileFilter(window.getFilter());
			ahxChooser.setFileFilter(window.getFilter());
			int gotAhx = ahxChooser.showDialog(window, "Choose");
			if(gotAhx == JFileChooser.APPROVE_OPTION) {
				try {
					AhxPdConverter.fromPd(pdChooser.getSelectedFiles(), window.conform(ahxChooser.getSelectedFile()));
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
