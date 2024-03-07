/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payloads;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Bum
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {
    @SerializedName("fileType")
    private String fileType;
    
    @SerializedName("userInfo")
    private UserData userData;
    
    @SerializedName("permission")
    private JsonElement permission;
    
    
        public HashMap<String, Object> getProcessedPermission() {
        // Xử lý và trả về giá trị permission dưới dạng HashMap
        if (permission.isJsonObject()) {
            return new Gson().fromJson(permission.getAsJsonObject(), HashMap.class);
        } else if (permission.isJsonPrimitive()) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("permission", permission.getAsString());
            return result;
        } else {
            return new HashMap<>();
        }
    }
}
