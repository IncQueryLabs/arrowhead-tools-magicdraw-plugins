import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
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
