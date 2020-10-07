package com.incquerylabs.arrowhead.bpmn;

import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.cbm.profiles.BPMN2Profile;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.magicdraw.ui.dialogs.SelectElementInfo;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlg;
import com.nomagic.magicdraw.ui.dialogs.selection.ElementSelectionDlgFactory;
import com.nomagic.magicdraw.ui.dialogs.selection.TypeFilter;
import com.nomagic.magicdraw.ui.dialogs.selection.TypeFilterImpl;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.magicdraw.uml.ClassTypes;
import com.nomagic.ui.ProgressStatusRunner;
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.File;

public class ExportAction extends MDAction {

    public static final String ACTION_TITLE = "JSon or something";
    private static final String FILE_CHOOSER_TITLE = "Select output File";
    private static final String FILE_CHOOSER_BUTTON_TEXT = "Export";
    public static final String JSON = "JSON";
    public static final String JSON_EXTENSION = ".json";
    private static final String PROGRESS_TITLE = "Exporting to " + ACTION_TITLE;

    public ExportAction() {
        super(ExportAction.class.getName(), ACTION_TITLE, null, ActionsGroups.PROJECT_OPENED_RELATED);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Project project = Application.getInstance().getProject();
        if (project != null) {
            //user selects the local cloud to be exported
            ElementSelectionDlg elementSelector = ElementSelectionDlgFactory
                    .create(MDDialogParentProvider.getProvider().getDialogOwner());
            TypeFilter filter = new ProcessFilter();
            SelectElementInfo info = new SelectElementInfo(false, false, project.getPrimaryModel(), false);
            ElementSelectionDlgFactory.initSingle(elementSelector, info, filter, filter, null, null);
            elementSelector.setVisible(true);

            if (elementSelector.isOkClicked()) {
                //user selects location and filename of output
                JFileChooser outputSelector = new JFileChooser();
                outputSelector.setDialogTitle(FILE_CHOOSER_TITLE);
                //case insensitive
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(JSON, JSON);
                outputSelector.addChoosableFileFilter(fileFilter);
                outputSelector.setFileFilter(fileFilter);

                int returnVal = outputSelector.showDialog(null, FILE_CHOOSER_BUTTON_TEXT);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File chosen = conform(outputSelector.getSelectedFile(), fileFilter);
                    BaseElement element = elementSelector.getSelectedElement();
                    if (element != null) {
                        ExportProgress exporter = new ExportProgress((Activity) element, chosen);
                        ProgressStatusRunner
                                .runWithProgressStatus(exporter, PROGRESS_TITLE, false, 0);
                    }
                }
            }
        }
    }

    private static File conform(File file, FileNameExtensionFilter filter) {
        if (filter.accept(file)) {
            return file;
        } else {
            return new File(file.toString() + JSON_EXTENSION);
        }
    }

    private static class ProcessFilter extends TypeFilterImpl {

        public ProcessFilter() {
            super(ClassTypes.getSubtypes(Activity.class));
        }

        @Override
        public boolean accept(BaseElement elem, boolean arg1) {
            if(Activity.class.isAssignableFrom(elem.getClass())) {
                Activity c = (Activity) elem;
                return BPMN2Profile.isBPMNProcess(c);
            } else {
                return false;
            }
        }
    }
}
