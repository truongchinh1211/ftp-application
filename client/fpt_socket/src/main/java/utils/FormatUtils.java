/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author Son
 */
public class FormatUtils {
    public static String convertTimestamp(String timestamp){
        long time=Long.parseLong(timestamp);
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        boolean isToday= dateTime.toLocalDate().isEqual(LocalDateTime.now().toLocalDate());
        String pattern = isToday ? "HH:mm":"dd 'th 'MM, yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern,new Locale("vi"));
        return dateTime.format(formatter);
    }
    public static String convertBytes(String bytesToString) {
        long bytes = Long.parseLong(bytesToString);
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", (double) bytes / 1024);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", (double) bytes / (1024 * 1024));
        } else {
            return String.format("%.2f GB", (double) bytes / (1024 * 1024 * 1024));
        }
}
}
