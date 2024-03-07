package ftp.commands;

import com.google.gson.Gson;
import dao.UserDao;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mapper.PublicUserMapper;
import model.User;
import payload.PublicUserInfo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author User
 */
public class PROFCommand implements Command {

    private UserDao userDao = new UserDao();
    private Gson gson = new Gson();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            User user = userDao.getUserByUserName(session.getUsername());
            PublicUserInfo profileDto = new PublicUserMapper().userToPublicUserInfo(user);
            String responseJson = gson.toJson(profileDto);

            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ABOUT_TO_OPEN_DATA_CONNECTION,
                    "About to open data connection",
                    commandSocketWriter
            );
            ServerSocket dataSocketServer = session.getDataSocket();
            Socket dataSocket = dataSocketServer.accept();
            BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));

            session.getSessionSocketUtils().writeLineAndFlush(responseJson, dataWriter);

            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection",
                    commandSocketWriter
            );

            dataWriter.close();
            dataSocketServer.close();
        } catch (IOException ex) {
            Logger.getLogger(PROFCommand.class.getName()).log(Level.SEVERE, null, ex);
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.ACTION_FAILED,
                        "Forbidden.",
                        commandSocketWriter
                );
            } catch (IOException ex1) {
                Logger.getLogger(PROFCommand.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

}
