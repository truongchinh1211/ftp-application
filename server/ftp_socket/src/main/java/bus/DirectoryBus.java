/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import static bus.FileBus.DIRECTORY_TYPE;
import config.AppConfig;
import dao.DirectoryDao;
import dao.FileDao;
import dao.ShareDirectoriesDao;
import dao.UserDao;
import ftp.DirectoryPermission;
import ftp.FtpFileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Directory;
import model.ShareDirectories;
import model.User;
import model.ids.ShareDirectoriesId;
import utils.EmailUtils;

/**
 *
 * @author User
 */
public class DirectoryBus {

    private static final FileBus fileBus = new FileBus();
    private static final FileDao fileDao = new FileDao();
    private static final DirectoryDao directoryDao = new DirectoryDao();
    private static final ShareDirectoriesDao shareDirectoriesDao = new ShareDirectoriesDao();
    private static final UserDao userDao = new UserDao();
    private static final NormalFileBus normalFileBus = new NormalFileBus();
    private static final FtpFileUtils ftpFileUtils = new FtpFileUtils();

    public boolean createHomeDirectoryIfNotExist(String username) {
        String fromRootPath = ftpFileUtils.joinPath(AppConfig.SERVER_FTP_USERS_PATH, username);
        User user = userDao.getUserByUsername(username);
        Directory homeDir = directoryDao.getDirectoryByPath(fromRootPath);
        if (homeDir != null) {
            return true;
        }

        Directory directory = new Directory(0, fromRootPath, user, null);
        boolean success = directoryDao.save(directory);
        shareDirectoriesDao.save(new ShareDirectories(new ShareDirectoriesId(directory.getId(), user.getId()), true, true, true, directory, user));
        File file = new File(fromRootPath);
        if (success) {
            file.mkdir();
        }
        return success;
    }

    public boolean createDirectory(String fromRootPath, String username) {
        User user = userDao.getUserByUsername(username);

        // Check if user is allowed to upload
        if (user.isBlockUpload()) {
            return false;
        }

        // Check if upload is allowed
        String parentDirPath = new FtpFileUtils().getParentPath(fromRootPath);
        DirectoryPermission parentPermission = (DirectoryPermission) fileBus.getFilePermission(
                parentDirPath,
                username,
                DIRECTORY_TYPE
        );
        if (!parentPermission.isUploadable()) {
            return false;
        }
        File file = new File(fromRootPath);
        if (file.exists()) {
            return false;
        }

        if (user.getUsedBytes() >= user.getQuotaInBytes()) {
            return false;
        }

        Directory directory = new Directory(0, fromRootPath, user, null);
        boolean success = directoryDao.save(directory);
        shareDirectoriesDao.save(new ShareDirectories(new ShareDirectoriesId(directory.getId(), user.getId()), true, true, true, directory, user));
        if (success) {
            file.mkdir();
        }
        return success;
    }

    public List<String> removeDirectory(String fromRootPath, String username) {
        File file = new File(fromRootPath);
        List<String> notRemovableFilePaths = new ArrayList<>();
        if (!file.exists()) {
            notRemovableFilePaths.add(fromRootPath);
            return notRemovableFilePaths;
        }

        if (!file.isDirectory()) {
            notRemovableFilePaths.add(fromRootPath);
            return notRemovableFilePaths;
        }

        File[] childFiles = file.listFiles();

        if (childFiles != null) {
            for (File childFile : childFiles) {
                String childFilePath = ftpFileUtils.convertJavaPathToFtpPath(childFile.getPath());
                if (childFile.isDirectory()) {
                    notRemovableFilePaths.addAll(removeDirectory(
                            childFilePath,
                            username
                    ));
                } else {
                    if (!normalFileBus.removeNormalFile(childFilePath, username)) {
                        notRemovableFilePaths.add(childFilePath);
                    }
                }
            }
        }

        DirectoryPermission directoryPermission = (DirectoryPermission) fileBus.getFilePermission(
                fromRootPath,
                username,
                DIRECTORY_TYPE
        );

        if (!directoryPermission.isDeletable()) {
            notRemovableFilePaths.add(0, fromRootPath);
            return notRemovableFilePaths;
        }

        if (!notRemovableFilePaths.isEmpty()) {
            return notRemovableFilePaths;
        }

        Directory directoryFromDb = directoryDao.getDirectoryByPath(fromRootPath);
        boolean success = directoryDao.remove(directoryFromDb.getId());
        if (success) {
            file.delete();
        }
        return notRemovableFilePaths;
    }

