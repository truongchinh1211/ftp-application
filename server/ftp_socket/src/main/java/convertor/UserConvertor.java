/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package convertor;

import com.google.gson.Gson;
import payload.UserDto;
import utils.MP5Utils;

/**
 *
 * @author lamanhhai
 */
public class UserConvertor {
    private Gson gson = new Gson();
    
    public UserDto convertJsonToObject(String json) {
        UserDto userDto = new UserDto();
        userDto = gson.fromJson(json, UserDto.class);
        MP5Utils mP5Utils = new MP5Utils();
        String pwdHash = mP5Utils.getMD5Hash(userDto.getPassword());
        userDto.setPassword(pwdHash);
        return userDto;
    }
}
