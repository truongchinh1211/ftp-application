/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ftp;

import config.AppConfig;
import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author User
 */
public class FtpFileUtils {

    public String joinPath(String workingDir, String... tokens) {
        if (workingDir.equals(AppConfig.SERVER_FTP_FILE_PATH)) {
            if (tokens.length > 0) {
                return AppConfig.SERVER_FTP_FILE_PATH + "/" + String.join("/", tokens);
            } else {
                return AppConfig.SERVER_FTP_FILE_PATH;
            }
        }
        return workingDir + "/" + String.join("/", tokens);
    }

    public String getParentPath(String path) {
        List<String> pathTokens = Arrays.asList(path.split("/"));
        pathTokens = pathTokens.subList(0, pathTokens.size() - 1);
        if (pathTokens.isEmpty()) {
            return "/";
        }
        String parentPath = String.join("/", pathTokens);
        return parentPath;
    }

    public String convertJavaPathToFtpPath(String javaFilePath) {
        return javaFilePath.replace("\\", "/");
    }

    public String convertPublicPathToFtpPath(String workingDirAbsolutePath, String publicPath) {
        String filePath = "";
        if (publicPath.startsWith("/")) {
            filePath = publicPath.replaceFirst("/", AppConfig.SERVER_FTP_FILE_PATH + "/");
        } else {
            filePath = joinPath(workingDirAbsolutePath, publicPath);
        }
        
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);

        return filePath;
    }
}
