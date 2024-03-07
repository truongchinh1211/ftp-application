/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

/**
 *
 * @author User
 */
public abstract class FilePermission {

    protected boolean exist;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public abstract boolean isShared();

    public abstract boolean isWritable();

    public abstract boolean isReadable();

    public abstract boolean isRenamable();

    public abstract boolean isDeletable();

}
