/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.File;
import javax.swing.JFileChooser;
import socket.socketManager;
import utils.CustomFileUtils;

/**
 *
 * @author Son
 */
public class testSTOR {
    public static void main(String[] args) throws Exception {
        socketManager.getInstance().login("testuser","test");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println(selectedFile.isDirectory());
            if(selectedFile.isFile()){
                System.out.println(socketManager.getInstance().uploadFile("/users/testuser", selectedFile).getMessage());
            }else if(selectedFile.isDirectory()){
                System.out.println(socketManager.getInstance().uploadDirectory("/users/testuser", selectedFile).getMessage());
            }
        }
        socketManager.getInstance().disconnect();
    }
}
    
