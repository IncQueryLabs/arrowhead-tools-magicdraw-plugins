package com.incquerylabs.arrowhead.magicdraw.vorto;

import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.ui.ProgressStatusRunner;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.annotation.CheckForNull;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportLocalVortoModel extends MDAction {

    private static final String FILE_CHOOSER_TITLE = "Select input file";
    private static final String PROGRESS_TITLE = "Importing from local Vorto model";
    private static final String ACTION_TITLE = "Local Vorto Model";


    public ImportLocalVortoModel() {
        super(ImportLocalVortoModel.class.getName(), ImportLocalVortoModel.ACTION_TITLE, null,
                ActionsGroups.PROJECT_OPENED_RELATED);
    }

    @Override
    public void actionPerformed(@CheckForNull ActionEvent actionEvent) {

        JFileChooser inputSelector = new JFileChooser();
        inputSelector.setDialogTitle(FILE_CHOOSER_TITLE);
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("infomodel", "fbmodel");
        inputSelector.addChoosableFileFilter(fileFilter);
        inputSelector.setFileFilter(fileFilter);
        inputSelector.setApproveButtonText("Import");
        int returnVal = inputSelector.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = inputSelector.getSelectedFile();
            ImportLocalWithProgress importer = new ImportLocalWithProgress(file);
            ProgressStatusRunner.runWithProgressStatus(importer, PROGRESS_TITLE, false, 0);
        }
    }
}
