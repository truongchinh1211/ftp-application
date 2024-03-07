/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payload;

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
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;

    @Override
    public String toString() {
        return "/n username: " + getUsername() +
               "/n password: " + getPassword() +
               "/n firstName: " + getFirstName() +
               "/n lastName: " + getLastName() +
               "/n gender: " + getGender() +
               "/n birthday: " + getBirthday();
    }
    
    
}
