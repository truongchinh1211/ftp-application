/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payload.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lamanhhai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String gender;
    private String username;
    private long quotaInBytes;
    private long usedBytes;
    private long maxDownloadFileSizeBytes;
    private long maxUploadFileSizeBytes;
    private boolean anonymous;
    private boolean isBlockUpload;
    private boolean isBlockDownload;

    @Override
    public String toString() {
        return "UserDetailResponse{" + "firstName=" + firstName + ", lastName=" + lastName + ", birthdate=" + birthdate + ", gender=" + gender + ", username=" + username + ", quotaInBytes=" + quotaInBytes + ", usedBytes=" + usedBytes + ", maxDownloadFileSizeBytes=" + maxDownloadFileSizeBytes + ", maxUploadFileSizeBytes=" + maxUploadFileSizeBytes + ", anonymous=" + anonymous + ", isBlockUpload=" + isBlockUpload + ", isBlockDownload=" + isBlockDownload + '}';
    }
    
}
