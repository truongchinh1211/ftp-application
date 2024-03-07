/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socket;

import cipher.AESCipher;
import cipher.KeyAES;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lamanhhai
 */
public class CustomBufferedReader extends BufferedReader{
    
    public CustomBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        String messageAES = super.readLine();
        byte[] keyAES = KeyAES.getInstance().getKey();
        String message = "";
        try {
            message = AESCipher.decrypt(keyAES, messageAES);
        } catch (Exception ex) {
//            Logger.getLogger(CustomBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return message;
    } 
    
    public static void main(String[] args) {
        String messageAES = "Welcome client";
        byte[] keyAES = KeyAES.getInstance().getKey();
        String message = null;
        try {
            message = AESCipher.decrypt(keyAES, messageAES);
        } catch (Exception ex) {
            Logger.getLogger(CustomBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
