package com.securestorage.repository;

import com.securestorage.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    // Find all files uploaded by a specific user (by user ID)
    List<FileEntity> findByUserId(Long userId);

    FileEntity findByFilename(String filename);

    List<FileEntity> findByUserIdAndFilenameContainingIgnoreCase(Long userId, String filename);

    List<FileEntity> findByFilenameContainingIgnoreCase(String filename);
}
