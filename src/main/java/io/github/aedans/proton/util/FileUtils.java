package io.github.aedans.proton.util;

import java.io.File;

public final class FileUtils {
    private FileUtils() {
    }

    public static String extension(String string) {
        int index = string.lastIndexOf('.');
        if (index < 0)
            return "";
        else
            return string.substring(index + 1);
    }

    public static String extension(File file) {
        return extension(file.getName());
    }
}
