/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

import payload.PublicUserInfo;

/**
 *
 * @author User
 */
public class FilePermissionWithUser {

    private String fileType;

    private PublicUserInfo userInfo;

    private FilePermission permission;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public PublicUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(PublicUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public FilePermission getPermission() {
        return permission;
    }

    public void setPermission(FilePermission permission) {
        this.permission = permission;
    }

}
