/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import payload.GetAnonymousFilesResult;
import config.AppConfig;
import dao.DirectoryDao;
import dao.FileDao;
import payload.GetSharedFilesResultDto;
import dao.UserDao;
import ftp.DirectoryPermission;
import ftp.FilePermission;
import ftp.FilePermissionWithUser;
import ftp.FtpFileUtils;
import ftp.NormalFilePermission;
import ftp.commands.FilePermissionGetter;
import ftp.commands.MLSDFormatter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mapper.PublicUserMapper;
import model.Directory;
import model.ShareDirectories;
import model.ShareFiles;
import model.User;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author User
 */
public class FileBus {

    public static final String DIRECTORY_TYPE = "directory";
    public static final String NORMAL_FILE_TYPE = "file";
    public static final String CHECK_FILE_SIZE_EXCEED_QUOTA = "File vượt quá tổng dung lượng lưu trữ cho phép";
    public static final String CHECK_FILE_SIZE_EXCEED_UPLOAD_SIZE = "File vượt quá dung lượng upload tối đa";
    public static final String CHECK_FILE_SIZE_OK = "";

    private static final FileDao fileDao = new FileDao();
    private static final DirectoryDao directoryDao = new DirectoryDao();
    private static final UserDao userDao = new UserDao();
    private static final FtpFileUtils ftpFileUtils = new FtpFileUtils();
    private static final NormalFileBus normalFileBus = new NormalFileBus();
    private static final DirectoryBus directoryBus = new DirectoryBus();
    private static final PublicUserMapper publicUserMapper = new PublicUserMapper();

    public List<String> removeFile(String fromRootFilePath, String username) {
        File file = new File(fromRootFilePath);
        List<String> notRemovableFilePaths = new ArrayList<>();
        if (!file.exists()) {
            notRemovableFilePaths.add(fromRootFilePath);
            return notRemovableFilePaths;
        }

        if (file.isDirectory()) {
            return directoryBus.removeDirectory(fromRootFilePath, username);
        }

        if (!normalFileBus.removeNormalFile(fromRootFilePath, username)) {
            notRemovableFilePaths.add(fromRootFilePath);
            return notRemovableFilePaths;
        }

        return notRemovableFilePaths;
    }

    private void reparentFilePathInDb(File file, String newParentPath, boolean recursive) {
        String filePath = file.getPath().replace("\\", "/");
        String newFilePath = ftpFileUtils.joinPath(newParentPath, file.getName());

        if (file.isFile()) {
            model.File fileInDb = fileDao.getFileByPath(filePath);
            fileInDb.setPath(newFilePath);
            fileDao.update(fileInDb);
        }

        if (file.isDirectory()) {
            Directory directory = directoryDao.getDirectoryByPath(filePath);
            directory.setPath(newFilePath);
            directoryDao.update(directory);

            if (recursive) {
                File[] childFiles = file.listFiles();

                if (childFiles == null) {
                    return;
                }

                for (File child : childFiles) {
                    reparentFilePathInDb(child, newFilePath, true);
                }
            }
        }
    }

