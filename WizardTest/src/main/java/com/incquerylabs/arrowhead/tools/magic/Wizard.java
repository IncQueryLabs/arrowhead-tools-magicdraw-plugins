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
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.emf.ecore.jaxbmodel.*;

@SuppressWarnings("unused")
public class Wizard {

    public static final Namespace AH = new Namespace("ah", "https://www.arrowhead.eu/interchange");
    public static final Namespace XINC = new Namespace("xi", "http://www.w3.org/2001/XInclude");
    public static final Namespace EC = new Namespace("ecore", "http://www.eclipse.org/emf/2002/Ecore");
    public static final Namespace XSI = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    public static final QName REF = new QName("include", XINC);
    public static final String HREF = "href";
    public static final String N = "name";
    public static final QName TYPE = new QName("type", XSI);
    public static Integer objectSuffix = 1;
    public static Integer factorySuffix = 1;
    public static Integer annotationSuffix = 1;
    public static Integer gTypeSuffix = 1;
    public static Integer ssSuffix = 1;

    public void compartmentalize(Path source, Path target, String name) throws IOException, JAXBException {
        if (Files.isReadable(source)) {
            File ecore = source.toFile();
            if (isEcore(ecore)) {
                if (Files.isDirectory(target)) {
                    Path topDir = target.resolve(name);
                    Path topXml = target.resolve(name + ".xml");

                    //delete previous version
                    if (Files.exists(topDir)) {
                        //noinspection ResultOfMethodCallIgnored
                        Files.walk(topDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    }
                    Files.createDirectories(topDir);
                    Files.deleteIfExists(topXml);
                    Files.createFile(topXml);

                    JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
                    Unmarshaller reader = context.createUnmarshaller();
                    @SuppressWarnings("unchecked")
                    JAXBElement<EPackage> elem = (JAXBElement<EPackage>) reader.unmarshal(new FileReader(source.toFile()));
                    if (elem == null) {
                        System.out.println("fuck");
                    }
                    assert elem != null; // a valid ecore file wouldn't fail
                    EPackage ePackage = elem.getValue();

                    Document doc = DocumentHelper.createDocument();
                    Element root = doc.addElement(new QName("Arrowehead", AH));
                    addNamespaces(root);

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

    public static void subCompartmentalize(Object o, Path dir, Element me, Path xml) {
        //TODO write with jaxb?
    }

    public static void addListAttribute(Element element, String name, List<String> list) {
        if(list != null){
            if (list.size() > 0) {
                StringBuilder b = new StringBuilder();
                b.append(list.get(0));
                for (int i = 1; i < list.size(); ++i) {
                    b.append(" ");
                    b.append(list.get(i));
                }
                element.addAttribute(name, b.toString());
            }
        }
    }

    public static void writeEClassifierAttributes(Element element, EClassifier c) {
        element.addAttribute("instanceClassName", c.getInstanceClassName());
        element.addAttribute("instanceClass", c.getInstanceClass());
        element.addAttribute("defaultValue", c.getDefaultValue());
        element.addAttribute("instanceTypeName", c.getInstanceTypeName());
    }

    public static void writeETypedElementAttributes(Element element, ETypedElement typed) {
        element.addAttribute("ordered", typed.getOrdered());
        element.addAttribute("unique", typed.getUnique());
        element.addAttribute("lowerBound", typed.getLowerBound());
        element.addAttribute("upperBound", typed.getUpperBound());
        element.addAttribute("many", typed.getMany());
        element.addAttribute("required", typed.getMany());
        element.addAttribute("eType", typed.getEType());
    }

    public static void writeEStructuralFeatureAttributes(Element element, EStructuralFeature struct) {
        element.addAttribute("changeable", struct.getChangeable());
        element.addAttribute("volatile", struct.getVolatile());
        element.addAttribute("transient", struct.getTransient());
        element.addAttribute("defaultValueLiteral", struct.getDefaultValueLiteral());
        element.addAttribute("defaultValue", struct.getDefaultValue());
        element.addAttribute("unsettable", struct.getUnsettable());
        element.addAttribute("derived", struct.getDerived());
    }

    //actual validation?
    private static boolean isEcore(File file) {
        return file.getName().endsWith(".ecore");
    }

    public static void addNamespaces(Element root) {
        root.add(AH);
        root.add(XINC);
        root.add(EC);
        root.add(XSI);
    }

    private static final OutputFormat of = OutputFormat.createPrettyPrint();

    public static void writeDocument(Path target, Document document) throws IOException {
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
