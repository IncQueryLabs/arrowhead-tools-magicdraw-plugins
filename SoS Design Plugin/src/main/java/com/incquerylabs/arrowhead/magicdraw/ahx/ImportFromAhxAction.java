package com.incquerylabs.arrowhead.magicdraw.ahx;

import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.ui.ProgressStatusRunner;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.annotation.CheckForNull;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportFromAhxAction extends MDAction {
	
	private static final String FILE_CHOOSER_TITLE = "Select input File";
	private static final String PROGRESS_TITLE = "Importing from AHX file";
	
	public ImportFromAhxAction() {
		super(ImportFromAhxAction.class.getName(), ExportToAhxAction.ACTION_TITLE, null,
			ActionsGroups.PROJECT_OPENED_RELATED);
	}
	
	@Override
	public void actionPerformed(@CheckForNull ActionEvent actionEvent) {
		
		JFileChooser inputSelector = new JFileChooser();
		inputSelector.setDialogTitle(FILE_CHOOSER_TITLE);
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(ExportToAhxAction.JSON, ExportToAhxAction.JSON);
		inputSelector.addChoosableFileFilter(fileFilter);
		inputSelector.setFileFilter(fileFilter);
		inputSelector.setApproveButtonText("Import");
		int returnVal = inputSelector.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = inputSelector.getSelectedFile();
			ImportFromAhxWithProgress importer = new ImportFromAhxWithProgress(file);
			ProgressStatusRunner.runWithProgressStatus(importer, PROGRESS_TITLE, false, 0);
		}
	}
}
