package com.incquerylabs.arrowhead.magicdraw.ahx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile;
import com.incquerylabs.arrowhead.magicdraw.dto.LocalCloudDto;
import com.incquerylabs.arrowhead.magicdraw.dto.SystemDto;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager;
import com.nomagic.magicdraw.openapi.uml.ReadOnlyElementException;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.task.ProgressStatus;
import com.nomagic.task.RunnableWithProgress;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.impl.ElementsFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ImportFromAhxWithProgress implements RunnableWithProgress {
	
	private File file;
	
	private Project project;
	private Package importsPackage;
	private ModelElementsManager elementsManager = ModelElementsManager.getInstance();
	
	private static final String IMPORTS_PACKAGE_NAME = "Imports";
	private static final String SESSION_NAME = "Import AHX file";
	
	public ImportFromAhxWithProgress(File file) {
		this.file = file;
	}
	
	@Override
	public void run(ProgressStatus progress) {
		try {
			progress.setMax(2);
			progress.setDescription("Loading file");
			
			Gson gson = new Gson();
			List<LocalCloudDto> clouds = gson
				.fromJson(new FileReader(file), new TypeToken<List<LocalCloudDto>>() {}.getType());
			LocalCloudDto cloud = clouds.get(0);
			project = Application.getInstance().getProject();
			
			progress.setDescription("Processing model");
			progress.increase();
			if(project != null) {
				SessionManager sessionManager = SessionManager.getInstance();
				sessionManager.createSession(project, SESSION_NAME);
				ElementsFactory factory = project.getElementsFactory();
				
				ArrowheadProfile profile = ArrowheadProfile.getInstance(project);
				Set<Element> bin = new HashSet<>(
					StereotypesHelper.getExtendedElementsIncludingDerived(profile.getDeployedSystem()));
				
				List<Element> cloudElements = StereotypesHelper.getExtendedElements(profile.getLocalCloud());
				Class cloudElement;
				if(cloudElements.isEmpty()) {
					cloudElement = factory.createClassInstance();
					cloudElement.setName("Local Cloud");
					elementsManager.addElement(cloudElement, getImportsPackage());
				} else {
					cloudElement = (Class) cloudElements.get(0);
				}
				
				List<SystemDto> unimportantSystems = new ArrayList<>();
				cloud.systems.parallelStream().forEach(sys -> {
					Optional<Property> first = cloudElement.getPart().stream()
						.filter(ArrowheadProfile::isDeployedSystem)
						.filter(inst -> inst.getName().toLowerCase().equals(sys.systemName.toLowerCase())).findFirst();
					if(first.isPresent()) {
						Property deployedSystem = first.get();
						bin.remove(deployedSystem);
						if(!sys.address.toLowerCase().equals(ArrowheadProfile.DeployedSystem.getAddress(deployedSystem).toLowerCase())) {
							ArrowheadProfile.DeployedSystem.setAddress(deployedSystem, sys.address);
						}
						if(!sys.port.equals(ArrowheadProfile.DeployedSystem.getPort(deployedSystem))) {
							ArrowheadProfile.DeployedSystem.setPort(deployedSystem, sys.port);
						}
					}
				});
				
				sessionManager.closeSession(project);
			}
		} catch(FileNotFoundException | ReadOnlyElementException e) {
			e.printStackTrace();
		}
	}
	
	private Package getImportsPackage() throws ReadOnlyElementException {
		
		if(importsPackage == null) {
			Collection<Package> subpackages = project.getPrimaryModel().getNestedPackage();
			for(Package pack : subpackages) {
				if(pack.getName().equals(IMPORTS_PACKAGE_NAME)) {
					importsPackage = pack;
				}
			}
			if(importsPackage != null) {
				for(Element eli : importsPackage.getOwnedElement()) {
					elementsManager.removeElement(eli);
				}
			} else {
				importsPackage = project.getElementsFactory().createPackageInstance();
				importsPackage.setName(IMPORTS_PACKAGE_NAME);
				elementsManager.addElement(importsPackage, project.getPrimaryModel());
			}
		}
		
		return importsPackage;
	}
}
