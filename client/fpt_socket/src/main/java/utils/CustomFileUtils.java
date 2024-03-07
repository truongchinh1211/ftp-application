/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Son
 */
public class CustomFileUtils {
    private String type;
    public static String determineType(File file){
        String filetype= file.getName().split("\\.")[1];
        return switch (filetype) {
            case "jpg", "png" -> "I";
            case "txt", "doc" -> "A";
            default -> "I";
        };
    }
    public static String determineType(String filePath){
        String fileName = Paths.get(filePath).getFileName().toString();
        String filetype= fileName.split("\\.")[1];
        return switch (filetype) {
            case "jpg", "png" -> "I";
            case "txt", "doc" -> "A";
            default -> "I";
        };
    }
}
