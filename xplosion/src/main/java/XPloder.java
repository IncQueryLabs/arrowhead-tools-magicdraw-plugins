import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Comparator;

public class XPloder {
    private BufferedReader reader;
    private int counter = 0;
    private int c;
    Path overDir;

    public void xPlode(Path source, Path target) throws IOException {
        if (Files.isReadable(source)) {
            File sourceFile = source.toFile();
            if (Files.isDirectory(target)) {
                String unName = sourceFile.getName();
                String name = unName.split(".")[0];
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
                FileWriter writer = new FileWriter(topXml.toFile());
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
                                //TODO first one special
                                //TODO write XInclude namespace
                                inPlode(topDir, writer); //after first
                        }
                    } else {
                        writer.write(c);
                    }
                    c = reader.read();
                }
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

    private void inPlode(Path parent, FileWriter backWriter) throws IOException {
        String name = Base64.getUrlEncoder().encodeToString(String.valueOf(counter++).getBytes());
        Path xml = parent.resolve(name + ".xml");
        Path dir = parent.resolve(name);
        backWriter.write("<xi:include href=\"" + overDir.relativize(xml).toString() + "\"/>");
        FileWriter writer = new FileWriter(xml.toFile());
        writer.write("<");
        writer.write(c);
        int b = c;
        c = reader.read();
        boolean me = true;
        while (true) {
            if (c == '>') {
                if (b == '/') {
                    writer.write(c);
                    return;
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
                    default:
                        if (!Files.exists(dir)) {
                            Files.createDirectory(dir);
                        }
                        inPlode(dir, writer);
                }
            } else {
                writer.write(c);
            }
            b = c;
            c = reader.read();
        }
    }

    private void writeTroughEndTag(FileWriter writer) throws IOException {
        writer.write("</");
        do {
            c = reader.read();
            writer.write(c);
        } while (c != '>');
    }

    private void writeTroughCdata(FileWriter writer) throws IOException {
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

    private void writeTroughComment(FileWriter writer) throws IOException {
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

    private void writeTroughProcessingInstructions(FileWriter writer) throws IOException {
        writer.write("<?");
        int b;
        do {
            b = c;
            c = reader.read();
            writer.write(c);
        } while (!(b == '?' && c == '>'));
    }
}
