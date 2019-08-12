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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.emf.ecore.jaxbmodel.EAnnotation;
import org.eclipse.emf.ecore.jaxbmodel.EClassifier;
import org.eclipse.emf.ecore.jaxbmodel.EObject;
import org.eclipse.emf.ecore.jaxbmodel.EPackage;
import org.eclipse.emf.ecore.jaxbmodel.ObjectFactory;

public class Wizard {
	
	private static final Namespace ah = new Namespace("ah", "https://www.arrowhead.eu/interchange");
    private static final Namespace xInc = new Namespace("xi", "http://www.w3.org/2001/XInclude");
    private static final Namespace ec = new Namespace("ec", "http://www.eclipse.org/emf/2002/Ecore");
    
	
	public void compartmentalize(Path source, Path target, String name) throws IOException, JAXBException {
        if (Files.isReadable(source)) {
            File ecore = source.toFile();
            if (isEcore(ecore)) {
                if (Files.isDirectory(target)) {
                    Path topDir = target.resolve(name);
                    Path topXml = target.resolve(name + ".xml");
                    
                    //delete previous version
                    if (Files.exists(topDir)) {
                        Files.walk(topDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    }
                    Files.createDirectories(topDir);
                    Files.deleteIfExists(topXml);
                    Files.createFile(topXml);

                    JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
                    Unmarshaller reader = context.createUnmarshaller();
                    @SuppressWarnings("unchecked")
					JAXBElement<EPackage> elem = (JAXBElement<EPackage>) reader.unmarshal(new FileReader(source.toFile()));
                    if(elem == null) {
                    	System.out.println("fuck");
                    }
                    EPackage ePackage = elem.getValue();
                    
                    Document doc = DocumentHelper.createDocument();
                    Element root = doc.addElement(new QName("Arrowehead", ah));
                    addCommon(root);
                    
                    subCompartmentalize(ePackage, topDir, root, topXml);
                    writeDocument(topXml, doc);
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

    private void subCompartmentalize(EAnnotation a, Path topDir, Element topParent, Path topPath) {
    }

    private void subCompartmentalize(EClassifier c, Path topDir, Element topParent, Path topPath) {
    }

    @SuppressWarnings("unused")
	private void subCompartmentalize(EObject obj, Path parent, Element topParent, Path topPath) {

    }

    private void subCompartmentalize(EPackage ePackage, Path parent, Element topParent, Path topPath) throws IOException {
    	String name = ePackage.getName();
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);
        
        Element ref = topParent.addElement(new QName("include", xInc));
        ref.addAttribute("href", topPath.relativize(xml).toString());
        
        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement(new QName(name, ec));
        me.addAttribute("name", name);
        me.addAttribute("nsUri", ePackage.getNsURI());
        me.addAttribute("nsPrefix", ePackage.getNsPrefix());
        Object fac = ePackage.getEFactoryInstance();
        if(fac != null) {
            me.addAttribute("eFactoryInstance", fac.toString());
        }
        addCommon(me);

        for (EAnnotation a : ePackage.getEAnnotations()) {
            subCompartmentalize(a, dir, me, xml);
        }
        for (EPackage p : ePackage.getESubpackages()) {
            subCompartmentalize(p, dir, me, xml);
        }
        for (EClassifier c : ePackage.getEClassifiers()) {
            subCompartmentalize(c, dir, me, xml);
        }
        writeDocument(xml, doc);
    }

    //actual validation?
    private static boolean isEcore(File file) {
        return file.getName().endsWith(".ecore");
    }
    
    
    private static void addCommon(Element root) {
        root.add(ah);
        root.add(xInc);
    }

	private static final OutputFormat of = OutputFormat.createPrettyPrint();
	
    private static void writeDocument(Path target, Document document) throws IOException {
    	XMLWriter writer = new XMLWriter(new FileWriter(target.toAbsolutePath().toFile()), of);
    	writer.write(document);
    	writer.flush();
    	writer.close();
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
}
