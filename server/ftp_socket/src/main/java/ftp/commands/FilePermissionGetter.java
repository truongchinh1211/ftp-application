/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp.commands;

import ftp.FilePermission;
import java.io.File;

/**
 *
 * @author User
 */
public interface FilePermissionGetter {

    public FilePermission getFilePermission(File file);
}
