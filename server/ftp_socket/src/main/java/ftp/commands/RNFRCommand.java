/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import ftp.FilePermission;
import bus.FileBus;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RNFRCommand implements Command {

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        FtpFileUtils ftpFileUtils = new FtpFileUtils();
        try {
            String fileName = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
            String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    fileName
            );
            File file = new File(filePath);
            if (!file.exists()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "File doesn't exist.",
                        commandSocketWriter
                );
                return;
            }
            FileBus fileService = new FileBus();
            FilePermission filePermission = fileService.getFilePermission(
                    filePath,
                    session.getUsername(),
                    file.isFile() ? FileBus.NORMAL_FILE_TYPE : FileBus.DIRECTORY_TYPE
            );
            if (!filePermission.isRenamable()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.",
                        commandSocketWriter
                );
                return;
            }

            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_REQUIRES_INFO,
                    "RNFR accepted. Please supply new name for RNTO.",
                    commandSocketWriter
            );
            session.setRNFRFilename(fileName);
        } catch (IOException ex) {
            Logger.getLogger(RETRCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
