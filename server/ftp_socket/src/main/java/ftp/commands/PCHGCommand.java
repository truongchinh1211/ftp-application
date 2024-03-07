/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.UserBus;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class PCHGCommand implements Command {

    private final UserBus userBus = new UserBus();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        String oldPassword = arguments[0];
        String newPassword = arguments[1];
        boolean oldPasswordMatch = userBus.checkLogin(session.getUsername(), oldPassword).equals(UserBus.LOGIN_SUCCESS_MSG);
        try {
            if (!oldPasswordMatch) {
                session.getSessionSocketUtils().respondCommandSocket(StatusCode.ACTION_FAILED, "Password cũ không chính xác.", commandSocketWriter);
                return;
            }

            boolean success = userBus.updatePasswordUser(session.getUsername(), newPassword);
            if (success) {
                session.getSessionSocketUtils().respondCommandSocket(StatusCode.COMMAND_OK, "Password changed.", commandSocketWriter);

            } else {
                session.getSessionSocketUtils().respondCommandSocket(StatusCode.ACTION_FAILED, "Forbidden.", commandSocketWriter);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
