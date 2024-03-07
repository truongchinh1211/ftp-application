/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socket;

import cipher.AESCipher;
import cipher.Encrypt;
import cipher.KeyAES;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.IPConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import payloads.StringResponse;
import payloads.UserData;
import payloads.UserPermission;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import payloads.UserPermissionResponse;
import utils.CustomFileUtils;

/**
 *
 * @author Son
 */
public class socketManager {

    private static socketManager instance = null;
    private Socket commandSocket, dataSocket;
    private BufferedWriter commandWriter, dataWriter;
    private BufferedReader commandReader, dataReader;
    private String ipServer = "";

    private socketManager() {
        try {
            IPConfig ipConfig = new IPConfig();
            ipServer = ipConfig.getIPServer();
//            ipServer = "localhost";
            // Khởi tạo kết nối TCP socket
            commandSocket = new Socket(ipServer, 21);
            // Khởi tạo BufferedReader và BufferedWriter để gửi và nhận dữ liệu
            commandReader = new CustomBufferedReader(new InputStreamReader(commandSocket.getInputStream()));
            commandWriter = new BufferedWriter(new OutputStreamWriter(commandSocket.getOutputStream()));
            commandReader.readLine();

            // send key aes to server
            String keyAES = Encrypt.encriptKey();
            commandWriter.append("KEY " + keyAES);
            commandWriter.newLine();
            commandWriter.flush();
            commandReader.readLine();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public static socketManager getInstance() {
        if (instance == null) {
            instance = new socketManager();
        }
        return instance;
    }

    /*--------------------------------Account command-----------------------------------------*/
    public StringResponse login(String user, String password) throws Exception {
        writeLineAndFlush("USER " + user, commandWriter);
        commandReader.readLine();
        writeLineAndFlush("PASS " + password, commandWriter);
        String message = commandReader.readLine();

        return new StringResponse(message);
    }

    public StringResponse register(String data) throws Exception {
        openNewDataPort();
        writeLineAndFlush("REG", commandWriter);
        commandReader.readLine();
        writeLineAndFlush(data, dataWriter);
        closeDataPort();
        return new StringResponse(commandReader.readLine());
    }

    public StringResponse verifyOTP(String username, String password, String otp) throws Exception {
        writeLineAndFlush("SOTP " + username + " " + password + " " + otp, commandWriter);
        return new StringResponse(commandReader.readLine());
    }

    public StringResponse regenerateOTP(String username, String password) throws Exception {
        writeLineAndFlush("GOTP " + username + " " + password + " ", commandWriter);
        return new StringResponse(commandReader.readLine());
    }

    public UserData getUserInfo() throws Exception {
        openNewDataPort();
        writeLineAndFlush("PROF", commandWriter);
        commandReader.readLine();
        String response = dataReader.readLine();
        closeDataPort();
        commandReader.readLine();// read close connection message
        Gson gson = new Gson();
        return gson.fromJson(response, UserData.class);
    }

    public StringResponse changePassword(String oldPass,String newPass) throws Exception {
        writeLineAndFlush("PCHG "+ oldPass+" " + newPass, commandWriter);
        return new StringResponse(commandReader.readLine());
    }
    public StringResponse changeInformation(String data) throws Exception{
        openNewDataPort();
        writeLineAndFlush("CHDT", commandWriter);
        commandReader.readLine();
        writeLineAndFlush(data, dataWriter);
        closeDataPort();
        return new StringResponse(commandReader.readLine());
    }
    /*------------------------------------------------------------------------------------------*/

 /*--------------------------------file manager command--------------------------------------*/
    public String getSharedFiles() throws Exception {
        openNewDataPort();
        writeLineAndFlush("LSHR", commandWriter);
        commandReader.readLine();
        String response = dataReader.readLine();
        closeDataPort();
        commandReader.readLine();// read close connection message
        return response;
    }

    public StringResponse getCurrentWorkingDirectory() throws Exception {
        writeLineAndFlush("PWD", commandWriter);
        return new StringResponse(commandReader.readLine());
    }

    public String getFileList() throws Exception {
        openNewDataPort();
        writeLineAndFlush("MLSD", commandWriter);
        commandReader.readLine();
        String response = dataReader.readLine();
        closeDataPort();
        commandReader.readLine();// read close connection message
        return response;
    }

    public StringResponse changeDirectory(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("CWD " + pathURLEncode, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }

    public StringResponse createNewFolder(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("MKD " + pathURLEncode, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }

    public StringResponse rename(String oldName, String newName) throws Exception {
        String oldNameURLEncode = URLEncoder.encode(oldName, StandardCharsets.UTF_8);
        String newNameURLEncode = URLEncoder.encode(newName, StandardCharsets.UTF_8);
        writeLineAndFlush("RNFR " + oldNameURLEncode, commandWriter);
        StringResponse res = new StringResponse(commandReader.readLine());
        if (res.getStatus() == StatusCode.FILE_ACTION_REQUIRES_INFO) {
            writeLineAndFlush("RNTO " + newNameURLEncode, commandWriter);
            res = new StringResponse(commandReader.readLine());
        }
        return res;
    }

    public StringResponse delete(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("DELE " + pathURLEncode, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);

    }

    public StringResponse checkPermissionForMoveCommand(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("RNFR " + pathURLEncode, commandWriter);
        return new StringResponse(commandReader.readLine());
    }

    public StringResponse move(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("RNTO " + pathURLEncode, commandWriter);
        return new StringResponse(commandReader.readLine());
    }

    public UserPermissionResponse getShareUserList(String path) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        openNewDataPort();
        writeLineAndFlush("LSUR " + pathURLEncode, commandWriter);
        UserPermissionResponse res = new UserPermissionResponse(commandReader.readLine());
        if (res.getStatus() == StatusCode.ACTION_FAILED) {
            return res;
        }
        String data = dataReader.readLine();
        closeDataPort();
        commandReader.readLine();
        System.out.println(data);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<UserPermission>>() {
        }.getType();
        res.setList(gson.fromJson(data, listType));
        return res;
    }

    /**
     * Cấp quyền cho một user đối với dạng tệp
     *
     * @param permission chỉ được xem 'r',được chỉnh sửa 's'.
     */
    public StringResponse grantFilePermission(String path, String username, String permission) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        String content = pathURLEncode + " " + username + " " + permission;
        writeLineAndFlush("SHRE file " + content, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }

    public StringResponse grantFolderPermission(String path, String username, boolean canModify, boolean uploadable, boolean downloadable) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        String content = pathURLEncode + " " + username
                + " " + (canModify ? "true" : "false")
                + " " + (uploadable ? "true" : "false")
                + " " + (downloadable ? "true" : "false");
        System.out.println(content);
        writeLineAndFlush("SHRE directory " + content, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }

    /**
     * Xóa quyền truy cập của user
     *
     * @param type dạng tệp 'file' dạng folder 'directory'.
     */
    public StringResponse deletePermission(String type, String path, String username) throws Exception {
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        String content = type + " " + pathURLEncode + " " + username;
        writeLineAndFlush("USHR " + content, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }
    public StringResponse checkFileSize(String path) throws Exception{
        String pathURLEncode = URLEncoder.encode(path, StandardCharsets.UTF_8);
        writeLineAndFlush("CHKS " + pathURLEncode, commandWriter);
        String response = commandReader.readLine();
        return new StringResponse(response);
    }

    public StringResponse uploadFile(String path, File file) throws Exception {
        String pathURLEncode = URLEncoder.encode(path + "/" + file.getName(), StandardCharsets.UTF_8);
        String fileType = CustomFileUtils.determineType(file);
        writeLineAndFlush("TYPE " + fileType, commandWriter);
        commandReader.readLine();
        openNewDataPort();
        writeLineAndFlush("STOR " + pathURLEncode, commandWriter);
        StringResponse res = new StringResponse(commandReader.readLine());
        if (res.getStatus() == StatusCode.FILE_ACTION_NOT_TAKEN) {
            return res;
        }
        if (fileType.equals("A")) {
            writeLineAndFlush(FileUtils.readFileToString(file, StandardCharsets.UTF_8), dataWriter);
        } else {
            writeLineAndFlush(Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file)), dataWriter);
        }
        closeDataPort();
        return new StringResponse(commandReader.readLine());
    }

    public StringResponse uploadDirectory(String path, File folder) throws Exception {
        StringResponse res = null;
        if (folder.isDirectory()) {
            if ((res = createNewFolder(path + "/" + folder.getName())).getStatus() == StatusCode.DIRECTORY_CREATED) {
                File[] files = folder.listFiles();
                if (files.length>0 || files!=null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String filePath = path + "/" + folder.getName();
                            res = uploadFile(filePath, file);
                            if (res.getStatus() == StatusCode.FILE_ACTION_NOT_TAKEN) {
                                return res;
                            }
                        } else if (file.isDirectory()) {
                            String newPath = path + "/" + folder.getName();
                            uploadDirectory(newPath, file);
                        }
                    }
                } else {
                    return createNewFolder(path);
                }
            }
        }
        return res;
    }

