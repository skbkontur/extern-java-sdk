package ru.kontur.extern_api.sdk.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class YAStringUtils {

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String center(String string, int width, char fillWith) {
        if (string.length() >= width) {
            return string;
        }

        int padding = (width - string.length()) / 2;
        String sPadding = new String(new char[padding]).replace('\0', fillWith);
        return sPadding + string + sPadding;
    }

    public static String joinIfExists(CharSequence delim, String... strings) {
        return String.join(delim, Arrays
                .stream(strings)
                .filter(s -> !isNullOrEmpty(s))
                .collect(Collectors.toList()));
    }

}
