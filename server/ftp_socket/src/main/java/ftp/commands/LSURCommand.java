/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import com.google.gson.Gson;
import ftp.FilePermissionWithUser;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author User
 */
public class LSURCommand implements Command {

    private FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private FileBus fileBus = new FileBus();
    private Gson gson = new Gson();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            String fileName = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
            String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    fileName
            );

            File file = new File(filePath);
            if (!file.exists()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.ACTION_FAILED,
                        "File does not exist",
                        commandSocketWriter
                );
                return;
            }

            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.ABOUT_TO_OPEN_DATA_CONNECTION,
                    "About to open data connection",
                    commandSocketWriter
            );

            List<FilePermissionWithUser> sharedUsersPermissions = fileBus.getSharedUsersPermissions(filePath);
            String responseJson = gson.toJson(sharedUsersPermissions);

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
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
