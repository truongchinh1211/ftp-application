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
import model.ids.ShareDirectoriesId;

/**
 *
 * @author lamanhhai
 */
@Entity
@Table(name = "share_directories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareDirectories {

    @EmbeddedId
    private ShareDirectoriesId ids;

    @Column(name = "can_modify")
    private boolean canModify;

    @Column(name = "upload_permission")
    private boolean uploadPermission;

    @Column(name = "download_permission")
    private boolean downloadPermission;

    @ManyToOne()
    @JoinColumn(name = "directory_id", insertable = false, updatable = false)
    private Directory directory;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

}
