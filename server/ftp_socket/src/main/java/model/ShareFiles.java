/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.ids.ShareFilesId;

/**
 *
 * @author lamanhhai
 */

@Entity
@Table(name = "share_files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareFiles {
    
    @EmbeddedId
    private ShareFilesId ids;
    
    @Column(name = "permission")
    private String permission;
    
    @ManyToOne
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private File file;
    
    @ManyToOne()
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
