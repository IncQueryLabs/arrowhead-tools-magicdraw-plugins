//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.08.08 at 01:05:25 PM CEST 
//

package org.eclipse.emf.ecore.jaxbmodel;

import com.incquerylabs.arrowhead.tools.magic.Wizard;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EAttribute")
public class EAttribute extends EStructuralFeature {

    @XmlAttribute(name = "iD")
    protected String id;
    @XmlAttribute(name = "eAttributeType")
    @XmlSchemaType(name = "anyURI")
    protected String eAttributeType;

    @Override
    public void subCompartmentalize(Path parent, Element topParent, Path topPath) throws IOException {
        Path dir = parent.resolve(name);
        Files.createDirectory(dir);
        Path xml = parent.resolve(name + ".xml");
        Files.createFile(xml);

        Element ref = topParent.addElement(Wizard.REF);
        ref.addAttribute(Wizard.HREF, topPath.relativize(xml).toString());

        Document doc = DocumentHelper.createDocument();
        Element me = doc.addElement("eStructuralFeatures");
        me.addAttribute(Wizard.TYPE, "ecore:EAttribute");
        me.addAttribute("name", name);
        Wizard.writeETypedElementAttributes(me, this);
        Wizard.writeEStructuralFeatureAttributes(me, this);
        me.addAttribute("iD", id);
        me.addAttribute("eAttributeType", eAttributeType);

        for (EAnnotation an : eAnnotations) {
            an.subCompartmentalize(dir, me, xml);
        }
        if (eGenericType != null) {
            eGenericType.subCompartmentalize(dir, me, xml);
        }

        Wizard.writeDocument(xml, doc);
    }

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getEAttributeType() {
        return eAttributeType;
    }

    public void setEAttributeType(String value) {
        this.eAttributeType = value;
    }
}
