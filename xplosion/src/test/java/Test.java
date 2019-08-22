import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        Path from = Paths.get("TMT.mdxml");
        Path to = Paths.get("Out");
        XPloder x = new XPloder();
        try {
            x.xPlode(from, to, "TMT");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
