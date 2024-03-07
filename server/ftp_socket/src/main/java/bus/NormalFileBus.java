/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import static bus.FileBus.DIRECTORY_TYPE;
import static bus.FileBus.NORMAL_FILE_TYPE;
import config.AppConfig;
import dao.FileDao;
import dao.ShareFilesDao;
import dao.UserDao;
import ftp.DirectoryPermission;
import ftp.FilePermission;
import ftp.FtpFileUtils;
import ftp.NormalFilePermission;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ShareFiles;
import model.User;
import model.ids.ShareFilesId;
import org.apache.commons.io.FileUtils;
import utils.EmailUtils;

/**
 *
 * @author User
 */
public class NormalFileBus {

    private static final FileDao fileDao = new FileDao();
    private static final ShareFilesDao shareFilesDao = new ShareFilesDao();
    private static final UserDao userDao = new UserDao();
    private static final FileBus fileBus = new FileBus();

    private File createTempFile(String fromRootDirPath) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyhhmmssSSS");
        String fileName = simpleDateFormat.format(new Date());
        File file = new File(fromRootDirPath + "/" + fileName);
        file.createNewFile();
        return file;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("ftp/users/testuser/bai3_3119410300_saudaiphat.png");
        File newFile = new File("bai3_3119410300_saudaiphat.png");
        byte[] readFileToByteArray = FileUtils.readFileToByteArray(file);
//        for (byte b : readFileToByteArray) {
//            System.out.println(b);
//        }
        Charset charset = StandardCharsets.US_ASCII;
        byte[] newBytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(readFileToByteArray));
        for (int i = 0; i < newBytes.length; i++) {
            if (newBytes[i] != readFileToByteArray[i]) {
                System.out.println(newBytes[i] + " and " + readFileToByteArray[i]);
            } else {
                System.out.println(newBytes[i] == readFileToByteArray[i]);
            }
        }
    }

    public synchronized boolean createNormalFile(String fromRootFilePath, String username) {
        User user = userDao.getUserByUsername(username);

        // Check if user is able to upload
        if (user.isBlockUpload()) {
            return false;
        }

        // Check if upload is allowed
        String parentDirPath = new FtpFileUtils().getParentPath(fromRootFilePath);
        DirectoryPermission parentPermission = (DirectoryPermission) fileBus.getFilePermission(
                parentDirPath,
                username,
                DIRECTORY_TYPE
        );
        if (!parentPermission.isUploadable()) {
            return false;
        }

        File file = new File(fromRootFilePath);
        if (file.exists()) {
            return false;
        }

        if (user.getUsedBytes() >= user.getQuotaInBytes()) {
            return false;
        }

        model.File fileDb = new model.File(0, fromRootFilePath, user, null);

        boolean success = fileDao.save(fileDb);
        shareFilesDao.save(new ShareFiles(new ShareFilesId(fileDb.getId(), user.getId()), username, fileDb, user));

        if (success) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileBus.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return success;
    }

    public synchronized boolean removeNormalFile(String fromRootFilePath, String username) {
        File file = new File(fromRootFilePath);
        if (!file.exists()) {
            return true;
        }

        if (!file.isFile()) {
            return false;
        }

        NormalFilePermission filePermission = (NormalFilePermission) fileBus.getFilePermission(
                fromRootFilePath,
                username,
                NORMAL_FILE_TYPE
        );
        if (!filePermission.isExist()) {
            return true;
        }
        
        if (!filePermission.isDeletable()) {
            return false;
        }
        
        FilePermission parentDirPermission = fileBus.getFilePermission(new FtpFileUtils().getParentPath(fromRootFilePath), username, DIRECTORY_TYPE);
        if(!parentDirPermission.isDeletable()) {
            return false;
        }

        model.File fileFromDb = fileDao.getFileByPath(fromRootFilePath);
        boolean success = fileDao.remove(fileFromDb.getId());
        if (success) {
            long fileSize = FileUtils.sizeOf(file);
            file.delete();

            User owner = fileFromDb.getUser();
            long usedBytes = owner.getUsedBytes();
            owner.setUsedBytes(usedBytes - fileSize);
            userDao.update(owner);
        }
        return success;
    }

    public synchronized boolean writeToNormalFile(String fromRootFilePath, String username, String data, String writeMode) {
        User user = userDao.getUserByUserName(username);

        // Check if user is able to upload
        if (user.isBlockUpload()) {
            return false;
        }

        File file = new File(fromRootFilePath);

        if (!file.exists()) {
            return false;
        }

        if (!file.isFile()) {
            return false;
        }

        if (!fromRootFilePath.startsWith(AppConfig.SERVER_FTP_ANON_PATH)) {
            NormalFilePermission filePermission = (NormalFilePermission) fileBus.getFilePermission(
                    fromRootFilePath,
                    username,
                    NORMAL_FILE_TYPE
            );
            if (!filePermission.isWritable()) {
                return false;
            }
        }

        FtpFileUtils ftpFileUtils = new FtpFileUtils();
        String folderPath = ftpFileUtils.getParentPath(fromRootFilePath);
        File tempFile;

        // Create temp file
        try {
            tempFile = createTempFile(folderPath);
        } catch (IOException ex) {
            return false;
        }

        // Write to temp file so we can check the size of the file later
        try {
            if (writeMode.equals("A")) {
                FileUtils.write(tempFile, data, StandardCharsets.UTF_8);
            } else {
                FileUtils.writeByteArrayToFile(tempFile, Base64.getDecoder().decode(data));
            }
        } catch (IOException ex) {
            Logger.getLogger(FileBus.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        // Check max upload size
        long oldFileSize = FileUtils.sizeOf(file);
        long newFileSize = FileUtils.sizeOf(tempFile);
        if (newFileSize > user.getMaxUploadFileSizeBytes()) {
            tempFile.delete();
            return false;
        }

        // Check if exceed quota
        model.File fileInDb = fileDao.getFileByPath(fromRootFilePath);
        User owner = fileInDb.getUser();
        long newUsedBytes = (long) (newFileSize - oldFileSize + owner.getUsedBytes());
        if (newUsedBytes > owner.getQuotaInBytes()) {
            tempFile.delete();
            return false;
        }

        // Overwrite to real file
        try {
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(NormalFileBus.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        // Update used kb
        owner.setUsedBytes(newUsedBytes);
        userDao.update(owner);

        return true;
    }

    public synchronized boolean setShareNormalFilePermission(String fromRootFilePath, String ownerUsername, String appliedUsername, String permission) {
        try {
            model.File fileInDb = fileDao.getFileByPath(fromRootFilePath);
            if (fileInDb == null) {
                return false;
            }

            if (!ownerUsername.equals(fileInDb.getUser().getUsername())) {
                return false;
            }

            User appliedUser = userDao.getUserByUserName(appliedUsername);

            if (appliedUser == null) {
                return false;
            }

            boolean resUpdate = shareFilesDao.update(
                    new ShareFiles(
                            new ShareFilesId(fileInDb.getId(), appliedUser.getId()),
                            permission,
                            fileInDb,
                            appliedUser)
            );

            EmailUtils emailUtils = new EmailUtils();
            boolean resSendEmail = emailUtils.sendSharingFileNotification(ownerUsername, appliedUsername, permission);

            return resUpdate && resSendEmail;
        } catch (Exception ex) {
            return false;
        }
    }

    public synchronized boolean unshareNormalFile(String fromRootFilePath, String ownerUsername, String appliedUsername) {
        model.File fileInDb = fileDao.getFileByPath(fromRootFilePath);
        if (fileInDb == null) {
            return false;
        }

        if (!ownerUsername.equals(fileInDb.getUser().getUsername())) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);

        ShareFiles shareFile = new ShareFiles();
        shareFile.setIds(new ShareFilesId(fileInDb.getId(), appliedUser.getId()));

        boolean success = shareFilesDao.remove(shareFile);
        return success;
    }

    public boolean setShareNormalFilePermissionAdmin(String fromRootFilePath, String appliedUsername, String permission) {
        model.File fileInDb = fileDao.getFileByPath(fromRootFilePath);
        if (fileInDb == null) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);

        boolean resUpdate = shareFilesDao.update(
                new ShareFiles(
                        new ShareFilesId(fileInDb.getId(), appliedUser.getId()),
                        permission,
                        fileInDb,
                        appliedUser)
        );

        EmailUtils emailUtils = new EmailUtils();
        boolean resSendEmail = emailUtils.sendSharingFileNotification("Hệ thống", appliedUsername, permission);

        return resUpdate && resSendEmail;
    }

    public synchronized boolean unshareNormalFileAdmin(String fromRootFilePath, String appliedUsername) {
        model.File fileInDb = fileDao.getFileByPath(fromRootFilePath);
        if (fileInDb == null) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);

        ShareFiles shareFile = new ShareFiles();
        shareFile.setIds(new ShareFilesId(fileInDb.getId(), appliedUser.getId()));

        boolean success = shareFilesDao.remove(shareFile);
        return success;
    }
}
