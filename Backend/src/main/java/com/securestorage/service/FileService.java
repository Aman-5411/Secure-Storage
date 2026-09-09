package com.securestorage.service;

import com.securestorage.dto.FileDTO;
import com.securestorage.model.FileEntity;
import com.securestorage.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface FileService {
    FileEntity uploadFile(MultipartFile file, User user) throws IOException;
    byte[] downloadFile(Long fileId, String requesterEmail) throws IOException;
    FileEntity getFileByIdForUser(Long fileId, String requesterEmail);
    List<FileDTO> getAllFilesForUser(String userEmail, String query);
    void deleteFile(Long fileId, String requesterEmail);
    List<FileDTO> getAllFiles(String query);
}
