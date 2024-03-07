/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import config.AppConfig;
import ftp.FilePermission;
import bus.FileBus;
import dao.UserDao;
import ftp.FtpFileUtils;
import ftp.FtpServerSession;
import ftp.StatusCode;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import org.apache.commons.io.FileUtils;

public class RETRCommand implements Command {

    private FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private UserDao userDao = new UserDao();
    private FileBus fileService = new FileBus();

    private void executeFile(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) throws IOException {
        Socket socket = session.getDataSocket().accept();
        String inputFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
        String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                session.getWorkingDirAbsolutePath(),
                inputFilePath
        );
        File file = new File(filePath);
        User user = userDao.getUserByUserName(session.getUsername());
        if (FileUtils.sizeOf(file) > user.getMaxDownloadFileSizeBytes()) {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_NOT_TAKEN,
                    "Dung lượng không được vượt quá dung lượng download tối đa.", commandSocketWriter);
            return;
        }

        fileService = new FileBus();
        FilePermission filePermission = fileService.getFilePermission(
                filePath,
                session.getUsername(),
                FileBus.NORMAL_FILE_TYPE
        );

        if (filePermission.isReadable()) {
            BufferedWriter dataSocketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            if (session.getType().equals("A")) {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                session.getSessionSocketUtils().writeLineAndFlush(content, dataSocketWriter);

            }
            if (session.getType().equals("I")) {
                byte[] data = FileUtils.readFileToByteArray(file);
                session.getSessionSocketUtils().writeLineAndFlush(Base64.getEncoder().encodeToString(data), dataSocketWriter);
            }
            dataSocketWriter.close();
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection.",
                    commandSocketWriter
            );
        } else {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_NOT_TAKEN,
                    "Forbidden.",
                    commandSocketWriter
            );
        }
        socket.close();
    }

    private List<String> listAllReadableNormalFilePaths(File directory, String username) {
        ArrayList<String> filePaths = new ArrayList<>();
        File[] childFiles = directory.listFiles();
        if (childFiles == null) {
            return filePaths;
        }

        for (File childFile : childFiles) {
            boolean isChildNormalFile = childFile.isFile();
            String childFilePath = ftpFileUtils.convertJavaPathToFtpPath(childFile.getPath());
            FilePermission filePermission = fileService.getFilePermission(
                    childFilePath,
                    username,
                    isChildNormalFile ? FileBus.NORMAL_FILE_TYPE : FileBus.DIRECTORY_TYPE
            );

            if (filePermission.isReadable()) {
                if (isChildNormalFile) {
                    filePaths.add(childFilePath);
                } else {
                    filePaths.addAll(listAllReadableNormalFilePaths(childFile, username));
                }
            }
        }

        return filePaths;
    }

    private void executeDirectory(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) throws IOException {
        Socket socket = session.getDataSocket().accept();
        String inputFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
        String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                session.getWorkingDirAbsolutePath(),
                inputFilePath
        );
        File file = new File(filePath);

        fileService = new FileBus();
        FilePermission filePermission = fileService.getFilePermission(
                filePath,
                session.getUsername(),
                FileBus.DIRECTORY_TYPE
        );

        if (filePermission.isReadable()) {
            BufferedWriter dataSocketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            List<String> readableNormalFilePaths = listAllReadableNormalFilePaths(file, session.getUsername());
            // Convert to public path
            readableNormalFilePaths = readableNormalFilePaths.stream().map((ftpPath) -> ftpPath.replaceFirst(AppConfig.SERVER_FTP_FILE_PATH, "")).toList();
            
            String result = String.join("\n", readableNormalFilePaths);
            session.getSessionSocketUtils().writeLineAndFlush(result, dataSocketWriter);
            dataSocketWriter.close();
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.CLOSING_DATA_CONNECTION,
                    "Closing data connection.",
                    commandSocketWriter
            );
        } else {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_NOT_TAKEN,
                    "Forbidden.",
                    commandSocketWriter
            );
        }

        socket.close();
    }

    @Override
    public void execute(String[] arguments, FtpServerSession session, BufferedWriter commandSocketWriter) {
        User user = userDao.getUserByUserName(session.getUsername());
        if (user.isBlockDownload()) {
            try {
                session.getSessionSocketUtils().respondCommandSocket(
                        StatusCode.FILE_ACTION_NOT_TAKEN,
                        "Forbidden.", commandSocketWriter);
            } catch (IOException ex) {
                Logger.getLogger(RETRCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        try {
            session.getSessionSocketUtils().respondCommandSocket(
                    StatusCode.FILE_ACTION_OK,
                    "Requested file action okay, completed.",
                    commandSocketWriter
            );

            String inputFilePath = URLDecoder.decode(arguments[0], StandardCharsets.UTF_8);
            String filePath = ftpFileUtils.convertPublicPathToFtpPath(
                    session.getWorkingDirAbsolutePath(),
                    inputFilePath
            );

            // Anonymous check
            if (filePath.startsWith(AppConfig.SERVER_FTP_ANON_PATH)) {
                if (!user.isAnonymous()) {
                    session.getSessionSocketUtils().respondCommandSocket(
                            StatusCode.FILE_ACTION_NOT_TAKEN,
                            "Anonymous disabled.",
                            commandSocketWriter
                    );
                    return;
                }
            }
            File file = new File(filePath);

            if (file.isFile()) {
                executeFile(arguments, session, commandSocketWriter);
            } else {
                executeDirectory(arguments, session, commandSocketWriter);
            }

        } catch (IOException ex) {
            Logger.getLogger(RETRCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
