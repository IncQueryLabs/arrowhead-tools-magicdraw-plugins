import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class XPloder {
    private BufferedReader reader;
    private int counter = 0;
    private int c;
    private Path overDir;
    private int fc;

    public void xPlode(Path source, Path target, String name) throws IOException {
        if (Files.isReadable(source)) {
            File sourceFile = source.toFile();
            if (Files.isDirectory(target)) {
                Path topDir = target.resolve(name);
                Path topXml = target.resolve(name + ".xml.iqs");
                //delete previous version
                if (Files.exists(topDir)) {
                    //noinspection ResultOfMethodCallIgnored
                    Files.walk(topDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
                Files.deleteIfExists(topXml);
                Files.createFile(topXml);
                Files.createDirectories(topDir);
                overDir = target;
                reader = new BufferedReader(new FileReader(sourceFile));
                //TODO validate xml
                String first = reader.readLine();
                BufferedWriter writer = new BufferedWriter(new FileWriter(topXml.toFile()));
                writer.write(first);
                c = reader.read();
                while (c != -1) {
                    if (c == '<') {
                        c = reader.read();
                        switch (c) {
                            case '!':
                                c = reader.read();
                                if (c == '-') {
                                    writeTroughComment(writer);
                                } else {
                                    writeTroughCdata(writer);
                                }
                                break;
                            case '?':
                                writeTroughProcessingInstructions(writer);
                                break;
                            default:
                                writer.write("<");
                                writer.write(c);
                                c = reader.read();
                                while (c != ' ' && c != '\t' && c != '\n' && c != '\r' && c != '>') {
                                    writer.write(c);
                                    c = reader.read();
                                }
                                writer.write(" xmlns:xi=\"http://www.w3.org/2001/XInclude\"");
                                writer.write(c);
                                if (c != '>') {
                                    do {
                                        c = reader.read();
                                        writer.write(c);
                                    } while (c != '>');
                                }
                                int b = c;
                                c = reader.read();
                                boolean me = true;
                                while (me) {
                                    if (c == '>') {
                                        if (b == '/') {
                                            writer.write(c);
                                            me = false;
                                        }
                                    } else if (c == '<') {
                                        b = c;
                                        c = reader.read();
                                        switch (c) {
                                            case '!':
                                                c = reader.read();
                                                if (c == '-') {
                                                    writeTroughComment(writer);
                                                } else {
                                                    writeTroughCdata(writer);
                                                }
                                                break;
                                            case '?':
                                                writeTroughProcessingInstructions(writer);
                                                break;
                                            case '/':
                                                writeTroughEndTag(writer);
                                                me = false;
                                                break;
                                            default:
                                                inPlode(topDir, writer);
                                        }
                                    } else {
                                        writer.write(c);
                                    }
                                    if (me) {
                                        b = c;
                                        c = reader.read();
                                    }
                                }
                        }
                    } else {
                        writer.write(c);
                    }
                    c = reader.read();
                }
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
            if (!Files.exists(source)) {
                throw new FileNotFoundException("Source path does not point to a file.");
            } else {
                throw new FileNotFoundException("Source path points to an unreadable file.");
            }
        }
    }

    private void inPlode(Path parent, BufferedWriter backWriter) throws IOException {
        String name = getName();
        Path xml = parent.resolve(name + ".xml");
        Path dir = parent.resolve(name);
        backWriter.write("<xi:include href=\"" + overDir.relativize(xml).toString() + "\"/>");
        if(!Files.exists(xml)){
            Files.createFile(xml);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(xml.toFile()));
        writer.write("<");
        writer.write(c);
        int b = c;
        c = reader.read();
        boolean me = true;
        while (me) {
            if (c == '>') {
                writer.write(c);
                if (b == '/') {
                    me = false;
                }
            } else if (c == '<') {
                b = c;
                c = reader.read();
                switch (c) {
                    case '!':
                        c = reader.read();
                        if (c == '-') {
                            writeTroughComment(writer);
                        } else {
                            writeTroughCdata(writer);
                        }
                        break;
                    case '?':
                        writeTroughProcessingInstructions(writer);
                        break;
                    case '/':
                        writeTroughEndTag(writer);
                        me = false;
                        break;
                    default:
                        if (!Files.exists(dir)) {
                            Files.createDirectory(dir);
                        }
                        inPlode(dir, writer);
                }
            } else {
                writer.write(c);
            }
            if (me) {
                b = c;
                c = reader.read();
            }
        }
        writer.flush();
        writer.close();
    }

    private void writeTroughEndTag(BufferedWriter writer) throws IOException {
        writer.write("</");
        do {
            c = reader.read();
            writer.write(c);
        } while (c != '>');
    }

    private void writeTroughCdata(BufferedWriter writer) throws IOException {
        writer.write("<![");
        int a;
        int b = 'x';
        do {
            a = b;
            b = c;
            c = reader.read();
            writer.write(c);
        } while (!(a == ']' && b == ']' && c == '>'));
    }

    private void writeTroughComment(BufferedWriter writer) throws IOException {
        writer.write("<!-");
        int a;
        int b = 'x';
        do {
            a = b;
            b = c;
            c = reader.read();
            writer.write(c);
        } while (!(a == '-' && b == '-' && c == '>'));
    }

    private void writeTroughProcessingInstructions(BufferedWriter writer) throws IOException {
        writer.write("<?");
        int b;
        do {
            b = c;
            c = reader.read();
            writer.write(c);
        } while (!(b == '?' && c == '>'));
    }

    private String[] dec = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "-", "_"};
    private Set<String> forbidden = new HashSet<>(Arrays.asList("con prn aux nul com1 com2 com3 com4 com5 com6 com7 com8 com9 com0 lpt1 lpt2 lpt3 lpt4 lpt5 lpt6 lpt7 lpt8 lpt9 lpt0".split(" ")));

    private String getName() {
        int y = counter;
        List<String> temp = new ArrayList<>();
        while (counter >= 0) {
            int rem = counter % 64;
            counter = (counter - rem)/64;
            temp.add(dec[rem]);
            if (counter == 0) {
                counter = -1;
            }
        }
        counter = y + 1;
        StringBuilder b = new StringBuilder();
        for (int i = temp.size() - 1; i >= 0; --i) {
            b.append(temp.get(i));
        }
        String name = b.toString();
        if(forbidden.contains(name.toLowerCase())){
            name = "+" + fc++;
        }
        System.out.println(name);
        return name;
    }
}
