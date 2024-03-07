/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.UserBus;
import com.google.gson.Gson;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import payload.PublicUserInfo;
import payload.response.UserDetailResponse;

/**
 *
 * @author User
 */
public class CHDTCommand implements Command {

    private final Gson gson = new Gson();
    private final UserBus userBus = new UserBus();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ABOUT_TO_OPEN_DATA_CONNECTION,
                    "About to open data connection",
                    commandSocketWriter);
            Socket dataSocket = session.getDataSocket().accept();

            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            String json = session.getSessionSocketUtils().readLine(dataReader);
            UserDetailResponse userInfo = gson.fromJson(json, UserDetailResponse.class);
            boolean success = userBus.saveUserDetail(userInfo);

            if (success) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.COMMAND_OK,
                        "About to close data connection",
                        commandSocketWriter);
            } else {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.ACTION_FAILED,
                        "Forbidden.",
                        commandSocketWriter);
            }
            
            dataSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(CHDTCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
