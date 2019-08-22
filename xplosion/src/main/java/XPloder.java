import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class XPloder {

    private void xPlode(Path source, Path target) throws IOException, XMLStreamException {
        if (Files.isReadable(source)) {
            File sourceFile = source.toFile();
            if (Files.isDirectory(target)) {
                String name = sourceFile.getName();
                Path overDir = target.resolve(name + ".dir");
                //delete previous version
                if (Files.exists(overDir)) {
                    //noinspection ResultOfMethodCallIgnored
                    Files.walk(overDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
                Files.createDirectories(overDir);
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileReader(sourceFile));
                while (reader.hasNext()){

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

    private void inPlode(Path parent, XMLStreamReader reader, XMLStreamWriter writer){

    }

    public static void main(String[] args) {
        Path from = Paths.get("fr");
        Path to = Paths.get("to");
        XPloder x = new XPloder();
        try {
            x.xPlode(from, to);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