    public boolean setShareDirectoryPermission(String fromRootDirPath, String ownerUsername, String appliedUsername, boolean canModify, boolean uploadable, boolean downloadable) {
        try {
            Directory directoryInDb = directoryDao.getDirectoryByPath(fromRootDirPath);
            if (directoryInDb == null) {
                return false;
            }

            if (!ownerUsername.equals(directoryInDb.getUser().getUsername())) {
                return false;
            }

            User appliedUser = userDao.getUserByUserName(appliedUsername);

            boolean resUpdate = shareDirectoriesDao.update(
                    new ShareDirectories(
                            new ShareDirectoriesId(directoryInDb.getId(), appliedUser.getId()),
                            canModify,
                            uploadable,
                            downloadable,
                            directoryInDb,
                            appliedUser)
            );

            // Todo: Send mail
            EmailUtils emailUtils = new EmailUtils();
            boolean resSendEmail = emailUtils.sendSharingDirectoryNotification(ownerUsername, appliedUsername, canModify, uploadable, downloadable);

            return resUpdate && resSendEmail;
        } catch (Exception ex) {
            return false;
        }

    }

    public boolean setDirectoryPermissionAdmin(String fromRootDirPath, String appliedUsername, boolean canModify, boolean uploadable, boolean downloadable) {
        Directory directoryInDb = directoryDao.getDirectoryByPath(fromRootDirPath);
        if (directoryInDb == null) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);
        
        if(appliedUser == null) {
           return false;
        }

        boolean resUpdate = shareDirectoriesDao.update(
                new ShareDirectories(
                        new ShareDirectoriesId(directoryInDb.getId(), appliedUser.getId()),
                        canModify,
                        uploadable,
                        downloadable,
                        directoryInDb,
                        appliedUser)
        );
        EmailUtils emailUtils = new EmailUtils();
        boolean resSendEmail = emailUtils.sendSharingDirectoryNotification("Hệ thống", appliedUsername, canModify, uploadable, downloadable);

        return resUpdate && resSendEmail;
    }

    public boolean unshareDirectoryAdmin(String fromRootDirPath, String appliedUsername) {
        Directory directoryInDb = directoryDao.getDirectoryByPath(fromRootDirPath);
        if (directoryInDb == null) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);

        ShareDirectories shareDirectory = new ShareDirectories();
        shareDirectory.setIds(new ShareDirectoriesId(directoryInDb.getId(), appliedUser.getId()));

        boolean success = shareDirectoriesDao.remove(shareDirectory);
        return success;
    }

    public boolean unshareDirectory(String fromRootDirPath, String ownerUsername, String appliedUsername) {
        Directory directoryInDb = directoryDao.getDirectoryByPath(fromRootDirPath);
        if (directoryInDb == null) {
            return false;
        }

        if (!ownerUsername.equals(directoryInDb.getUser().getUsername())) {
            return false;
        }

        User appliedUser = userDao.getUserByUserName(appliedUsername);

        ShareDirectories shareDirectory = new ShareDirectories();
        shareDirectory.setIds(new ShareDirectoriesId(directoryInDb.getId(), appliedUser.getId()));

        boolean success = shareDirectoriesDao.remove(shareDirectory);
        return success;
    }

//    public double getHomeDirectorySizeKb(String username) {
//        String homeDirPath = ftpFileUtils.joinPath(AppConfig.SERVER_FTP_FILE_PATH, username);
//        File homeDir = new File(homeDirPath);
//        // Convert byte to kb
//        return homeDir.length() * 1024;
//    }
}
