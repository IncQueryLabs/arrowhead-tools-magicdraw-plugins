import com.incquerylabs.emf.MetaModel;
import com.incquerylabs.emf.Model;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        new Test().start();
    }

    private void start() {
        Path ecore = Paths.get("src/test/resources/model.ecore");
        Path genmodel = Paths.get("src/test/resources/model.genmodel");
        Path model = Paths.get("src/test/resources/example.cyberphysicalsystem");
        MetaModel cps = new MetaModel(ecore, genmodel);
        Model example = cps.getModel(model);
        example.getContents();
    }
}
