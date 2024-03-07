/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import bus.NormalFileBus;
import config.AppConfig;
import dao.UserDao;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class STORCommand implements Command {

    private final NormalFileBus normalFileBus = new NormalFileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private final UserDao userDao = new UserDao();
    private final FileBus fileBus = new FileBus();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {

        String inputFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);

        User user = userDao.getUserByUserName(session.getUsername());
        String path = ftpFileUtils.convertPublicPathToFtpPath(session.getWorkingDirAbsolutePath(), inputFilePath);

        if (path.startsWith(AppConfig.SERVER_FTP_ANON_PATH)) {
            if (!user.isAnonymous()) {
                try {
                    session.getSessionSocketUtils().respondCommandSocket(
                            StatusCode.FILE_ACTION_NOT_TAKEN,
                            "Anonymous disabled.",
                            commandSocketWriter
                    );
                    return;
                } catch (IOException ex) {
                    Logger.getLogger(STORCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            // Create file
            boolean fileCreationSuccess = normalFileBus.createNormalFile(path, session.getUsername());
            if (!fileCreationSuccess) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.",
                        commandSocketWriter
                );
                return;
            }
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_OK,
                    "Requested file action okay.",
                    commandSocketWriter
            );

            // Write to file
            Socket socket = session.getDataSocket().accept();
            normalFileBus.writeToNormalFile(
                    path,
                    session.getUsername(),
                    session.getSessionSocketUtils().readAll(
                            socket.getInputStream(),
                            session.getType().equals("I")
                    ),
                    session.getType()
            );
//            normalFileBus.writeToNormalFile(
//                    path,
//                    session.getUsername(),
//                    socket.getInputStream(),
//                    session.getType(),
//                    session.getAESKey()
//            );
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection.",
                    commandSocketWriter
            );

        } catch (IOException ex) {
            Logger.getLogger(RETRCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
