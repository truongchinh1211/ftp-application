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
import ftp.NormalFilePermission;
import ftp.SessionSocketUtils;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

// Fix
/**
 *
 * @author User
 */
public class SHRECommand implements Command {

    private final FileBus fileService = new FileBus();
    private final NormalFileBus normalFileBus = new NormalFileBus();
    private final DirectoryBus directoryBus = new DirectoryBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        try {
            String type = arguments[0];
            String fileName = URLDecoder.decode(arguments[1], StandardCharsets.UTF_8);
            String appliedUsername = arguments[2];
            String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    fileName
            );
            if (type.equals(FileBus.NORMAL_FILE_TYPE)) {
                String permission = arguments[3];
                if (!permission.equals(NormalFilePermission.FULL_PERMISSION)
                        && !permission.equals(NormalFilePermission.NULL_PERMISSION)
                        && !permission.equals(NormalFilePermission.READABLE_PERMISSION)) {
                    session.getSessionSocketUtils().respondCommandSocket(
                            StatusCode.FILE_ACTION_NOT_TAKEN,
                            "Syntax error.",
                            commandSocketWriter
                    );
                    return;
                }

                boolean isSuccess = normalFileBus.setShareNormalFilePermission(
                        filePath,
                        session.getUsername(),
                        appliedUsername,
                        permission
                );

                if (isSuccess) {
                    try {
                        session.getSessionSocketUtils().respondCommandSocket(
                                StatusCode.FILE_ACTION_OK,
                                String.format("Shared file %s successfully.", fileName),
                                commandSocketWriter
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        session.getSessionSocketUtils().respondCommandSocket(
                                StatusCode.FILE_ACTION_NOT_TAKEN,
                                "Forbidden.",
                                commandSocketWriter
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            if (type.equals(FileBus.DIRECTORY_TYPE)) {
                boolean canModify = Boolean.parseBoolean(arguments[3]);
                boolean uploadable = Boolean.parseBoolean(arguments[4]);
                boolean downloadable = Boolean.parseBoolean(arguments[5]);

                boolean isSuccess = directoryBus.setShareDirectoryPermission(
                        filePath,
                        session.getUsername(),
                        appliedUsername,
                        canModify,
                        uploadable,
                        downloadable
                );

                if (isSuccess) {
                    try {
                        session.getSessionSocketUtils().respondCommandSocket(
                                StatusCode.FILE_ACTION_OK,
                                String.format("Shared file %s successfully.", fileName),
                                commandSocketWriter
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        session.getSessionSocketUtils().respondCommandSocket(
                                StatusCode.FILE_ACTION_NOT_TAKEN,
                                "Forbidden.",
                                commandSocketWriter
                        );
                    } catch (IOException ex) {
                        Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        } catch (IOException ex) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Syntax error.",
                        commandSocketWriter
                );
            } catch (IOException ex1) {
                Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Syntax error.",
                        commandSocketWriter
                );
            } catch (IOException ex1) {
                Logger.getLogger(SHRECommand.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

}
