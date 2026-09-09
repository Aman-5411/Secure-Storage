package com.securestorage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String fileType;
    private Long fileSize;

    //@Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] data;

    @Column(name = "Uploaded_At")
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Constructors
    public FileEntity() {}

    public FileEntity(String filename, String fileType, Long fileSize, byte[] data, User user,LocalDateTime uploadedAt) {
        this.filename = filename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.data = data;
        this.user = user;
        this.uploadedAt = uploadedAt;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getUploadedAt() {return uploadedAt;}
    public void setUploadedAt(LocalDateTime uploadedAt) {this.uploadedAt = uploadedAt;}
}
