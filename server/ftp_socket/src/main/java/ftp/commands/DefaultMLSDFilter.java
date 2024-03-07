/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import bus.FileBus;
import ftp.FilePermission;
import ftp.FtpFileUtils;
import java.io.File;
import java.io.FileFilter;

/**
 * Filter to get all files that has readable permission
 *
 * @author User
 */
public class DefaultMLSDFilter implements FileFilter {

    private final FileBus fileService = new FileBus();
    private final FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private final String username;

    public DefaultMLSDFilter(String username) {
        this.username = username;
    }

    @Override
    public boolean accept(File childFile) {
        
        FilePermission filePermission = fileService.getFilePermission(
                ftpFileUtils.convertJavaPathToFtpPath(childFile.getPath()),
                username,
                childFile.isFile() ? FileBus.NORMAL_FILE_TYPE : FileBus.DIRECTORY_TYPE);
        return filePermission != null;
    }

}
