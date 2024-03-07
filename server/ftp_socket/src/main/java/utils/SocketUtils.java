/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author User
 */
public class SocketUtils {
    public static void writeLineAndFlush(String content, BufferedWriter writer) throws IOException {
        writer.append(content);
        writer.newLine();
        writer.flush();
    }

    public static void respondCommandSocket(int statusCode, String message, BufferedWriter socketWriter) throws IOException {
        writeLineAndFlush(statusCode + " " + message, socketWriter);
    }
}
