/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payload;

import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;

/**
 *
 * @author User
 */
@Builder()
public class PublicUserInfo {

    private String username;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String gender;
    private boolean anonymous;
    private boolean isBlockDownload;
    private boolean isBlockUpload;
    private long maxUploadSizeBytes;
    private long maxDownloadSizeBytes;
    private long quotaInBytes;
    private long usedBytes;
}
