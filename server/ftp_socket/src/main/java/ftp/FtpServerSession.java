package ftp;

import config.AppConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class FtpServerSession {

    private String username;
    // Working dir starts with "/"
    private String workingDir;
    private ServerSocket dataSocket;
    private String RNFRFilename;
    private String type;
    private SessionSocketUtils socketUtils = new SessionSocketUtils(null);
    private byte[] AESKey;
    private BufferedWriter writer;
    private BufferedReader reader;

    public FtpServerSession(Socket commandSocket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public String getWorkingDirAbsolutePath() {
        // Note: working dir property starts with "/"

        // Replace / with corresponding root path
        return AppConfig.SERVER_FTP_FILE_PATH + workingDir;
    }

    public boolean changeWorkingDir(String workingDir) {
        if (workingDir.equals("/")) {
            this.workingDir = "/";
            return true;
        }
        // Go up 1 level
        if (workingDir.equals("..")) {
            System.out.println("Change working dir: " + workingDir);
            List<String> pathTokens = Arrays.asList(this.workingDir.replaceFirst("/", "").split("/"));
            pathTokens = pathTokens.subList(0, pathTokens.size() - 1);
            this.workingDir = "/" + String.join("/", pathTokens);
            return true;
        }
        // Change working dir relative to root
        if (workingDir.startsWith("/")) {
            this.workingDir = workingDir;
            return true;
        }
        // Change working dir relative to current working dir
        this.workingDir = this.workingDir + "/" + workingDir;
        return true;
    }

    public ServerSocket getDataSocket() {
        return dataSocket;
    }

    public void setDataSocket(ServerSocket dataSocket) {
        this.dataSocket = dataSocket;
    }

    public String getRNFRFilename() {
        return RNFRFilename;
    }

    public void setRNFRFilename(String RNFRFilename) {
        this.RNFRFilename = RNFRFilename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getAESKey() {
        return AESKey;
    }

    public void setAESKey(byte[] AESKey) {
        this.AESKey = AESKey;
        socketUtils.setAESKey(AESKey);
    }

    public SessionSocketUtils getSessionSocketUtils() {
        return socketUtils;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

}
