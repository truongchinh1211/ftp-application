/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payloads;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author User
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateData {

    private String username;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String gender;
    private boolean anonymous;
    private boolean isBlockUpload;
    private boolean isBlockDownload;
    private long quotaInBytes;
    private long usedBytes;
    private long maxDownloadFileSizeBytes;
    private long maxUploadFileSizeBytes;

}
