package com.incquerylabs.arrowhead.gen;

import java.time.Instant;
import java.util.Random;

public class Ip4Generator {

    private static Random rand = new Random(Instant.now().hashCode());

    public static String good() {
        int num = rand.nextInt();

        if((num >> 22) == 511){
            num = rand.nextInt();
        }

        int one = num >> 24;
        int two = (num >> 16) & 255;
        int three = (num >> 8) & 255;
        int four = num & 255;



        String out = one + "." + two + "." + three + "." + four + ".";
        if(out.equals("0.0.0.0")){
            out = good();
        }
        return out;
    }
}
