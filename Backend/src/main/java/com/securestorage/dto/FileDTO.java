package com.securestorage.dto;

import java.time.LocalDateTime;


public class FileDTO {
    private Long id;
    private String filename;
    private String fileType;
    private long fileSize;
    private String uploadedBy;
    private LocalDateTime uploadedAt;


    public FileDTO() {}

    public FileDTO(Long id, String filename, String fileType, long fileSize, String uploadedBy, LocalDateTime uploadedAt) {
        this.id = id;
        this.filename = filename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getFileType() { return fileType; }
    public long getFileSize() { return fileSize; }
    public String getUploadedBy() { return uploadedBy; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    public void setId(Long id) { this.id = id; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
