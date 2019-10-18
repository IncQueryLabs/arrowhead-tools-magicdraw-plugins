package com.incquerylabs.arrowhead.gen;

import java.time.Instant;
import java.util.Random;

public class Ip4Generator {

    private static Random rand = new Random(Instant.now().hashCode());

    public static String good() {
        int num = 0;
        boolean good = false;
        while(!good){
            num = rand.nextInt();
            if(num >>> 28 != 0b1110){  //multicast
                if(num >>> 28 != 0b1111){ //reserved
                    if(num >>> 24 != 0){ //only valid as source address
                        if(num >> 24 != 127) { //localhost
                            if(num >>> 22 != 0b0110010010){ //shared address space
                                if(num >>> 17 != 0b110001101001000){ //for testing only
                                    if(num >>> 16 != 0b1010100111111110){ //link-local
                                        if(num >>> 8 != 0b110000000000000000000000){ //IANA assigned
                                            if(num >>> 8 != 0b1100000010110001100011){ //reserved
                                                if(num >>> 8 != 0b110000000000000000000010){ //TEST-NET-1
                                                    if(num >>> 8 != 0b110001100011001101100100){ //TEST-NET-2
                                                        if(num >>> 8 != 0b110010110000000001110001){ //TEST-NET-3
                                                            if(num != -2147483648){
                                                                good = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        int one = num >> 24;
        int two = (num >> 16) & 255;
        int three = (num >> 8) & 255;
        int four = num & 255;
        return one + "." + two + "." + three + "." + four + ".";
    }
}
