/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

/**
 *
 * @author User
 */
public class DirectoryPermission extends FilePermission {

    private boolean uploadable = false;
    private boolean downloadable = false;
    private boolean canModify = false;

    public DirectoryPermission() {
    }

    public DirectoryPermission(boolean canModify, boolean uploadable, boolean downloadable, boolean exist) {
        this.canModify = canModify;
        this.uploadable = uploadable;
        this.downloadable = downloadable;
        this.exist = exist;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public boolean isUploadable() {
        return uploadable;
    }

    public void setUploadable(boolean uploadable) {
        this.uploadable = uploadable;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    @Override
    public boolean isShared() {
        return downloadable || uploadable || canModify;
    }

    @Override
    public boolean isWritable() {
        return uploadable;
    }

    @Override
    public boolean isReadable() {
        return downloadable;
    }

    @Override
    public boolean isRenamable() {
        return canModify;
    }

    @Override
    public boolean isDeletable() {
        return canModify;
    }

}
