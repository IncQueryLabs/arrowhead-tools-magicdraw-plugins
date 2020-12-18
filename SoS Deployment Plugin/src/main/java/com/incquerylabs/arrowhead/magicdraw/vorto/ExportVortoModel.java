package com.incquerylabs.arrowhead.magicdraw.vorto;

import com.incquerylabs.arrowhead.magicdraw.vorto.auto.VortoProfile;
import com.incquerylabs.arrowhead.magicdraw.vorto.auto.VortoProfile.VortoProperty;
import com.incquerylabs.arrowhead.magicdraw.vorto.auto.VortoProfile.VortoVersionEnum;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsGroups;
import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.foundation.MDExtension;
import com.nomagic.magicdraw.sysml.util.SysMLProfile;
import com.nomagic.magicdraw.ui.dialogs.MDDialogParentProvider;
import com.nomagic.ui.ProgressStatusRunner;
import com.nomagic.uml2.ext.magicdraw.activities.mdintermediateactivities.ActivityPartition;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdmodels.Model;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DirectedRelationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ElementValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Feature;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.MultiplicityElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PackageableElement;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Relationship;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.TypedElement;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;
import com.nomagic.uml2.impl.ElementsFactory;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;

import akka.cluster.protobuf.msg.ClusterMessages.ConfigCheck.Type;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.AbstractProperty;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Infomodel.InfomodelBuilder;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.ModelInfo;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;

public class ExportVortoModel extends MDAction {
	
	private VortoProfile profile;
	
