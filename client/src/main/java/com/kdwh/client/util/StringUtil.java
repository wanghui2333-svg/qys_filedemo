package com.kdwh.client.util;

import org.springframework.util.StringUtils;

public class StringUtil {
    public static String generateString(int StringLength){
        String string = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < StringLength; i++) {
            int index = (int) Math.floor(Math.random() * string.length());//向下取整0-25
            sb.append(string.charAt(index));
        }
        return sb.toString();
    }


}
