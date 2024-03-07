/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author User
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserFileLimits {

    private Integer quotaBytes;
    private Integer maxDownloadFileSizeBytes;
    private Integer maxUploadFileSizeBytes;
}
