/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import cipher.AESKeyDecryptor;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class KEYCommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        String encryptedAESKey = arguments[0];
        try {
            String AESKey = AESKeyDecryptor.decryptKey(encryptedAESKey);
            System.out.println("AESKey: " + AESKey);
            session.setAESKey(AESKey.getBytes());
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.COMMAND_OK,
                    "Setup AES key successfully.",
                    commandSocketWriter
            );
            return;
        } catch (Exception ex) {
            Logger.getLogger(KEYCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ACTION_FAILED,
                    "Failed to setup AES key.",
                    commandSocketWriter
            );
        } catch (IOException ex) {
            Logger.getLogger(KEYCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
