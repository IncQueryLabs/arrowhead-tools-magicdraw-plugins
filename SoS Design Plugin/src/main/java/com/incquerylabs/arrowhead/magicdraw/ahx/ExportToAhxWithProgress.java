package com.incquerylabs.arrowhead.magicdraw.ahx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile;
import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile.DeployedSystem;
import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile.IDD;
import com.incquerylabs.arrowhead.magicdraw.auto.ArrowheadProfile.SecurityKindEnum;
import com.incquerylabs.arrowhead.magicdraw.dto.AuthDto;
import com.incquerylabs.arrowhead.magicdraw.dto.LocalCloudDto;
import com.incquerylabs.arrowhead.magicdraw.dto.ServiceDto;
import com.incquerylabs.arrowhead.magicdraw.dto.SystemDto;
import com.nomagic.task.ProgressStatus;
import com.nomagic.task.RunnableWithProgress;
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectableElement;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectorEnd;
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class ExportToAhxWithProgress implements RunnableWithProgress {
	
	private final Class cloudElement;
	private final File chosen;
	
	public ExportToAhxWithProgress(Class cloudElement, File chosen) {
		this.cloudElement = cloudElement;
		this.chosen = chosen;
	}
	
	@Override
	public void run(ProgressStatus progress) {
		progress.setMax(2);
		progress.setDescription("Processing model");
		LocalCloudDto dto = new LocalCloudDto();
		
		cloudElement.getPart().parallelStream().filter(ArrowheadProfile::isDeployedSystem)
			.filter(property -> ArrowheadProfile.isSysDD(property.getType())).forEach(systemInstance -> {
			SystemDto systemDto = new SystemDto();
			systemDto.systemName = systemInstance.getName();
			systemDto.address = DeployedSystem.getAddress(systemInstance);
			systemDto.port = DeployedSystem.getPort(systemInstance);
			systemDto.authenticationInfo = DeployedSystem.getAuthenticationInfo(systemInstance);
			dto.systems.add(systemDto);
			Class sysDD = (Class) systemInstance.getType();
			Map<Class, String> map = sysDD.getMember().parallelStream()
				.filter(elem -> Port.class.isAssignableFrom(elem.getClass())).map(f -> (Port) f)
				.filter(port -> !port.isConjugated()).filter(port -> ArrowheadProfile.isIDD(port.getType()))
				.filter(port -> {
					boolean subsetted = false;
					Iterator<Dependency> i = port.getSupplierDependency().iterator();
					while(!subsetted && i.hasNext()) {
						Dependency d = i.next();
						if(ArrowheadProfile.isSubsetOf(d)) {
							subsetted = true;
						}
					}
					return !subsetted;
				}).collect(Collectors.toMap(port -> (Class) port.getType(), port -> {
					Class serviceRealization = (Class) port.getType();
					Class serviceDefinition = getServiceDefinition(serviceRealization);
					return serviceDefinition.getName(); //not null, filtered above
				}));
			Map<String, List<Class>> reverseMap = new HashMap<>();
			for(Class key : map.keySet()) {
				String value = map.get(key);
				List<Class> list = reverseMap.computeIfAbsent(value, s -> new ArrayList<>());
				list.add(key);
			}
			for(String serviceDefinition : reverseMap.keySet()) {
				ServiceDto serviceDto = new ServiceDto();
				serviceDto.serviceDefinition = serviceDefinition;
				serviceDto.provider = systemDto;
				//serviceDto.endOfValidity = ArrowheadProfile.DeployedSystem.getEndOfValidity(systemInstance);
				
				List<Class> interfaces = reverseMap.get(serviceDefinition);
				serviceDto.secure = IDD.getSecurity(interfaces.get(0));
				for(Class intface : interfaces) {
					String name = getInterfaceString(intface);
					serviceDto.interfaces.add(name);
				}
				ArrowheadProfile.DeployedSystem.getMetadata(systemInstance).forEach(e -> {
					if(ArrowheadProfile.isMetaDataEntry(e)) {
						Class metaDataEntry = (Class) e;
						serviceDto.metadata
							.put(metaDataEntry.getName(), ArrowheadProfile.MetaDataEntry.getData(metaDataEntry));
					}
				});
				dto.serviceRegistryEntries.add(serviceDto);
			}
		});
		cloudElement.getFeature().parallelStream()
			.filter(feature -> Connector.class.isAssignableFrom(feature.getClass())).map(feature -> (Connector) feature)
			.filter(connector -> {
				Iterator<ConnectorEnd> ends = connector.getEnd().iterator();
				boolean allEnds = ends.hasNext(); //checks if it has ends at all
				while(allEnds && ends.hasNext()) {
					ConnectorEnd end = ends.next();
					allEnds = ArrowheadProfile.isDeployedSystem(end.getPartWithPort());
				}
				return allEnds;
			}).forEach(connector -> {
			AuthDto authDto = new AuthDto();
			for(ConnectorEnd end : connector.getEnd()) {
				ConnectableElement connectableElement = end.getRole();
				if(connectableElement != null) {
					if(Port.class.isAssignableFrom(connectableElement.getClass())) {
						Port port = (Port) connectableElement;
						Property part = Objects.requireNonNull(end.getPartWithPort());
						
						if(!port.isConjugated()) {
							Type type = port.getType();
							authDto.interfaces.add(getInterfaceString(type));
							authDto.serviceDefinition = getServiceDefinition((Class) type).getName();
							authDto.providers.add(part.getName());
						} else {
							authDto.consumer = part.getName();
						}
					}
				}
			}
			dto.authRules.add(authDto);
		});
		progress.increase();
		progress.setDescription("Writing File");
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter(chosen);
			List<LocalCloudDto> communicationError = new ArrayList<>();
			communicationError.add(dto);
			gson.toJson(communicationError, writer);
			writer.flush();
			writer.close();
			progress.increase();
		} catch(IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private String getInterfaceString(Type intface) {
		String protocol = ArrowheadProfile.IDD.getProtocol(intface);
		if(protocol == null) {
			protocol = ExportToAhxAction.DEFAULT_PROTOCOL;
		} else {
			protocol = protocol.replaceAll("\\s", "").toUpperCase();
		}
		SecurityKindEnum sc = ArrowheadProfile.IDD.getSecurity(intface);
		String security;
		if(sc == null) {
			security = ExportToAhxAction.DEFAULT_SECURITY;
		} else {
			switch(sc) {
				case TOKEN:
				case CERTIFICATE:
					security = ExportToAhxAction.SECURE;
					break;
				case NOT_SECURE:
					security = ExportToAhxAction.INSECURE;
					break;
				default:
					security = ExportToAhxAction.DEFAULT_SECURITY;
					break;
			}
		}
		String format = ArrowheadProfile.IDD.getEncoding(intface);
		if(format == null) {
			format = ExportToAhxAction.DEFAULT_FORMAT;
		} else {
			format = format.replaceAll("\\s", "").toUpperCase();
		}
		return protocol + "-" + security + "-" + format;
	}
	
	private Class getServiceDefinition(Class serviceRealization) {
		
		Class serviceDefinition = null;
		if(ArrowheadProfile.isSD(serviceRealization)) {
			serviceDefinition = serviceRealization;
		} else {
			Iterator<Class> i = serviceRealization.getSuperClass().iterator();
			while(serviceDefinition == null && i.hasNext()) {
				Class sup = i.next();
				serviceDefinition = getServiceDefinition(sup);
			}
		}
		return serviceDefinition;
	}
	
}
