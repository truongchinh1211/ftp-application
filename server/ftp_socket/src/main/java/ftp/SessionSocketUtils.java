/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

import cipher.AESCipher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author User
 */
public class SessionSocketUtils {

    private byte[] AESKey;

    public SessionSocketUtils(byte[] AESKey) {
        this.AESKey = AESKey;
    }

    public byte[] getAESKey() {
        return AESKey;
    }

    public void setAESKey(byte[] AESKey) {
        this.AESKey = AESKey;
    }

    public String readLine(BufferedReader reader) throws IOException {
        String encryptedMessage = reader.readLine();
        if (AESKey == null) {
            return encryptedMessage;
        }
        String message = "";
        try {
            message = AESCipher.decrypt(AESKey, encryptedMessage);
        } catch (Exception ex) {
            
        }
        return message;
    }

    public String readAll(InputStream inputStream, boolean readAsBase64) throws IOException {
        if (AESKey == null) {
            return readAsBase64
                    ? Base64.getEncoder().encodeToString(inputStream.readAllBytes())
                    : IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        String encryptedMessage = IOUtils.toString(inputStream, StandardCharsets.UTF_8).replaceFirst("[\n\r]+$", "");
        String message = "";
        try {
            message = AESCipher.decrypt(AESKey, encryptedMessage);
        } catch (Exception ex) {
            Logger.getLogger(BufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public void writeLineAndFlush(String content, BufferedWriter writer) throws IOException {
        try {
            String toSendContent = content;
            if (AESKey != null) {
                toSendContent = AESCipher.encrypt(AESKey, content);
                System.out.println("Content: " + AESCipher.decrypt(AESKey, toSendContent));
            }
            writer.append(toSendContent);
            writer.newLine();
            writer.flush();
        } catch (Exception ex) {
            Logger.getLogger(SessionSocketUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void respondCommandSocket(int statusCode, String message, BufferedWriter socketWriter) throws IOException {
        writeLineAndFlush(statusCode + " " + message, socketWriter);
    }
}