    public StringResponse downloadFile(String filePath, String localPath, String currentDir) throws Exception {
        String pathURLEncode = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
        String fileType = CustomFileUtils.determineType(filePath);
        writeLineAndFlush("TYPE " + fileType, commandWriter);
        commandReader.readLine();
        openNewDataPort();
        writeLineAndFlush("RETR " + pathURLEncode, commandWriter);
        StringResponse res = new StringResponse(commandReader.readLine());
        if (res.getStatus() == StatusCode.FILE_ACTION_NOT_TAKEN) {
            return res;
        }
        Path currentPath = Paths.get(currentDir);
        Path serverFilePath = Paths.get(filePath);
        String relativePath = currentPath.relativize(serverFilePath).toString();
        File file = new File(localPath + "/" + relativePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (fileType.equals("A")) {
            FileUtils.writeStringToFile(file, dataReader.readLine(), StandardCharsets.UTF_8);
        } else {
            FileUtils.writeByteArrayToFile(file, Base64.getDecoder().decode(dataReader.readLine()));
        }
        closeDataPort();
        return new StringResponse(commandReader.readLine());
    }

    public StringResponse downloadFolder(String folderPath, String localPath, String currentDir) throws Exception {
        String pathURLEncode = URLEncoder.encode(folderPath, StandardCharsets.UTF_8);
        openNewDataPort();
        writeLineAndFlush("RETR " + pathURLEncode, commandWriter);
        StringResponse res = new StringResponse(commandReader.readLine());
        if (res.getStatus() == StatusCode.FILE_ACTION_NOT_TAKEN) {
            return res;
        }
        String data = dataReader.readLine();
        closeDataPort();
        commandReader.readLine();
        System.out.println(data);
        StringTokenizer tokenizer = new StringTokenizer(data, "\n");
        while (tokenizer.hasMoreTokens()) {
            String filePath = tokenizer.nextToken();
            res = downloadFile(filePath, localPath, currentDir);
            System.out.println("downloading " + filePath);
            if (res.getStatus() == StatusCode.FILE_ACTION_NOT_TAKEN) {
                return res;
            }
        }
        return res;
    }

    /*------------------------------------------------------------------------------------------*/
 /*---------------------------------EPSV command---------------------------------------------*/
    public void openNewDataPort() throws Exception {
        writeLineAndFlush("EPSV", commandWriter);
        String epsvResponse = commandReader.readLine();
        int dataPort = Integer.parseInt(epsvResponse
                .replace("229 Entering Extended Passive Mode (|||", "")
                .replace("|)", ""));
        dataSocket = new Socket(ipServer, dataPort);
        dataWriter = new BufferedWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        dataReader = new CustomBufferedReader(new InputStreamReader(dataSocket.getInputStream()));
    }

    public void closeDataPort() throws IOException {
        dataWriter.close();
        dataReader.close();
        dataSocket.close();
    }

    /*------------------------------------------------------------------------------------------*/
    public void disconnect() throws IOException {
        commandReader.close();
        commandWriter.close();
        commandSocket.close();
        instance = null;
    }

    public void writeLineAndFlush(String content, BufferedWriter writer) throws Exception {

        byte[] ketAES = KeyAES.getInstance().getKey();
        String contextAES = AESCipher.encrypt(ketAES, content);

        writer.append(contextAES);
        writer.newLine();
        writer.flush();
    }

}
