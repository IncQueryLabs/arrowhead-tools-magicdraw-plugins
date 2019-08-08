package eu.arrowhead.interchange;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Element {
    Header header;
    Compartment compartment;
    Package pack;
    Object object;
    Attribute attribute;
    Association association;
}
