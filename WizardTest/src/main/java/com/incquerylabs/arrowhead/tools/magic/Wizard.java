package com.incquerylabs.arrowhead.tools.magic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.emf.ecore.jaxbmodel.EAnnotation;
import org.eclipse.emf.ecore.jaxbmodel.EClassifier;
import org.eclipse.emf.ecore.jaxbmodel.EObject;
import org.eclipse.emf.ecore.jaxbmodel.EPackage;

public class Wizard {

	private static final NamespaceContext AH_NAMESPACE = new ArrowheadNamespace();
	private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	Path target;
	
	public void compartmentalize(Path source, Path target, String name) throws IOException, JAXBException, XMLStreamException {
        this.target = target;
        if (Files.isReadable(source)) {
            File ecore = source.toFile();
            if (isEcore(ecore)) {
                if (Files.isDirectory(target)) {
                    Path topDir = target.resolve(name);
                    Path topXml = target.resolve(name + ".xml");
                    if (Files.exists(topDir)) {
                        Files.walk(topDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    }
                    Files.createDirectories(topDir);
                    Files.deleteIfExists(topXml);
                    Files.createFile(topXml);

                    JAXBContext context = JAXBContext.newInstance(EPackage.class);
                    Unmarshaller reader = context.createUnmarshaller();
                    EPackage ePackage = (EPackage) reader.unmarshal(new FileReader(source.toFile()));
                    XMLStreamWriter writer = outputFactory.createXMLStreamWriter(new FileWriter(topXml.toFile()));

                    writer.setNamespaceContext(AH_NAMESPACE);
                    writer.writeStartDocument();
                    //TODO write compartment into topXML (including namespaces
                    subCompartmentalize(ePackage, topDir, writer);
                    //TODO write my end tags
                    writer.writeEndDocument();
                    writer.flush();
                    writer.close();
                } else {
                    if (Files.exists(target)) {
                        throw new FileNotFoundException("Target is a non-directory file.");
                    } else {
                        throw new FileNotFoundException("Target directory does not exist.");
                    }
                }
            } else {
                throw new FileNotFoundException("Source file is not a .ecore file.");
            }
        } else {
            if (!Files.exists(source)) {
                throw new FileNotFoundException("Source path does not point to a file.");
            } else {
                throw new FileNotFoundException("Source path points to an unreadable file.");
            }
        }
    }

    private void subCompartmentalize(EAnnotation a, Path topDir, XMLStreamWriter writer) {
    }

    private void subCompartmentalize(EClassifier c, Path topDir, XMLStreamWriter writer) {
    }

    private void subCompartmentalize(EObject obj, Path parent, XMLStreamWriter writer) {

    }

    private void subCompartmentalize(EPackage ePackage, Path parent, XMLStreamWriter writer) throws IOException, XMLStreamException {
        String name = ePackage.getName();
        if(name == null){
            name = "<>";
        }
        String temp = name;
        for(int i = 2; !Files.exists(parent.resolve(name)); ++i){
            temp = name + i;
        }
        name = temp;
        Path dir = parent.resolve(name);
        Path xml = parent.resolve(name + ".xml");

        //write this package's ref into the higher xml
        writer.writeStartElement("EPackage");
        writer.writeAttribute("ref", parent.relativize(xml).toString());
        writer.writeEndElement();
        writer.flush();

        XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(new FileWriter(xml.toFile()));

        streamWriter.setNamespaceContext(AH_NAMESPACE);
        streamWriter.writeStartDocument();
        //TODO write my root, my header, the ePackage start tags &

        for (EAnnotation a : ePackage.getEAnnotations()) {
            subCompartmentalize(a, dir, streamWriter);
        }
        for (EPackage p : ePackage.getESubpackages()) {
            subCompartmentalize(p, dir, streamWriter);
        }
        for (EClassifier c : ePackage.getEClassifiers()) {
            subCompartmentalize(c, dir, streamWriter);
        }

        //TODO write my end tags
        streamWriter.writeEndDocument();
        streamWriter.flush();
        streamWriter.close();
    }

    //actual validation?
    private static boolean isEcore(File file) {
        String[] temp = file.getName().split(".");
        return temp[temp.length - 1].equals("ecore");
    }
	
	public static void main(String[] args) {
		Path source = Paths.get("model/testProj.ecore");
		Path target = Paths.get("Out");
		Wizard ted = new Wizard();
		try {
			ted.compartmentalize(source, target, "WizTest");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	//TODO finish
	private static class ArrowheadNamespace implements NamespaceContext{

        @Override
        public String getNamespaceURI(String prefix) {
        	if(prefix != null) {
        		switch (prefix) {
    			case "ah":
    				return "https://www.arrowhead.eu/interchange";
    			default:
    				return XMLConstants.NULL_NS_URI;
    			}
        	} else {
        		throw new IllegalArgumentException("No prefix provided!");
        	}
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return "ah";
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            return Set.of("ah").iterator();
        }
    }
}
