package ftp.commands;

import config.AppConfig;
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

public class RNTOCommand implements Command {

    private final FileBus fileService = new FileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {

        String inputNewFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
        String oldFilename = session.getRNFRFilename();
        if (oldFilename == null) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "RNFR command is required before this command.",
                        commandSocketWriter
                );
                return;
            } catch (IOException ex) {
                Logger.getLogger(RNTOCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            String oldFilePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    oldFilename
            );

            String newFilePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    inputNewFilePath
            );

            File fileWithNewName = new File(newFilePath);
            if (fileWithNewName.exists()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        String.format("File with %s name already exists.", inputNewFilePath),
                        commandSocketWriter
                );
                return;
            }
            
            File oldFile = new File(oldFilePath);

            boolean success = fileService.changeFilePath(
                    oldFilePath,
                    newFilePath,
                    session.getUsername(),
                    oldFile.isFile() ? FileBus.NORMAL_FILE_TYPE : FileBus.DIRECTORY_TYPE);
            if (success) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_OK,
                        "Requested file action okay, completed.",
                        commandSocketWriter
                );
                session.setRNFRFilename(null);
            } else {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.",
                        commandSocketWriter
                );
            }

        } catch (IOException ex) {
            Logger.getLogger(RETRCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
