package seeder;

import config.AppConfig;
import dao.DirectoryDao;
import dao.FileDao;
import dao.ShareDirectoriesDao;
import dao.ShareFilesDao;
import dao.UserDao;
import ftp.NormalFilePermission;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;
import model.Directory;
import model.ShareDirectories;
import model.ShareFiles;
import model.User;
import model.ids.ShareDirectoriesId;
import model.ids.ShareFilesId;
import org.apache.commons.io.FileUtils;
import utils.MP5Utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author User
 */
public class Seeder {

    private static DirectoryDao directoryDao = new DirectoryDao();
    private static FileDao fileDao = new FileDao();
    private static UserDao userDao = new UserDao();
    private static ShareDirectoriesDao shareDirectoriesDao = new ShareDirectoriesDao();
    private static ShareFilesDao shareFilesDao = new ShareFilesDao();

    private static Directory createDir(String path, User user, List<ShareDirectories> sharePermissions) throws IOException {
        Directory directory = new Directory(0, path, user, sharePermissions);
        directoryDao.save(directory);
        shareDirectoriesDao.save(new ShareDirectories(new ShareDirectoriesId(
                directory.getId(), user.getId()),
                true,
                true,
                true,
                directory,
                user
        ));
        File file = new File(path);
        file.mkdirs();
        return directory;
    }

    private static model.File createFile(String path, String content, User user, List<ShareFiles> sharePermissions) throws IOException {
        model.File fileModel = new model.File(0, path, user, sharePermissions);
        fileDao.save(fileModel);
        shareFilesDao.save(new ShareFiles(new ShareFilesId(
                fileModel.getId(), user.getId()),
                NormalFilePermission.FULL_PERMISSION,
                fileModel,
                user
        ));
        File file = new File(path);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
        return fileModel;
    }

    public static void main(String[] args) throws IOException, ParseException {
        MP5Utils md5Utils = new MP5Utils();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        User user1 = new User();
        user1.setUsername("vanan@gmail.com");
        user1.setPassword(md5Utils.getMD5Hash("test"));
        user1.setBirthdate(dateFormat.parse("31/6/1998"));
        user1.setFirstName("An");
        user1.setLastName("Tran Van");
        user1.setGender("Nam");
        user1.setIsActive(1);
        user1.setCreateDateOtp(LocalDateTime.of(2023, Month.NOVEMBER, 14, 7, 33));
        user1.setMaxDownloadFileSizeBytes(314572800); // 300 MB
        user1.setMaxUploadFileSizeBytes(314572800); // 300 MB
        user1.setQuotaInBytes(1073741824); // 1 GB
        userDao.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("tuyetle@gmail.com");
        user2.setPassword(md5Utils.getMD5Hash("test2"));
        user2.setBirthdate(dateFormat.parse("22/11/1996"));
        user2.setFirstName("Tuyet");
        user2.setLastName("Le Thi");
        user2.setGender("Nữ");
        user2.setIsActive(1);
        user2.setCreateDateOtp(LocalDateTime.of(2023, Month.NOVEMBER, 17, 9, 22));
        user2.setMaxDownloadFileSizeBytes(314572800); // 300 MB
        user2.setMaxUploadFileSizeBytes(314572800); // 300 MB
        user2.setQuotaInBytes(1073741824); // 1 GB
        userDao.save(user2);
        
        User user3 = new User();
        user2.setId(3);
        user2.setUsername("notactive@gmail.com");
        user2.setPassword(md5Utils.getMD5Hash("test3"));
        user2.setBirthdate(dateFormat.parse("5/2/1998"));
        user2.setFirstName("Tran");
        user2.setLastName("Thanh Tam");
        user2.setGender("Nam");
        user2.setIsActive(0);
        user2.setMaxDownloadFileSizeBytes(314572800); // 300 MB
        user2.setMaxUploadFileSizeBytes(314572800); // 300 MB
        user2.setQuotaInBytes(1073741824); // 1 GB
        userDao.save(user3);

        File file = new File(AppConfig.SERVER_FTP_ANON_PATH);
        file.mkdirs();

//        createDir(AppConfig.SERVER_FTP_USERS_PATH, rootUser, null);
        createDir(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com", user1, null);
        Directory sharedDir = createDir(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com/english", user1, null);
        createDir(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com/images", user1, null);
        createDir(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com/documents", user1, null);
        createDir(AppConfig.SERVER_FTP_USERS_PATH + "/tuyetle@gmail.com", user2, null);

        model.File sharedReadableFile = createFile(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com/english/readable-share-file.txt", "Readable", user1, null);
        model.File sharedFullPermissionFile = createFile(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com/full-permission-share-file.txt", "Full permission", user1, null);

        shareFilesDao.save(new ShareFiles(new ShareFilesId(sharedReadableFile.getId(), user2.getId()), NormalFilePermission.READABLE_PERMISSION, sharedReadableFile, user2));
        shareFilesDao.save(new ShareFiles(new ShareFilesId(sharedFullPermissionFile.getId(), user2.getId()), NormalFilePermission.FULL_PERMISSION, sharedFullPermissionFile, user2));

        shareDirectoriesDao.save(new ShareDirectories(new ShareDirectoriesId(sharedDir.getId(), user2.getId()), false, false, true, sharedDir, user2));
        
        user1.setUsedBytes(FileUtils.sizeOf(new File(AppConfig.SERVER_FTP_USERS_PATH + "/vanan@gmail.com")));
        userDao.update(user1);
        user2.setUsedBytes(FileUtils.sizeOf(new File(AppConfig.SERVER_FTP_USERS_PATH + "/tuyetle@gmail.com")));
        userDao.update(user2);
    }
}
