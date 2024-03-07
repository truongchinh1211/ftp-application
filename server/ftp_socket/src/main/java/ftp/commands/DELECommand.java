/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;

public class DELECommand implements Command {

    private final FileBus fileService = new FileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            String filename = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
            String filePath = ftpFileUtils.convertPublicPathToFtpPath(session.getWorkingDirAbsolutePath(), filename);
            List<String> notDeletablePaths = fileService.removeFile(filePath, session.getUsername());
            if (notDeletablePaths.isEmpty()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_OK,
                        "Command okay.",
                        commandSocketWriter
                );
            } else {
                String notDeletablePathMsg = "Các đường dẫn không xóa được: \n";
                for (String path : notDeletablePaths) {
                    notDeletablePathMsg += path + "\n";
                }

                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        notDeletablePathMsg,
                        commandSocketWriter
                );
            }

        } catch (IOException ex) {
            Logger.getLogger(DELECommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
