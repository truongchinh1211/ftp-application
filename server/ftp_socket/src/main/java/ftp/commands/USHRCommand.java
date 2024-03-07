/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.DirectoryBus;
import bus.FileBus;
import bus.NormalFileBus;
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

/**
 *
 * @author User
 */
public class USHRCommand implements Command {

    private final FileBus fileService = new FileBus();
    private final NormalFileBus normalFileBus = new NormalFileBus();
    private final DirectoryBus directoryBus = new DirectoryBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        if (arguments.length != 3) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Syntax error.",
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(USHRCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String type = arguments[0];
        String fileName = URLDecoder.decode(arguments[1], StandardCharsets.UTF_8);
        String appliedUsername = arguments[2];
        String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                session.getWorkingDirAbsolutePath(),
                fileName
        );
        boolean success;
        if (type.equals(FileBus.NORMAL_FILE_TYPE)) {
            success = normalFileBus.unshareNormalFile(filePath, session.getUsername(), appliedUsername);
        } else {
            success = directoryBus.unshareDirectory(filePath, session.getUsername(), appliedUsername);
        }

        if (success) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_OK,
                        String.format("Unshare file %s successfully.", fileName),
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(USHRCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.",
                        commandSocketWriter
                );
            } catch (IOException ex) {
                Logger.getLogger(USHRCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
