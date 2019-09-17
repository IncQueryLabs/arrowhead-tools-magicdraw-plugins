package com.incquerylabs.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;

public class MetaModel {

    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;

    public MetaModel() {

    }

    public MetaModel(Path ecore, Path genmodel, String extension) {
        try {
            this.add(ecore, genmodel, extension);
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void add(Path ecore, Path genmodel, String extension) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = registry.getExtensionToFactoryMap();
        map.put(extension, new XMIResourceFactoryImpl());

        //TODO compile & Find
        URLClassLoader cl = new URLClassLoader(new URL[]{}); //This will be filled at this point
        String packageImplName = "com.hal.nem.impl.HalPackageImpl"; //Will be the actual one.

        Class<?> pic = cl.loadClass(packageImplName);
        Method[] ms = pic.getMethods();
        for (int i = 0; i < ms.length; ++i) {
            Method m = ms[i];
            if(m.getName().equals("init")){
                m.invoke(null);
            }
        }
    }

    public Model getModel(URI source) {
        Model model = new Model();
        model.res = new ResourceSetImpl();
        Resource resource = model.res.createResource(source);
        return model;
    }

    public void expandModel(Model target, URI source) {
        Resource tas = target.res.createResource(source);
    }
}