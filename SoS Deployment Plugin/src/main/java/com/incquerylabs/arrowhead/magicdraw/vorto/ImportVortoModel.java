package com.incquerylabs.arrowhead.magicdraw.vorto;

import com.incquerylabs.arrowhead.magicdraw.vorto.auto.VortoProfile;
import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.ui.ProgressStatusRunner;
import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.ModelInfo;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;
import org.jboss.netty.util.internal.jzlib.ZStream;

public class ImportVortoModel extends MDAction {
	
	public ImportVortoModel() {
		super(ImportVortoModel.class.getName(), "Vorto Repository", null, ActionsGroups.PROJECT_OPENED_RELATED);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Project project = Application.getInstance().getProject();
		if(project != null) {
			if(VortoProfile.getInstance(project).getProfile() != null) {
				IRepositoryClient client = RepositoryClientBuilder.newBuilder().setBaseUrl("https://vorto.eclipse.org/")
					.build(); //config?
				
				Map<String, ModelId> ids = new HashMap<>();
				
				List<String> lines = new ArrayList<>();
				Collection<ModelInfo> infos = client.search("name:*");
				infos.stream().map(AbstractModel::getId).forEach(id -> {
					String pretty = id.getPrettyFormat();
					lines.add(pretty);
					ids.put(pretty, id);
				});
				lines.sort(String::compareTo);
				
				Window parent = MDDialogParentProvider.getProvider().getDialogOwner();
				JDialog dialog = new JDialog(parent, "Importing from Vorto repository", ModalityType.APPLICATION_MODAL);
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.setLayout(new BorderLayout());
				
				JPanel buttons = new JPanel();
				FlowLayout buttonLayout = new FlowLayout();
				buttons.setLayout(buttonLayout);
				buttonLayout.setAlignment(FlowLayout.TRAILING);
				dialog.add(buttons, BorderLayout.SOUTH);
				
				JButton ok = new JButton("Ok");
				ok.setEnabled(false);
				buttons.add(ok);
				
				String[] array = lines.toArray(new String[0]);
				JList<String> list = new JList<>(array);
				list.setVisibleRowCount(-1);
				JScrollPane scroll = new JScrollPane(list);
				dialog.add(scroll, BorderLayout.CENTER);
				list.addListSelectionListener(e -> ok.setEnabled(list.getSelectedIndices().length > 0));
				ok.addActionListener(e -> {
					Set<ModelId> selected = list.getSelectedValuesList().stream().map(ids::get)
						.collect(Collectors.toSet());
					if(!selected.isEmpty()) {
						ProgressStatusRunner.runWithProgressStatus(new ImportWithProgress(client, selected),
							"Importing from Vorto repository", true, 0);
						dialog.dispose();
					}
				});
				
				JButton cancel = new JButton("Cancel");
				buttons.add(cancel);
				cancel.addActionListener(e -> dialog.dispose());
				
				JLabel filterLabel = new JLabel("  Filter:"); //incredibly higy tech
				JTextField filter = new JTextField();
				JPanel filterPanel = new JPanel();
				dialog.add(filterPanel, BorderLayout.NORTH);
				filterPanel.setLayout(new GridBagLayout());
				GridBagConstraints labelConstraint = new GridBagConstraints();
				labelConstraint.anchor = GridBagConstraints.EAST;
				labelConstraint.gridwidth = GridBagConstraints.RELATIVE;
				labelConstraint.ipadx = 5;
				filterPanel.add(filterLabel, labelConstraint);
				GridBagConstraints boxConstraint = new GridBagConstraints();
				boxConstraint.weightx = 1;
				boxConstraint.fill = GridBagConstraints.HORIZONTAL;
				boxConstraint.gridwidth = GridBagConstraints.REMAINDER;
				filterPanel.add(filter, boxConstraint);
				filter.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						String text = filter.getText();
						String[] filters = text.split("\\s");
						if(filters.length > 0) {
							Stream<String> stream = lines.stream();
							for(String term : filters) {
								stream = stream.filter(id -> id.toLowerCase().contains(term.toLowerCase()));
							}
							List<String> hits = stream.sorted(String::compareTo).collect(Collectors.toList());
							DefaultListModel<String> listModel = new DefaultListModel<>();
							hits.forEach(listModel::addElement);
							list.setModel(listModel);
						}
					}
				});
				
				dialog.setMinimumSize(new Dimension(400, 400));
				dialog.setSize(600, 800);
				dialog.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(MDDialogParentProvider.getProvider().getDialogOwner(),
					"To import models from the Vorto Repository the Arrowhead Vorto extension profile needs to be in this project.",
					"Profile not found", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
