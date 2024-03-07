/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import ftp.FtpServer;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AUTHCommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        if (arguments[0].equals("TLS")) {
            try {
                System.out.println("431 Service is unavailable.");
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.SECURITY_METHOD_UNAVAILABLE,
                        "431 Service is unavailable.",
                        commandSocketWriter
                );
                return;
            } catch (IOException ex) {
                Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (arguments[0].equals("SSL")) {
            try {
                System.out.println("431 Service is unavailable.");
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.SECURITY_METHOD_UNAVAILABLE,
                        "431 Service is unavailable.",
                        commandSocketWriter
                );
                return;
            } catch (IOException ex) {
                Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.COMMAND_UNRECOGNIZED,
                    "500 Command is not recognised.",
                    commandSocketWriter
            );
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
