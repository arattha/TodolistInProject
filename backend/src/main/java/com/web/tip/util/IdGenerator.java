package com.web.tip.util;

import java.util.Random;

public class IdGenerator {
    private static Random random = new Random();

    public String generateId(){
        StringBuilder id = new StringBuilder();
        for(int i = 0; i < 13; i++){
            id.append(random.nextInt(10));
        }
        return id.toString();
    }
}
