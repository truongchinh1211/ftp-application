/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.UserBus;
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
public class SOTPCommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        if (arguments.length != 3) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.SYNTAX_ERROR,
                        "Syntax error.",
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(GOTPCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        UserBus userBus = new UserBus();
        String username = arguments[0];
        String password = arguments[1];
        String otp = arguments[2];
        boolean success = userBus.verifyOtp(username, password, otp);
        
        if (success) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.COMMAND_OK,
                        "OTP matches.",
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(GOTPCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.ACTION_FAILED,
                        "OTP doesn't match.",
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(GOTPCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
