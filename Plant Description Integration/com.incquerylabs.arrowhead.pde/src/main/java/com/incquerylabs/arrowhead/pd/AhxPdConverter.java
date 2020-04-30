package com.incquerylabs.arrowhead.pd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.incquerylabs.arrowhead.pd.dto.ahx.AuthDto;
import com.incquerylabs.arrowhead.pd.dto.ahx.LocalCloudDto;
import com.incquerylabs.arrowhead.pd.dto.ahx.ServiceDto;
import com.incquerylabs.arrowhead.pd.dto.ahx.SystemDto;
import com.incquerylabs.arrowhead.pd.dto.pd.PdConnection;
import com.incquerylabs.arrowhead.pd.dto.pd.PdPort;
import com.incquerylabs.arrowhead.pd.dto.pd.PdSystem;
import com.incquerylabs.arrowhead.pd.dto.pd.PlantDescription;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AhxPdConverter {
	
	public static void fromAhx(File ahxFile, File pdFile) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<LocalCloudDto> clouds = gson
			.fromJson(new FileReader(ahxFile), new TypeToken<List<LocalCloudDto>>() {}.getType());
		LocalCloudDto cloud = clouds.get(0);
		PlantDescription pd = fromAhx(cloud);
		Files.deleteIfExists(pdFile.toPath());
		Files.createFile(pdFile.toPath());
		FileWriter writer = new FileWriter(pdFile);
		gson.toJson(pd, writer);
		writer.flush();
		writer.close();
	}
	
	public static PlantDescription fromAhx(LocalCloudDto cloud) {
		PlantDescription pd = new PlantDescription();
		pd.id = 1;
		pd.plantDescription = "Converted from AHX";
		Map<String, PdSystem> systems = new HashMap<>();
		for(SystemDto system : cloud.systems) {
			PdSystem s = new PdSystem();
			s.systemName = system.systemName;
			systems.put(s.systemName, s);
			pd.systems.add(s);
		}
		for(ServiceDto serviceDto : cloud.serviceRegistryEntries) {
			PdSystem system = systems.get(serviceDto.provider.systemName);
			for(String inter : serviceDto.interfaces) {
				PdPort port = new PdPort();
				port.portName = inter;
				port.serviceDefinition = serviceDto.serviceDefinition;
				system.ports.add(port);
			}
		}
		for(AuthDto authDto : cloud.authRules) {
			PdSystem consumer = systems.get(authDto.consumer);
			PdPort port = new PdPort();
			port.serviceDefinition = authDto.serviceDefinition;
			port.portName = authDto.interfaces.get(0);
			port.consumer = true;
			consumer.ports.add(port);
			PdConnection connection = new PdConnection();
			connection.consumer.systemName = consumer.systemName;
			connection.consumer.portName = port.portName;
			connection.producer.systemName = authDto.providers.get(0);
			connection.producer.portName = port.portName;
			pd.connections.add(connection);
		}
		return pd;
	}
	
	public static void fromPd(File[] pds, File ahxFile) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<PlantDescription> plantDescriptions = new ArrayList<>();
		for(File pdFile : pds) {
			plantDescriptions.add(gson.fromJson(new FileReader(pdFile), PlantDescription.class));
		}
		LocalCloudDto cloud = fromPd(plantDescriptions);
		List<LocalCloudDto> comm = new ArrayList<>();
		comm.add(cloud);
		Files.deleteIfExists(ahxFile.toPath());
		Files.createFile(ahxFile.toPath());
		FileWriter writer = new FileWriter(ahxFile);
		gson.toJson(comm, writer);
		writer.flush();
		writer.close();
	}
	
	public static LocalCloudDto fromPd(List<PlantDescription> pds) {
		LocalCloudDto cloud = new LocalCloudDto();
		HashMap<String, PdSystem> systemsByName = new HashMap<>();
		for(PlantDescription pd : pds) {
			for(PdSystem sys : pd.systems) {
				SystemDto tem = new SystemDto();
				tem.systemName = sys.systemName;
				cloud.systems.add(tem);
				Map<String, ServiceDto> services = new HashMap<>();
				for(PdPort port : sys.ports) {
					if(!port.consumer) {
						systemsByName.put(sys.systemName, sys);
						ServiceDto service = services.computeIfAbsent(port.serviceDefinition, s -> {
							ServiceDto n = new ServiceDto();
							n.serviceDefinition = s;
							n.provider = tem;
							return n;
						});
						service.interfaces.add(port.portName);
					}
				}
				cloud.serviceRegistryEntries.addAll(services.values());
			}
			for(PdConnection conn : pd.connections) {
				AuthDto auth = new AuthDto();
				auth.providers.add(conn.producer.systemName);
				auth.interfaces.add(conn.producer.portName);
				auth.consumer = conn.consumer.systemName;
				PdSystem sys = systemsByName.get(conn.producer.systemName);
				if(sys != null) {
					Optional<PdPort> p = sys.ports.stream().filter(port -> port.portName.equals(conn.producer.portName))
						.findAny();
					if(p.isPresent()) {
						auth.serviceDefinition = p.get().serviceDefinition;
						cloud.authRules.add(auth);
					}
				}
			}
		}
		return cloud;
	}
}
