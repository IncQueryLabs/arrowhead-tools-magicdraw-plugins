package com.incquerylabs.arrowhead.magicdraw;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.net.URLClassLoader;

import com.nomagic.magicdraw.tests.MagicDrawApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MagicDrawApplication.class)
public class Tester {

    @Test
    public void dumbo() {
    	
        for (URL u:((URLClassLoader) getClass().getClassLoader()).getURLs()) {
        	System.out.println(u);
        }

        assertTrue(ArrowheadPlugin.isInitialized());
    }
}
