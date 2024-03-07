/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import ftp.FilePermission;
import ftp.FtpFileUtils;
import java.io.File;

/**
 *
 * @author User
 */
public class DefaultFilePermissionGetter implements FilePermissionGetter {

    private final String username;
    private final FileBus fileBus = new FileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    public DefaultFilePermissionGetter(String username) {
        this.username = username;

    }

    @Override
    public FilePermission getFilePermission(File file) {
        return fileBus.getFilePermission(
                ftpFileUtils.convertJavaPathToFtpPath(file.getPath()),
                username,
                file.isFile() ? FileBus.NORMAL_FILE_TYPE : FileBus.DIRECTORY_TYPE
        );
    }

}
