/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.custom;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class FileTypeIconProvider {
    private Map<String, ImageIcon> fileTypeIcons;

    public FileTypeIconProvider() {
        fileTypeIcons = new HashMap<>();
        // Đăng ký các loại tệp và đường dẫn đến biểu tượng tương ứng
        fileTypeIcons.put("txt", new ImageIcon(getClass().getResource("/view/img/fileIcon/txt.png")));
        fileTypeIcons.put("dir", new ImageIcon(getClass().getResource("/view/img/fileIcon/folder.png")));
        fileTypeIcons.put("doc", new ImageIcon(getClass().getResource("/view/img/fileIcon/doc.png")));
        // Thêm các loại tệp khác vào đây
    }

    public ImageIcon getIconForFileType(String fileType) {
        // Lấy biểu tượng tương ứng với loại tệp
        return fileTypeIcons.get(fileType);
    }
}
