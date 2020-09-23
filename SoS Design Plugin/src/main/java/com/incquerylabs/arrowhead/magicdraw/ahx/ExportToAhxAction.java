package com.incquerylabs.arrowhead.magicdraw.ahx;

import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile;
import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
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
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportToAhxAction extends MDAction {
	
	public static final String ACTION_TITLE = "AHX File";
	public static final String JSON = "JSON";
	public static final String JSON_EXTENSION = ".json";
	public static final String SECURE = "SECURE";
	public static final String INSECURE = "INSECURE";
	public static final String DEFAULT_PROTOCOL = "HTTP";
	public static final String DEFAULT_SECURITY = INSECURE;
	public static final String DEFAULT_FORMAT = JSON;
	
	private static final String FILE_CHOOSER_TITLE = "Select output File";
	private static final String FILE_CHOOSER_BUTTON_TEXT = "Export";
	private static final String PROGRESS_TITLE = "Exporting to AHX file";
	
	public ExportToAhxAction() {
		super(ExportToAhxAction.class.getName(), ACTION_TITLE, null, ActionsGroups.PROJECT_OPENED_RELATED);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Project project = Application.getInstance().getProject();
		if(project != null) {
			//user selects the local cloud to be exported
			ElementSelectionDlg elementSelector = ElementSelectionDlgFactory
				.create(MDDialogParentProvider.getProvider().getDialogOwner());
			TypeFilter filter = new LocalCloudFilter();
			SelectElementInfo info = new SelectElementInfo(false, false, project.getPrimaryModel(), false);
			ElementSelectionDlgFactory.initSingle(elementSelector, info, filter, filter, null, null);
			elementSelector.setVisible(true);
			
			if(elementSelector.isOkClicked()) {
				//user selects location and filename of output
				JFileChooser outputSelector = new JFileChooser();
				outputSelector.setDialogTitle(FILE_CHOOSER_TITLE);
				//case insensitive
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(JSON, JSON);
				outputSelector.addChoosableFileFilter(fileFilter);
				outputSelector.setFileFilter(fileFilter);
				
				int returnVal = outputSelector.showDialog(null, FILE_CHOOSER_BUTTON_TEXT);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File chosen = conform(outputSelector.getSelectedFile(), fileFilter);
					Class cloudElement = (Class) elementSelector.getSelectedElement();
					if(ArrowheadProfile.isLocalCloud(cloudElement)) {
						ExportToAhxWithProgress exporter = new ExportToAhxWithProgress(cloudElement, chosen);
						ProgressStatusRunner
							.runWithProgressStatus(exporter, PROGRESS_TITLE, false, 0);
					} //TODO else
				}
			}
		}
	}
	
	private static File conform(File file, FileNameExtensionFilter filter) {
		if(filter.accept(file)) {
			return file;
		} else {
			return new File(file.toString() + JSON_EXTENSION);
		}
	}
	
	private static class LocalCloudFilter extends TypeFilterImpl {
		
		public LocalCloudFilter() {
			super(ClassTypes.getSubtypes(com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class.class));
		}
		
		@Override
		public boolean accept(BaseElement elem, boolean arg1) {
			if(Class.class.isAssignableFrom(elem.getClass())) {
				Class c = (Class) elem;
				return ArrowheadProfile.isLocalCloud(c);
			} else {
				return false;
			}
		}
	}
	
	private static final long serialVersionUID = 1702158702388839765L;
}
