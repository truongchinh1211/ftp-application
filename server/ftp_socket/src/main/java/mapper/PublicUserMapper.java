/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import model.User;
import payload.PublicUserInfo;

/**
 *
 * @author User
 */
public class PublicUserMapper {

    public PublicUserInfo userToPublicUserInfo(User user) {
        return PublicUserInfo.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .username(user.getUsername())
                .birthdate(user.getBirthdate())
                .anonymous(user.isAnonymous())
                .isBlockDownload(user.isBlockDownload())
                .isBlockUpload(user.isBlockUpload())
                .maxDownloadSizeBytes(user.getMaxDownloadFileSizeBytes())
                .maxUploadSizeBytes(user.getMaxUploadFileSizeBytes())
                .quotaInBytes(user.getQuotaInBytes())
                .usedBytes(user.getUsedBytes())
                .build();
    }
}
