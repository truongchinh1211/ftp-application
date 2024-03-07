/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import dao.UserDao;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author User
 */
public class CHKSCommand implements Command {

    private FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private UserDao userDao = new UserDao();

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        String inputFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
        String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                session.getWorkingDirAbsolutePath(),
                inputFilePath
        );
        File file = new File(filePath);
        User user = userDao.getUserByUserName(session.getUsername());

        try {
            if (FileUtils.sizeOf(file) > user.getMaxDownloadFileSizeBytes()) {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Dung lượng không được vượt quá dung lượng download tối đa.", commandSocketWriter);
            } else {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.COMMAND_OK,
                        "OK", commandSocketWriter);
            }
        } catch (IOException ex) {
            Logger.getLogger(CHKSCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
