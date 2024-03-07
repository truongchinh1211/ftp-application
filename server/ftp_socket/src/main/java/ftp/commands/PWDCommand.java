/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import ftp.FtpServer;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import ftp.commands.Command;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class PWDCommand implements Command {
    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CURRENT_WORKING_DIRECTORY,
                    String.format("\"%s\" is current directory.", session.getWorkingDir()),
                    commandSocketWriter);
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
