/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.utils;

import java.util.Random;

/**
 *
 * @author tareq
 */
public class Utils {
    
     public static String getRandom(int length) {
        String subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int index = r.nextInt(subset.length());
            char c = subset.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }
    
}
