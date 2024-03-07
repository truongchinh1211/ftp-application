/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import config.AppConfig;
import dao.UserDao;
import ftp.FtpFileUtils;
import ftp.FtpServer;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;


public class MLSDCommand implements Command {

    private final UserDao userDao = new UserDao();
    private final FileBus fileBus = new FileBus();

    public void anonymousExecute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            Socket socket = session.getDataSocket().accept();
            BufferedWriter dataSocketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            User user = userDao.getUserByUserName(session.getUsername());

            if (!user.isAnonymous()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.CLOSING_DATA_CONNECTION,
                        "Anonymous disabled.",
                        commandSocketWriter
                );
            }

            String fileData = fileBus.listAllAnonFilesInStringFormat(session.getWorkingDirAbsolutePath(), session.getUsername());
            session.getSessionSocketUtils().writeLineAndFlush(fileData, dataSocketWriter);

            socket.close();
            session.getDataSocket().close();
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection.",
                    commandSocketWriter
            );
        } catch (IOException ex) {
            Logger.getLogger(MLSDCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            System.out.println("User working directory: " + session.getWorkingDirAbsolutePath());
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ABOUT_TO_OPEN_DATA_CONNECTION,
                    "File status okay; about to open data connection.",
                    commandSocketWriter
            );
            
            FtpFileUtils ftpFileUtils = new FtpFileUtils();
            String ftpPath =ftpFileUtils.convertPublicPathToFtpPath(session.getWorkingDirAbsolutePath(), "");

            if (session.getWorkingDirAbsolutePath().startsWith(AppConfig.SERVER_FTP_ANON_PATH)) {
                anonymousExecute(arguments, session, commandSocketWriter);
                return;
            }

            Socket socket = session.getDataSocket().accept();
            BufferedWriter dataSocketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            File file = new File(session.getWorkingDirAbsolutePath());
            MLSDFormatter formatter = new MLSDFormatter();
            String fileData = formatter.listFormat(
                    file,
                    new DefaultMLSDFilter(session.getUsername()),
                    new DefaultFilePermissionGetter(session.getUsername()),
                    false
            );
            session.getSessionSocketUtils().writeLineAndFlush(fileData, dataSocketWriter);
            socket.close();
            session.getDataSocket().close();

            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection.",
                    commandSocketWriter
            );
        } catch (IOException ex) {
            Logger.getLogger(FtpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
