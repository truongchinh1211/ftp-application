/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payloads;

import java.util.List;
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
public class UserPermissionResponse {
    private int status;
    private String message;
    private List<UserPermission> list;
    public UserPermissionResponse(String response){
        status = Integer.parseInt(response.substring(0, 3));
        message = response.substring(3).trim();
    }
}
