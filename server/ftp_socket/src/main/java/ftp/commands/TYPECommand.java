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

public class TYPECommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            String type = arguments[0];
            if (type.equals("A") || type.equals("I")) {
                session.setType(arguments[0]);
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.COMMAND_OK,
                        "Command TYPE okay.",
                        commandSocketWriter
                );
            } else {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.COMMAND_UNRECOGNIZED,
                        "Command TYPE only accepts one argument, which is either \"I\" or \"A\"",
                        commandSocketWriter
                );
            }
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
