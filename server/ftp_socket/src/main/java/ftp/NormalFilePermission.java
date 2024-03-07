/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

/**
 *
 * @author User
 */
public class NormalFilePermission extends FilePermission {

    public static final String READABLE_PERMISSION = "r";
    public static final String FULL_PERMISSION = "w";
    public static final String NULL_PERMISSION = "";

    private String permission = NULL_PERMISSION;

    public NormalFilePermission() {
    }

    public NormalFilePermission(String permission, boolean exist) {
        this.permission = permission;
        this.exist = exist;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean isShared() {
        return permission.equals(READABLE_PERMISSION) || permission.equals(FULL_PERMISSION);
    }

    @Override
    public boolean isWritable() {
        return permission.equals(FULL_PERMISSION);
    }

    @Override
    public boolean isReadable() {
        return permission.equals(READABLE_PERMISSION) || permission.equals(FULL_PERMISSION);
    }

    @Override
    public boolean isRenamable() {
        return permission.equals(FULL_PERMISSION);
    }

    @Override
    public boolean isDeletable() {
        return permission.equals(FULL_PERMISSION);
    }

}