    public boolean changeFilePath(String oldFilePath, String newFilePath, String username, String fileType) {
        File file = new File(oldFilePath);
        if (!file.exists()) {
            return false;
        }

        FilePermission filePermission = getFilePermission(oldFilePath, username, fileType);
        if (!filePermission.isRenamable()) {
            return false;
        }

        String parentPath = ftpFileUtils.getParentPath(newFilePath);
        // Check if uploadable
        if (file.isFile()) {
            DirectoryPermission parentDirPermission = (DirectoryPermission) getFilePermission(parentPath, username, DIRECTORY_TYPE);
            if (!parentDirPermission.isUploadable()) {
                return false;
            }
        }

        // Reparent children files/directories
        File destination = new File(newFilePath);
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File child : childFiles) {
                reparentFilePathInDb(child, newFilePath, true);
            }
        }

        // Rename current file/directory
        if (file.isFile()) {
            model.File fileInDb = fileDao.getFileByPath(oldFilePath);
            fileInDb.setPath(newFilePath);
            fileDao.update(fileInDb);
        }
        if (file.isDirectory()) {
            Directory directory = directoryDao.getDirectoryByPath(oldFilePath);
            directory.setPath(newFilePath);
            directoryDao.update(directory);
        }
        file.renameTo(destination);

        // Reparent current file/directory in db
        reparentFilePathInDb(file, parentPath, false);
        return true;
    }

    public GetSharedFilesResultDto getSharedFiles(String appliedUsername) {
        return userDao.getSharedFiles(appliedUsername);
    }

    private FilePermission getSingleFilePermission(String fromRootFilePath, String username, String fileType, boolean isAnonymous) {
        // FTP root case
        if (fromRootFilePath.equals(AppConfig.SERVER_FTP_FILE_PATH)) {
            return new DirectoryPermission(false, false, false, true);
        }

        if (fromRootFilePath.equals(AppConfig.SERVER_FTP_ANON_PATH)) {
            if (!isAnonymous) {
                return new DirectoryPermission(false, false, false, true);
            } else {
                return new DirectoryPermission(false, true, true, true);
            }
        }

        if (fromRootFilePath.startsWith(AppConfig.SERVER_FTP_ANON_PATH)) {
            if (!isAnonymous) {
                if (fileType.equals(DIRECTORY_TYPE)) {
                    return new DirectoryPermission(false, false, false, false);
                } else {
                    return new NormalFilePermission(NormalFilePermission.NULL_PERMISSION, false);
                }
            }

            if (fileType.equals(DIRECTORY_TYPE)) {
                Directory directoryFromDb = directoryDao.getDirectoryByPath(fromRootFilePath);
                if (directoryFromDb == null) {
                    return new DirectoryPermission(false, false, false, false);
                }

                // Return full permission if the user is directory's owner
                if (directoryFromDb.getUser().getUsername().equals(username)) {
                    return new DirectoryPermission(true, true, true, true);
                }

                return new DirectoryPermission(false, false, true, true);
            } else {
                model.File fileFromDb = fileDao.getFileByPath(fromRootFilePath);
                if (fileFromDb == null) {
                    return new NormalFilePermission(NormalFilePermission.NULL_PERMISSION, false);
                }

                // Return full permission if the user is file's owner
                if (fileFromDb.getUser().getUsername().equals(username)) {
                    return new NormalFilePermission(NormalFilePermission.FULL_PERMISSION, true);
                }
                return new NormalFilePermission(NormalFilePermission.READABLE_PERMISSION, true);
            }

        }

        // Directory case
        if (fileType.equals(DIRECTORY_TYPE)) {
            Directory directoryFromDb = directoryDao.getDirectoryByPath(fromRootFilePath);
            if (directoryFromDb == null) {
                return new DirectoryPermission(false, false, false, false);
            }

            // Return full permission if the user is directory's owner
//            if (directoryFromDb.getUser().getUsername().equals(username)) {
//                return new DirectoryPermission(true, true, true, true);
//            }
            // Get directory's share permission
            List<ShareDirectories> directoryPermissions = directoryFromDb.getShareDirectories();
            ShareDirectories userPermission = directoryPermissions.stream()
                    .filter(permission -> permission.getUser().getUsername().equals(username))
                    .findFirst()
                    .orElse(null);

            // Return if share permission can't be found
            if (userPermission == null) {
                return new DirectoryPermission(false, false, false, true);
            }

            // Set directory share permission
            DirectoryPermission directoryPermission = new DirectoryPermission();
            directoryPermission.setCanModify(userPermission.isCanModify());
            directoryPermission.setDownloadable(userPermission.isDownloadPermission());
            directoryPermission.setUploadable(userPermission.isUploadPermission());

            return directoryPermission;
        } // Normal file case
        else {
            System.out.println("Fetching file: " + fromRootFilePath);
            // Fetch from file dao
            model.File fileFromDb = fileDao.getFileByPath(fromRootFilePath);
            if (fileFromDb == null) {
                return new NormalFilePermission(NormalFilePermission.NULL_PERMISSION, false);
            }

//            // Return full permission if the user is file's owner
//            if (fileFromDb.getUser().getUsername().equals(username)) {
//                return new NormalFilePermission(NormalFilePermission.FULL_PERMISSION, true);
//            }
            // Get file's share permission
            List<ShareFiles> filePermissions = fileFromDb.getShareFiles();
            ShareFiles userPermission = filePermissions.stream()
                    .filter(permission -> permission.getUser().getUsername().equals(username))
                    .findFirst()
                    .orElse(null);

            // Return null permission if share permission can't be found
            if (userPermission == null) {
                return new NormalFilePermission(NormalFilePermission.NULL_PERMISSION, true);
            }

            // Sset file read/full permission
            NormalFilePermission normalFilePermission = new NormalFilePermission(NormalFilePermission.NULL_PERMISSION, true);
            if (userPermission.getPermission().equals(NormalFilePermission.READABLE_PERMISSION)) {
                normalFilePermission.setPermission(NormalFilePermission.READABLE_PERMISSION);
            }
            if (userPermission.getPermission().equals(NormalFilePermission.FULL_PERMISSION)) {
                normalFilePermission.setPermission(NormalFilePermission.FULL_PERMISSION);
            }
            return normalFilePermission;
        }

    }

    public FilePermission getFilePermission(String fromRootFilePath, String username, String fileType) {
        FilePermission filePermission;
        User user = userDao.getUserByUserName(username);
        filePermission = getSingleFilePermission(fromRootFilePath, username, fileType, user.isAnonymous());

//        if (!filePermission.isExist()) {
//            return filePermission;
//        }
        if (filePermission.isShared()) {
            return filePermission;
        }

        // If the file/directory isn't shared then look up whether one of its parent directories is shared
        String parentPath = fromRootFilePath;
        DirectoryPermission parentDirPermission = new DirectoryPermission(false, false, false, true);
        while (!parentDirPermission.isShared()) {
            if (parentPath.equals(AppConfig.SERVER_FTP_FILE_PATH)) {
                parentDirPermission = new DirectoryPermission(false, false, false, true);
                break;
            }
            parentPath = ftpFileUtils.getParentPath(parentPath);
            parentDirPermission = (DirectoryPermission) getSingleFilePermission(parentPath, username, DIRECTORY_TYPE, user.isAnonymous());
        }

        if (fileType.equals(DIRECTORY_TYPE)) {
            return parentDirPermission;
        } else {
            String permission;
            if (parentDirPermission.isDownloadable() && parentDirPermission.isUploadable()) {
                permission = NormalFilePermission.FULL_PERMISSION;
            } else if (parentDirPermission.isDownloadable()) {
                permission = NormalFilePermission.READABLE_PERMISSION;
            } else {
                permission = NormalFilePermission.NULL_PERMISSION;
            }
            return new NormalFilePermission(permission, true);
        }

    }

