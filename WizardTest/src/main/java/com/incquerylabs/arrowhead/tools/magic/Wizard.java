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

public class Wizard {

    private static final Namespace ah = new Namespace("ah", "https://www.arrowhead.eu/interchange");
    private static final Namespace xInc = new Namespace("xi", "http://www.w3.org/2001/XInclude");
    private static final Namespace ec = new Namespace("ecore", "http://www.eclipse.org/emf/2002/Ecore");
    private static final Namespace xsi = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    Path root;
    Integer annotationSuffix = 1;
    QName refName = new QName("include", xInc);
    String href = "href";
    QName typeName = new QName("type", xsi);

    public void compartmentalize(Path source, Path target, String name) throws IOException, JAXBException {
        root = target;
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

    private void subCompartmentalize(EAnnotation a, Path parent, Element topParent, Path topPath) throws IOException {
        String name = "Annotation" + annotationSuffix;
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);

        Element ref = topParent.addElement(refName);
        ref.addAttribute(href, topPath.relativize(xml).toString());

        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement("eAnnotations");
        me.addAttribute("source", a.getSource());
        List<String> refers = a.getReferences();
        if (refers.size() > 0) {
            StringBuilder b = new StringBuilder();
            b.append(refers.get(0));
            for (int i = 1; i < refers.size(); ++i) {
                b.append(" ");
                b.append(refers.get(i));
            }
            me.addAttribute("references", b.toString());
        }

        for (EAnnotation an : a.getEAnnotations()){
            subCompartmentalize(an, dir, me, xml);
        }
        for (EStringToStringMapEntry ss : a.getDetails()) {
            subCompartmentalize(ss, dir, me, xml);
        }
        for (Object o : a.getContents()) {
            subCompartmentalize(o, dir, me, xml);
        }

        writeDocument(xml, doc);
    }

    private void  subCompartmentalize(EAttribute a, Path parent, Element topParent, Path topPath) throws IOException {
        String name = a.getName();
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);

        Element ref = topParent.addElement(refName);
        ref.addAttribute(href, topPath.relativize(xml).toString());

        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement("eStructuralFeatures");
        me.addAttribute(typeName, "ecore:EAttribute");
        me.addAttribute("name", name);
        me.addAttribute("ordered", a.getOrdered());
        me.addAttribute("unique", a.getUnique());
        me.addAttribute("lowerBound", a.getLowerBound());
        me.addAttribute("upperBound", a.getUpperBound());
        me.addAttribute("many", a.getMany());
        me.addAttribute("required", a.getMany());
        me.addAttribute("eType", a.getEType());
        me.addAttribute("changeable", a.getChangeable());
        me.addAttribute("volatile", a.getVolatile());
        me.addAttribute("transient", a.getTransient());
        me.addAttribute("defaultValueLiteral", a.getDefaultValueLiteral());
        me.addAttribute("defaultValue", a.getDefaultValue());
        me.addAttribute("unsettable", a.getUnsettable());
        me.addAttribute("derived", a.getDerived());
        me.addAttribute("iD", a.getID());
        me.addAttribute("eAttributeType", a.getEAttributeType());

        subCompartmentalize(a.getEGenericType(), dir, me, xml);
        for(EAnnotation an : a.getEAnnotations()){
            subCompartmentalize(an, dir, me, xml);
        }

        writeDocument(xml, doc);
    }

    private void subCompartmentalize(EStringToStringMapEntry ss, Path dir, Element me, Path xml) {
    }

    private void subCompartmentalize(EClassifier c, Path topDir, Element topParent, Path topPath) {

    }

    private void subCompartmentalize(EObject obj, Path parent, Element topParent, Path topPath) {

    }

    private void subCompartmentalize(Object o, Path dir, Element me, Path xml) {
        //TODO write with jaxb?
    }

    private void subCompartmentalize(EPackage ePackage, Path parent, Element topParent, Path topPath) throws IOException {
        String name = ePackage.getName();
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);

        Element ref = topParent.addElement(refName);
        ref.addAttribute(href, topPath.relativize(xml).toString());

        Document doc = DocumentHelper.createDocument();
        Element me;
        if (topPath.getParent().equals(root)) {
            me = doc.addElement(new QName("EPackage", ec));
        } else {
            me = doc.addElement("eSubpackages");
        }
        me.addAttribute("name", name);
        me.addAttribute("nsUri", ePackage.getNsURI());
        me.addAttribute("nsPrefix", ePackage.getNsPrefix());
        Object fac = ePackage.getEFactoryInstance();
        if (fac != null) {
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
        root.add(ec);
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
