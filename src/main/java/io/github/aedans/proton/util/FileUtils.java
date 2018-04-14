package io.github.aedans.proton.util;

import java.io.File;

public final class FileUtils {
    private FileUtils() {
    }

    public static String extension(File file) {
        int index = file.getName().lastIndexOf('.');
        if (index < 0)
            return "";
        else
            return file.getName().substring(index + 1);
    }
}
