/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lamanhhai
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "anonymous")
    private boolean anonymous;

    @Column(name = "max_download_file_size_bytes")
    private long maxDownloadFileSizeBytes;

    @Column(name = "max_upload_file_size_bytes")
    private long maxUploadFileSizeBytes;

    @Column(name = "quota_in_bytes")
    private long quotaInBytes;

    @Column(name = "used_bytes")
    private long usedBytes;

    @Column(name = "is_active")
    private int isActive;

    @Column(name = "otp")
    private String otp;

    @Column(name = "create_date_otp")
    private LocalDateTime createDateOtp;

    @Column(name = "is_block_upload")
    private boolean isBlockUpload;

    @Column(name = "is_block_download")
    private boolean isBlockDownload;

    @OneToMany(mappedBy = "user")
    private List<File> files;

    @OneToMany(mappedBy = "user")
    private List<Directory> directories;

    @OneToMany(mappedBy = "user")
    private List<ShareDirectories> shareDirectories;

    @OneToMany(mappedBy = "user")
    private List<ShareFiles> shareFiles;

}