	public ExportVortoModel() {
		super(ExportVortoModel.class.getName(), "Vorto Import File", null, ActionsGroups.PROJECT_SAVING_RELATED);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Project project = Application.getInstance().getProject();
		if(project != null) {
			if(VortoProfile.getInstance(project).getProfile() != null) {
				List<String> lines = new ArrayList<>();
				profile = VortoProfile.getInstance(project);
				
				List<Package> projectModels = project.getModels();
				for(Package pac : projectModels) {
					Model model = (Model)pac;
					for(PackageableElement mpac : model.getPackagedElement()) {
						lines.add(mpac.getName());
					}
				}
				
				lines.sort(String::compareTo);
				
				Window parent = MDDialogParentProvider.getProvider().getDialogOwner();
				JDialog dialog = new JDialog(parent, "Exporting to Vorto repository file", ModalityType.APPLICATION_MODAL);
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
					List<String> selected = list.getSelectedValuesList();
					if(!selected.isEmpty()) {
						exportAction(project, selected);
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
	
	private List<Element> recursiveSearch(Collection<Element> collection) {
		List<Element> foundElements = new ArrayList<>();
		if(collection.isEmpty()) {
			return foundElements;
		}
	
		for(Element element : collection) {
			if(element.getHumanType().equals("Information Model") || element.getHumanType().equals("Function Block") || 
					element.getHumanType().equals("Enumeration") || element.getHumanType().equals("Vorto Entity")  || element.getClassType().equals(Dependency.class)) {
				foundElements.add(element);
			}
			foundElements.addAll(recursiveSearch(element.getOwnedElement()));
		}
		return foundElements;
	}
	
	private void writeToZip(File file, ZipOutputStream zo) throws IOException {
		FileInputStream fi = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(file.getName());
		zo.putNextEntry(zipEntry);
		
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fi.read(bytes)) >= 0) {
			zo.write(bytes, 0, length);
		}
		
		zo.closeEntry();
		fi.close();
	}
	
	private AbstractModel exportModel(Element absModel) {
		String category = VortoProfile.Model.getCategory(absModel)!=null ? VortoProfile.Model.getCategory(absModel) : "";
		VortoVersionEnum vortolang = VortoProfile.Model.getVortolang(absModel);
		String namespace = VortoProfile.Model.getNamespace(absModel)!=null ? VortoProfile.Model.getNamespace(absModel) : "";
		String shortname = VortoProfile.Model.getIdentifier(absModel)!=null ? VortoProfile.Model.getIdentifier(absModel) : "";
		String version = VortoProfile.Model.getVersion(absModel)!=null ? VortoProfile.Model.getVersion(absModel) : "";
		String description = VortoProfile.Model.getDescription(absModel)!=null ? VortoProfile.Model.getDescription(absModel) : "";
		
		ModelId mId = new ModelId(shortname,namespace,version);
		AbstractModel model = null;
		
		if(absModel.getHumanType().toLowerCase().contains("information model")) {
			model = new Infomodel(mId);
		} else if(absModel.getHumanType().toLowerCase().contains("function block")) {
			model = new FunctionblockModel(mId);
		}else if(absModel.getHumanType().toLowerCase().contains("vorto entity")) {
			model = new EntityModel(mId);
		}else if(absModel.getHumanType().toLowerCase().contains("enumeration")){
			model = new org.eclipse.vorto.model.EnumModel(mId);
		}
		
		model.setDisplayName(shortname);
		model.setCategory(category);
		model.setVortolang(vortolang.getText());
		model.setDescription(description);
		
		return model;
	}
	
	private List<NamedElement> getDependencies (Element im, List<Dependency> deps, List<Element> vortoElements) {
		List<NamedElement> mps = new ArrayList<>();
		for (Dependency dependency : deps) {
			if(dependency.getClient().contains(im)) {
				for (NamedElement suplier : dependency.getSupplier()) {
					for (Element element : vortoElements) {
						if(((NamedElement)element).getName().equals(suplier.getName())) {
							mps.add(((NamedElement)element));
						}
					}
				}
			}
		}
		return mps;
	}
	
	private String getPrimitiveType(com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type type) {
		SysMLProfile sysML = SysMLProfile.getInstance(profile.getInteger());
		if(type.equals(sysML.getString())) {
			return "string";
		}
		else if(type.equals(profile.getInteger())) {
			return "int";
		}
		else if(type.equals(profile.getFloat())) {
			return "float";
		}
		else if(type.equals(sysML.getBoolean())) {
			return "boolean";
		}
		else if(type.equals(profile.getDateTime())) {
			return "dateTime";
		}
		else if(type.equals(profile.getDouble())) {
			return "double";
		}
		else if(type.equals(profile.getLong())) {
			return "long";
		}
		else if(type.equals(profile.getShort())) {
			return "short";
		}
		else if(type.equals(profile.getBase64())) {
			return "base64Binary";
		}
		else if(type.equals(profile.getByte())) {
			return "byte";
		}
		else {
			return type.getName();
		}
	}
		
	private void exportAction(Project project, List<String> selected) {
		String text = "";
		List<Element> vortoElements = new ArrayList<>();
		List<Dependency> vortoDependencies = new ArrayList<>();
		
		try {
			for(Package pac : project.getModels()) {
				Model model = (Model)pac;
				for(PackageableElement mpac : model.getPackagedElement()) {
					if(selected.contains(mpac.getName())) {
						vortoElements = recursiveSearch(mpac.getOwnedElement());
					}
				}
			}
			if(vortoElements.isEmpty()) {
				throw(new FileNotFoundException());
			}
			else {
				String mainDir = "exports";
				File directory = new File(mainDir);
			    if (! directory.exists()){
			        directory.mkdir();
			    }
				
			    String projectDir = selected.get(0);
				File pdirectory = new File(mainDir + "/" + projectDir);
			    if (! pdirectory.exists()){
			    	pdirectory.mkdir();
			    }
			    
				for(Element depElement : vortoElements){
					if(depElement.getClassType().equals(Dependency.class)) {
						Dependency dep = (Dependency) depElement;
						vortoDependencies.add(dep);
					}
				}
				
				for(Element absModel : vortoElements){//-----------------------------------------------------------------------------------------------------------
					if(absModel.getHumanType().equals("Information Model")) {
						Infomodel im = (Infomodel)exportModel(absModel);
						List<NamedElement> fbs = getDependencies(absModel, vortoDependencies, vortoElements);
						exportModel(im, fbs, mainDir + "/" + projectDir );
						//printAll(absModel, mainDir + "/" + projectDir);	
					} else if(absModel.getHumanType().equals("Function Block")) {
						FunctionblockModel im = (FunctionblockModel)exportModel(absModel);
						exportBlock(im, absModel, mainDir + "/" + projectDir );
						//printAll(absModel, mainDir + "/" + projectDir);						
					}else if(absModel.getHumanType().equals("Enumeration")) {
						EnumModel im = (EnumModel)exportModel(absModel);
						exportEnum(im, absModel, mainDir + "/" + projectDir );
						//printAll(absModel, mainDir + "/" + projectDir);	
					}else if(absModel.getHumanType().equals("Vorto Entity")) {
						EntityModel im = (EntityModel)exportModel(absModel);
						exportEntity(im, absModel, mainDir + "/" + projectDir );
						//printAll(absModel, mainDir + "/" + projectDir);	
					}else {
						Logger.getLogger(this.getClass()).error("Export for " + absModel.getClass().getName() + " not implemented yet");
					}
				}
				String zipPath = mainDir + "/" + projectDir + "/" + selected.get(0) + ".zip";
				FileOutputStream fo = new FileOutputStream(zipPath);
				ZipOutputStream zo = new ZipOutputStream(fo);
				File rootFolder = new File(mainDir + "/" + projectDir);
				
				for(File file : rootFolder.listFiles()) {
					if(!file.getName().contains("zip"))
						writeToZip(file, zo);
				}
				
				zo.close();
				fo.close();
			}
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, e.getStackTrace());
		}
	}
	
	private void exportModel(Infomodel im, List<NamedElement> fbs, String directoryName) throws FileNotFoundException, UnsupportedEncodingException {
		//String text = "";
	    String fileName = im.getId().getName().toLowerCase() + ".infomodel";
	    
	    File file = new File(directoryName + "/" + fileName);
		PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
		
		writer.println("vortolang " + im.getVortolang() + "\n" +
				"namespace " + im.getId().getNamespace() + "\n" +
				"version " + im.getId().getVersion() + "\n" + 
				"displayname \"" + im.getId().getName() + "\"\n" +
				"description \"" + im.getDescription() + "\"");
		
		for (NamedElement fb : fbs) {
			writer.println("using " + VortoProfile.Model.getNamespace(fb)  + "." + fb.getName() + "; " + VortoProfile.Model.getVersion(fb));			
		}
		
		writer.println("\ninfomodel " + im.getId().getName() + " {\n" + 
				"\t" + "functionblocks {" + "\n");
		
		for (NamedElement ne : fbs) {
			String varName = Character.toLowerCase(ne.getName().charAt(0)) + ne.getName().substring(1);
			ArrayList<Comment> comments = new ArrayList<>();
			comments.addAll(ne.getOwnedComment());
			String comment = !comments.isEmpty() ? " \"" + comments.get(0).toString() + "\"" : "";
			writer.println("\t\t" + varName + " as " + ne.getName() + comment);
		}
		
		writer.println("\t}\n}");
		
		writer.close();
		//javax.swing.JOptionPane.showMessageDialog(null, text);
	}
	
	private void exportBlock(FunctionblockModel im, Element fbs, String directoryName) throws FileNotFoundException, UnsupportedEncodingException {
		//String text = "";
	    String fileName = im.getId().getName().toLowerCase() + ".fbmodel";

	    File file = new File(directoryName + "/" + fileName);
		PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
		
		writer.println("vortolang " + im.getVortolang() + "\n" +
				"namespace " + im.getId().getNamespace() + "\n" +
				"version " + im.getId().getVersion() + "\n" + 
				"displayname \"" + im.getId().getName() + "\"\n" +
				"description \"" + im.getDescription() + "\"");
		
		for (DirectedRelationship rel : fbs.get_directedRelationshipOfSource()) {
			for (Element target : rel.getTarget()) {
				if(VortoProfile.Model.getNamespace(target) != null) {
					writer.println("using " + VortoProfile.Model.getNamespace(target)  + "." + target.getHumanName().substring(target.getHumanType().length()+1) + "; " + VortoProfile.Model.getVersion(target));
				}
			}
		}
		
		writer.println("functionblock " + im.getId().getName() + " {");
		
		ArrayList<Element> statuses = new ArrayList<>();
		ArrayList<Element> operations = new ArrayList<>();
		ArrayList<Element> events = new ArrayList<>();
		
		for (Element element : fbs.getOwnedElement()) {
			if(element.getHumanName().contains("Value Property")) {
				statuses.add(element);
			}
			else if(element.getHumanName().contains("Operation")) {
				operations.add(element);
			}
			else if(element.getHumanName().contains("Class")) {
				events.add(element);
			}
		}
		
		if(!statuses.isEmpty()) {
			writer.println("\t" + "status {");
			for (Element status : statuses) {
				TypedElement te = (TypedElement)status;
				String multipl = ModelHelper.getMultiplicity((Property) te).charAt(0) == '1'? "mandatory" : "optional";
				/*String vartype = te.getType().getHumanName().substring(te.getType().getHumanType().length()+1);
				vartype = Character.toLowerCase(vartype.charAt(0)) + vartype.substring(1);*/
				String vartype = getPrimitiveType(te.getType());
				writer.println("\t\t" + multipl + " " + status.getHumanName().substring(status.getHumanType().length()+1) + " as " + vartype);
			}
			writer.println("\t}");
		}
		
		if(!operations.isEmpty()) {
			writer.println("\t" + "operations {");
			for (Element operation : operations) {
				String opName = operation.getHumanName().substring(operation.getHumanType().length()+1);
				ArrayList<Element> params = new ArrayList<>();
				Element returns = null;
				ArrayList<Comment> comments = new ArrayList<>();
				comments.addAll(operation.getOwnedComment());
				
				for (Element property : operation.getOwnedElement()) {
					if(property.getHumanName().contains("Parameter")) {
						if(property.getHumanName().contains("return")) {
							returns = property;
						}
						else {
							params.add(property);
						}
					}
				}
				
				String sparam = "";
				for (Element param : params) {
					TypedElement te = (TypedElement)param;
					/*String vartype = te.getType().getHumanName().substring(te.getType().getHumanType().length()+1);
					vartype = Character.toLowerCase(vartype.charAt(0)) + vartype.substring(1);*/
					String vartype = getPrimitiveType(te.getType());
					sparam += param.getHumanName().substring(param.getHumanType().length()+1) + " as " + vartype + ", ";
				}
				if(sparam.length()>0) {
					sparam = sparam.substring(0, sparam.length()-2);
				}
				
				String comment = !comments.isEmpty() ? " \"" + comments.get(0).toString() + "\"" : "";
				
				String sreturns = "";
				if(returns != null) {
					TypedElement te = (TypedElement)returns;
					/*String vartype = te.getType().getHumanName().substring(te.getType().getHumanType().length()+1);
					vartype = Character.toLowerCase(vartype.charAt(0)) + vartype.substring(1);*/
					String vartype = getPrimitiveType(te.getType());
					sreturns += "returns " + vartype;
				}
				
				writer.println("\t\t" + opName + "(" + sparam + ") " + sreturns + comment);
			}
			writer.println("\t}");
		}
		
		if(!events.isEmpty()) {
			writer.println("\t" + "events {");
			for (Element event : events) {
				writer.println("\t\t" + event.getHumanName().substring(event.getHumanType().length()+1) + " {");
				for (Element property : event.getOwnedElement()) {
					if(property.getHumanName().contains("Property")) {
						TypedElement te = (TypedElement)property;
						String multipl = ModelHelper.getMultiplicity((Property) te).charAt(0) == '1'? "mandatory" : "optional";
						/*String vartype = te.getType().getHumanName().substring(te.getType().getHumanType().length()+1);
						vartype = Character.toLowerCase(vartype.charAt(0)) + vartype.substring(1);*/
						String vartype = getPrimitiveType(te.getType());
						writer.println("\t\t\t" + multipl + " " + property.getHumanName().substring(property.getHumanType().length()+1) + " as " + vartype);
					}
				}
				writer.println("\t\t}");
			}
			writer.println("\t}");
		}
		writer.println("}");
		writer.close();
		//javax.swing.JOptionPane.showMessageDialog(null, text);
	}
	
	private void exportEntity(EntityModel em, Element absModel, String directoryName) throws FileNotFoundException, UnsupportedEncodingException {
		//String text = "";
		String fileName = em.getId().getName().toLowerCase() + ".type";
		
		File file = new File(directoryName + "/" + fileName);
		PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
		
		writer.println("vortolang " + em.getVortolang() + "\n" +
				"namespace " + em.getId().getNamespace() + "\n\n" +
				"version " + em.getId().getVersion() + "\n" + 
				"displayname \"" + em.getId().getName() + "\"\n" +
				"description \"" + em.getDescription() + "\"");
		
		writer.println("\nentity " + em.getId().getName() + " {");
		
		for (Element mp : absModel.getOwnedElement()) {
			if(!mp.getHumanName().equals(mp.getHumanType())) {
				TypedElement te = (TypedElement)mp;
				String multipl = ModelHelper.getMultiplicity((Property) te).charAt(0) == '1'? "mandatory" : "optional";
				/*String vartype = te.getType().getHumanName().substring(te.getType().getHumanType().length()+1);
				vartype = Character.toLowerCase(vartype.charAt(0)) + vartype.substring(1);*/
				String vartype = getPrimitiveType(te.getType());
				writer.println("\t" + multipl + " " + mp.getHumanName().substring(mp.getHumanType().length()+1) + " as " + vartype);
			}
		}
		
		writer.println("\n}");
		
		writer.close();
		//javax.swing.JOptionPane.showMessageDialog(null, text);
	}
	
	private void exportEnum(EnumModel em, Element absModel, String directoryName) throws FileNotFoundException, UnsupportedEncodingException {
		//String text = "";
		String fileName = em.getId().getName().toLowerCase() + ".type";
		
		File file = new File(directoryName + "/" + fileName);
		PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
		
		writer.println("vortolang " + em.getVortolang() + "\n" +
				"namespace " + em.getId().getNamespace() + "\n\n" +
				"version " + em.getId().getVersion() + "\n" + 
				"displayname \"" + em.getId().getName() + "\"\n" +
				"description \"" + em.getDescription() + "\"");
		
		writer.println("\n" + "enum " + em.getId().getName() + " {");
		
		for (Element mp : absModel.getOwnedElement()) {
			if(!mp.getHumanName().equals(mp.getHumanType())) {
				writer.println("\t" +  mp.getHumanName().substring(mp.getHumanType().length()+1));
			}
		}
		
		writer.println("\n}");
		
		writer.close();
		//javax.swing.JOptionPane.showMessageDialog(null, text);
	}

	void printAll(Element e, String directoryName) throws FileNotFoundException, UnsupportedEncodingException { //debug purposes only
		//String text = "";
		String fileName = e.getHumanName() + "_log.txt";
		
		File file = new File(directoryName + "/" + fileName);
		PrintWriter writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
		
		writer.println("get_representationText " + e.get_representationText());
		writer.println("getHumanName " + e.getHumanName());
		writer.println("getHumanType " + e.getHumanType());
		writer.println("getID " + e.getID());
		writer.println("getLocalID " + e.getLocalID());
		
		int get_activityPartitionOfRepresents = e.get_activityPartitionOfRepresents().size();
		writer.println("get_activityPartitionOfRepresents.size " + get_activityPartitionOfRepresents);
		if(get_activityPartitionOfRepresents > 0) {
			for (ActivityPartition ee : e.get_activityPartitionOfRepresents()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_commentOfAnnotatedElement = e.get_commentOfAnnotatedElement().size();
		writer.println("get_commentOfAnnotatedElement.size " + get_commentOfAnnotatedElement);
		if(get_commentOfAnnotatedElement > 0) {
			for (Comment ee : e.get_commentOfAnnotatedElement()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_constraintOfConstrainedElement = e.get_constraintOfConstrainedElement().size();
		writer.println("get_constraintOfConstrainedElement.size " + get_constraintOfConstrainedElement);
		if(get_constraintOfConstrainedElement > 0) {
			for (Constraint ee : e.get_constraintOfConstrainedElement()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_diagramOfContext = e.get_diagramOfContext().size();
		writer.println("get_diagramOfContext.size " + get_diagramOfContext);
		if(get_diagramOfContext > 0) {
			for (Diagram ee : e.get_diagramOfContext()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_directedRelationshipOfSource = e.get_directedRelationshipOfSource().size();
		writer.println("get_directedRelationshipOfSource.size " + get_directedRelationshipOfSource);
		if(get_directedRelationshipOfSource > 0) {
			for (DirectedRelationship ee : e.get_directedRelationshipOfSource()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getTarget");
				for (Element eee : ee.getTarget()) {
					writer.println("___________ getHumanName " + eee.getHumanName());
					writer.println("___________ getHumanType " + eee.getHumanType());
				}
				writer.println("_____ getSource");
				for (Element eee : ee.getSource()) {
					writer.println("___________ getHumanName " + eee.getHumanName());
					writer.println("___________ getHumanType " + eee.getHumanType());
				}
			}
		}
		
		int get_directedRelationshipOfTarget = e.get_directedRelationshipOfTarget().size();
		writer.println("get_directedRelationshipOfTarget.size " + get_directedRelationshipOfTarget);
		if(get_directedRelationshipOfTarget > 0) {
			for (DirectedRelationship ee : e.get_directedRelationshipOfTarget()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getTarget");
				for (Element eee : ee.getTarget()) {
					writer.println("___________ getHumanName " + eee.getHumanName());
					writer.println("___________ getHumanType " + eee.getHumanType());
				}
				writer.println("_____ getSource");
				for (Element eee : ee.getSource()) {
					writer.println("___________ getHumanName " + eee.getHumanName());
					writer.println("___________ getHumanType " + eee.getHumanType());
				}
			}
		}
		
		int get_elementOfSyncElement = e.get_elementOfSyncElement().size();
		writer.println("get_elementOfSyncElement.size" + get_elementOfSyncElement);
		if(get_elementOfSyncElement > 0) {
			for (Element ee : e.get_elementOfSyncElement()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_elementValueOfElement = e.get_elementValueOfElement().size();
		writer.println("get_elementValueOfElement.size" + get_elementValueOfElement);
		if(get_elementValueOfElement > 0) {
			for (ElementValue ee : e.get_elementValueOfElement()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int get_relationshipOfRelatedElement = e.get_relationshipOfRelatedElement().size();
		writer.println("get_relationshipOfRelatedElement.size" + get_relationshipOfRelatedElement);
		if(get_relationshipOfRelatedElement > 0) {
			for (Relationship ee : e.get_relationshipOfRelatedElement()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getRelatedElement");
				for (Element eee : ee.getRelatedElement()) {
					writer.println("___________ getHumanName " + eee.getHumanName());
					writer.println("___________ getHumanType " + eee.getHumanType());
				}
			}
		}
		
		int getMdExtensions = e.getMdExtensions().size();
		writer.println("getMdExtensions.size" + getMdExtensions);
		if(getMdExtensions > 0) {
			for (MDExtension ee : e.getMdExtensions()) {
				writer.println("_____ getHumanName " + ee.toString());
			}
		}
		
		int getOwnedComment = e.getOwnedComment().size();
		writer.println("getOwnedComment.size " + getOwnedComment);
		if(getOwnedComment > 0) {
			for (Comment ee : e.getOwnedComment()) {
				writer.println("_____ getHumanName " + ee.getHumanName());
				writer.println("_____ getHumanType " + ee.getHumanType());
			}
		}
		
		int getOwnedElement = e.getOwnedElement().size();
		writer.println("getOwnedElement.size " + getOwnedElement);
		if(getOwnedElement > 0) {
			for (Element ee : e.getOwnedElement()) {
				writer.println("_____ get_representationText " + ee.get_representationText());
				writer.println("_____ getHumanName " + ee.getHumanName());
				if(ee.getHumanName().contains("Value Property")) {
					TypedElement te = (TypedElement)ee;
					writer.println("___________ getHumanName " + te.getHumanName());
					writer.println("___________ getName " + te.getName());
					writer.println("___________ getType getName " + te.getType().getName());
					writer.println("___________ getType getHumanName " + te.getType().getHumanName()); //--- ez az
					writer.println("___________ getType getQualifiedName " + te.getType().getQualifiedName());
					writer.println("___________ getType " + te.getType());
					printAll(ee, directoryName);
				}
				else if(ee.getHumanName().contains("Operation")) {
					printAll(ee, directoryName);
				}
			}
		}
		
		writer.close();
		//javax.swing.JOptionPane.showMessageDialog(null, text);
	}
}