//    public GetAnonymousFilesResult getAnonymousFiles(String username) throws AnonymousDisabledException {
//        GetAnonymousFilesResult result = new GetAnonymousFilesResult();
//        result.files = new ArrayList<>();
//        result.directories = new ArrayList<>();
//
//        User user = userDao.getUserByUserName(username);
//        if (!user.isAnonymous()) {
//            throw new AnonymousDisabledException();
//        }
//        File file = new File(AppConfig.SERVER_FTP_ANON_PATH);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//
//        File[] files = file.listFiles();
//        for (File f : files) {
//            String filePath = ftpFileUtils.convertJavaPathToFtpPath(f.getPath());
//            if (f.isDirectory()) {
//                Directory dir = directoryDao.getDirectoryByPath(filePath);
//                result.directories.add(dir);
//            }
//            if (f.isFile()) {
//                model.File fileFromDb = fileDao.getFileByPath(filePath);
//                result.files.add(fileFromDb);
//            }
//        }
//
//        return result;
//    }
    public String checkFileSize(String fromRootFilePath, int uploadBytes, String username) {
        File file = new File(fromRootFilePath);
        User user = userDao.getUserByUserName(username);
        long oldFileSize = FileUtils.sizeOf(file);
        if (uploadBytes > user.getMaxUploadFileSizeBytes()) {
            return CHECK_FILE_SIZE_EXCEED_UPLOAD_SIZE;
        }

        long newUsedBytes = (long) (uploadBytes - oldFileSize + user.getUsedBytes());
        if (newUsedBytes > user.getQuotaInBytes()) {
            return CHECK_FILE_SIZE_EXCEED_QUOTA;
        }

        return CHECK_FILE_SIZE_OK;
    }

    public List<FilePermissionWithUser> getSharedUsersPermissions(String filePath) {
        List<FilePermissionWithUser> filePermissions = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return filePermissions;
        }

        if (file.isFile()) {
            model.File fileInDb = fileDao.getFileByPath(filePath);
            List<ShareFiles> fileSharePermissionsInDb = fileInDb.getShareFiles();

            if (fileSharePermissionsInDb == null) {
                return filePermissions;
            }

            for (ShareFiles fileSharePermission : fileSharePermissionsInDb) {
                FilePermissionWithUser filePermissionWithUser = new FilePermissionWithUser();
                filePermissionWithUser.setFileType(NORMAL_FILE_TYPE);
                filePermissionWithUser.setUserInfo(publicUserMapper.userToPublicUserInfo(fileSharePermission.getUser()));
                filePermissionWithUser.setPermission(new NormalFilePermission(fileSharePermission.getPermission(), true));
                filePermissions.add(filePermissionWithUser);
            }
        } else {
            Directory dir = directoryDao.getDirectoryByPath(filePath);
            List<ShareDirectories> dirSharePermissionInDb = dir.getShareDirectories();

            if (dirSharePermissionInDb == null) {
                return filePermissions;
            }

            for (ShareDirectories dirSharePermission : dirSharePermissionInDb) {
                FilePermissionWithUser filePermissionWithUser = new FilePermissionWithUser();
                filePermissionWithUser.setFileType(DIRECTORY_TYPE);
                filePermissionWithUser.setUserInfo(publicUserMapper.userToPublicUserInfo(dirSharePermission.getUser()));
                filePermissionWithUser.setPermission(new DirectoryPermission(
                        dirSharePermission.isCanModify(),
                        dirSharePermission.isUploadPermission(),
                        dirSharePermission.isDownloadPermission(),
                        true
                ));

                filePermissions.add(filePermissionWithUser);
            }
        }

        return filePermissions;
    }

    public String listAllFilesInStringFormat(String path) {
        MLSDFormatter formatter = new MLSDFormatter();
        boolean isAnonymous = path.startsWith(AppConfig.SERVER_FTP_ANON_PATH);
        String formattedString = "";
        formattedString += formatter.listFormat(
                new File(path),
                (File file) -> {
                    if (file.isFile()) {
                        return new NormalFilePermission(NormalFilePermission.FULL_PERMISSION, true);
                    } else {
                        return new DirectoryPermission(true, true, true, true);
                    }
                },
                isAnonymous);
        return formattedString;
    }

    public String listAllAnonFilesInStringFormat(String path, String username) {
        MLSDFormatter formatter = new MLSDFormatter();
        return formatter.listFormat(new File(path), (file) -> {
            return getFilePermission(ftpFileUtils.convertJavaPathToFtpPath(file.getPath()), username, file.isDirectory() ? FileBus.DIRECTORY_TYPE : FileBus.NORMAL_FILE_TYPE).isReadable();
        }, (File file) -> {
            return getFilePermission(ftpFileUtils.convertJavaPathToFtpPath(file.getPath()), username, file.isDirectory() ? FileBus.DIRECTORY_TYPE : FileBus.NORMAL_FILE_TYPE);
        }, true);
    }

    public static void main(String[] args) {
        String str = new FileBus().listAllFilesInStringFormat(AppConfig.SERVER_FTP_FILE_PATH);
        System.out.println(str);
    }

}
