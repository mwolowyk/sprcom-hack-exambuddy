package com.springer.hack.exambuddy.utils;

import org.apache.commons.lang3.StringUtils;

public class Utils {
    public static String normalizeString(Object val){
        return StringUtils.normalizeSpace(String.valueOf(val)).replace("\n", " ");
    }

}
