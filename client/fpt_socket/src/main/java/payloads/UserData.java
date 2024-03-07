/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payloads;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Son
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @SerializedName("username")
    private String username;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthdate")
    private Date birthdate;
    @SerializedName("gender")
    private String gender;
    @SerializedName("anonymous")
    private boolean anonymous;
    @SerializedName("isBlockDownload")
    private boolean isBlockDownload;
    @SerializedName("isBlockUpload")
    private boolean isBlockUpload;
    @SerializedName("maxUploadSizeBytes")
    private long maxUploadSizeBytes;
    @SerializedName("maxDownloadSizeBytes")
    private long maxDownloadSizeBytes;
    @SerializedName("quotaInBytes")
    private long quotaInBytes;
    @SerializedName("usedBytes")
    private long usedBytes;
}
