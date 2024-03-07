/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import ftp.DirectoryPermission;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CWDCommand implements Command {

    private final FileBus fileBus = new FileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            String newDir = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
            String newDirPath = ftpFileUtils.convertPublicPathToFtpPath(session.getWorkingDirAbsolutePath(), newDir);
            DirectoryPermission directoryPermission = (DirectoryPermission) fileBus.getFilePermission(
                    newDirPath,
                    session.getUsername(),
                    FileBus.DIRECTORY_TYPE
            );
            if (directoryPermission.isReadable()) {
                session.changeWorkingDir(newDir);
                session.getSessionSocketUtils().respondCommandSocket(StatusCode.FILE_ACTION_OK,
                        "Okay.",
                        commandSocketWriter
                );
            } else {
                session.getSessionSocketUtils().respondCommandSocket(StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.",
                        commandSocketWriter
                );
            }

        } catch (IOException ex) {
            Logger.getLogger(CWDCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
