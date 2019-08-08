package com.incquerylabs.arrowhead.compartmentaizer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;


public class Wizard {
    private static final Set<String> pack = Set.of();
    private static final Set<String> object = Set.of();
    private static final Set<String> attribute = Set.of();
    private static final Set<String> relation = Set.of();

    public static void compartmentalize(Path source, Path target) throws FileNotFoundException, XMLStreamException {
        if(Files.isReadable(source)){
            File ecore = source.toFile();
            if(fileFormatEquals(ecore, "ecore")){
                if(Files.isDirectory(target)) {
                    //validation?
                    XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(ecore));
                    //TODO: here crate the compartment here, copy tool specific information to folder one layer below
                    subCompartmentalize(reader, target);

                } else {
                    if(Files.exists(target)){
                        throw new FileNotFoundException("Target is a non-directory file.");
                    } else {
                        throw new FileNotFoundException("Target directory does not exist.");
                    }
                }
            } else {
                throw new FileNotFoundException("Source file is not a .ecore file.");
            }
        } else {
            if(!Files.exists(source)){
                throw new FileNotFoundException("Source path does not point to a file.");
            } else {
                throw new FileNotFoundException("Source path points to an unreadable file.");
            }
        }
    }

    private static void subCompartmentalize(XMLStreamReader reader, Path parent) throws XMLStreamException {
        if(reader.hasNext()){
            reader.next();
            while (!reader.isEndElement()){
                //TODO fé
            }
        }
    }

    private static boolean fileFormatEquals(File file, String string){
        String[] temp = file.getName().split(".");
        return temp[temp.length-1].equals(string);
    }
}
