package com.example.EcommerceApp.utils;

import java.util.Optional;

public class FileUtils {
    static public Optional<String> getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0)
            return Optional.of(filename.substring(i+1));
        return Optional.empty();
    }
}
