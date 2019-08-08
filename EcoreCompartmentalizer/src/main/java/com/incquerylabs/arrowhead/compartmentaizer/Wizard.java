package com.incquerylabs.arrowhead.compartmentaizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;


public class Wizard {
    //TODO reformat to classes
    private static final Set<String> pack = Set.of();
    private static final Set<String> object = Set.of();
    private static final Set<String> attribute = Set.of();
    private static final Set<String> relation = Set.of();

    public static void compartmentalize(Path source, Path target) throws FileNotFoundException {
        if (Files.isReadable(source)) {
            File ecore = source.toFile();
            if (fileFormatEquals(ecore, "ecore")) {
                if (Files.isDirectory(target)) {
                    //TODO here comes the magic
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

    private static boolean fileFormatEquals(File file, String string) {
        String[] temp = file.getName().split(".");
        return temp[temp.length - 1].equals(string);
    }
}
