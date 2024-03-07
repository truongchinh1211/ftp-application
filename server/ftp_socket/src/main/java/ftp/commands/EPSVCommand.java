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
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EPSVCommand implements Command {

    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            // Listen on any free port
            ServerSocket dataSocket = new ServerSocket(0);
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ENTERED_EXTENDED_PASSIVE_MODE,
                    String.format("Entering Extended Passive Mode (|||%s|)", dataSocket.getLocalPort()), 
                    commandSocketWriter
            );
            session.setDataSocket(dataSocket);
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
