package com.web.tip.util;

import java.util.Random;

public class GeneratePK {

    public String generateKey(){
        String result = "";
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        for (int i = 0 ; i < 13 ; i++)
            result += rnd.nextInt(10);
        return result;
    }

}
