/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import java.util.Date;
import model.User;
import payload.UserDto;
import payload.response.UserDetailResponse;
import utils.DateUtils;

/**
 *
 * @author lamanhhai
 */
public class UserMapper {
    public User userDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        DateUtils dateUtils = new DateUtils();
        Date birthday = new Date(dateUtils.converDateToLong(userDto.getBirthday()));
        user.setBirthdate(birthday);
        user.setMaxDownloadFileSizeBytes(52428800); // 50 MB
        user.setMaxUploadFileSizeBytes(52428800); // 50 MB
        user.setQuotaInBytes(209715200); // 200 MB
        user.setAnonymous(false);
        user.setUsedBytes(0);
        user.setIsActive(0);
        user.setBlockDownload(false);
        user.setBlockUpload(false);
        return user;
    }
    
    public UserDetailResponse userToUserDetailResponse(User user) {
        UserDetailResponse udrs = new UserDetailResponse();
        udrs.setFirstName(user.getFirstName());
        udrs.setLastName(user.getLastName());
        udrs.setBirthdate(user.getBirthdate());
        udrs.setGender(user.getGender());
        udrs.setUsername(user.getUsername());
        udrs.setQuotaInBytes(user.getQuotaInBytes());
        udrs.setUsedBytes(user.getUsedBytes());
        udrs.setMaxDownloadFileSizeBytes(user.getMaxDownloadFileSizeBytes());
        udrs.setMaxUploadFileSizeBytes(user.getMaxUploadFileSizeBytes());
        udrs.setAnonymous(user.isAnonymous());
        udrs.setBlockDownload(user.isBlockDownload());
        udrs.setBlockUpload(user.isBlockUpload());
        
        return udrs;
    }
    
    public User userDetailResponseToUser(UserDetailResponse userDetail) {
        User user = new User();
        user.setFirstName(userDetail.getFirstName());
        user.setLastName(userDetail.getLastName());
        user.setBirthdate(userDetail.getBirthdate());
        user.setGender(userDetail.getGender());
        user.setUsername(userDetail.getUsername());
        user.setQuotaInBytes(userDetail.getQuotaInBytes());
        user.setUsedBytes(userDetail.getUsedBytes());
        user.setMaxDownloadFileSizeBytes(userDetail.getMaxDownloadFileSizeBytes());
        user.setMaxUploadFileSizeBytes(userDetail.getMaxUploadFileSizeBytes());
        user.setAnonymous(userDetail.isAnonymous());
        user.setBlockDownload(userDetail.isBlockDownload());
        user.setBlockUpload(userDetail.isBlockUpload());
        return user;
    }
}
