/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.UserBus;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class REGCommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ABOUT_TO_OPEN_DATA_CONNECTION,
                    "Waiting for register info",
                    commandSocketWriter
            );

            Socket dataSocket = session.getDataSocket().accept();
            UserBus userBus = new UserBus();

            String jsonData = session.getSessionSocketUtils().readAll(dataSocket.getInputStream(), false);
            boolean success = userBus.registerUser(jsonData);
            if (success) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.CLOSING_DATA_CONNECTION,
                        "Account created successfully.",
                        commandSocketWriter
                );
            } else {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.ACTION_FAILED,
                        "Registration failed.",
                        commandSocketWriter
                );
            }
        } catch (IOException ex) {
            Logger.getLogger(REGCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
