/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lamanhhai
 */

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareFilesId implements Serializable{
    
    @Column(name = "file_id")
    private int fileId;
    
    @Column(name = "user_id")
    private int userId;
}
