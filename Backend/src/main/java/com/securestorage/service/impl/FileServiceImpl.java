package com.securestorage.service.impl;

import com.securestorage.dto.FileDTO;
import com.securestorage.model.FileEntity;
import com.securestorage.model.User;
import com.securestorage.repository.FileRepository;
import com.securestorage.repository.UserRepository;
import com.securestorage.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    //Inject the secret key
    @Value("${app.encryption.secret-key}")
    private String encryptionKeyString;

    private static final String ALGORITHM = "AES";

    // Simple constant for this example. A more secure approach uses AES/GCM/NoPadding
    // but requires storing an Initialization Vector (IV) per file.
    // For this project, ECB is simple and self-contained.
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

 //   private static final int GCm_IV_Length = 12;

 //   private static final int GCM_TAG_LENGTH = 128;

 //   private final SecureRandom secureRandom = new SecureRandom();


    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    // ---Helper method to get the encryption key ---
    private Key getEncryptionKey() {
        byte[] keyBytes = encryptionKeyString.getBytes();
        return new SecretKeySpec(keyBytes, 0, 32, ALGORITHM);
    }

    // --- Helper method to encrypt data ---
    private byte[] encrypt(byte[] data) throws Exception {
        Key key = getEncryptionKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // --- Helper method to decrypt data ---
    private byte[] decrypt(byte[] encryptedData) throws Exception {
        Key key = getEncryptionKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }


    // --- uploadFile method ---
    @Override
    public FileEntity uploadFile(MultipartFile file, User user) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(file.getOriginalFilename());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setFileSize(file.getSize());

        try {
            // Encrypt the file data before saving
            byte[] encryptedData = encrypt(file.getBytes());
           // System.out.println("Original Size: " + file.getSize());
           // System.out.println("Encrypted Size: " + encryptedData.length);
           // System.out.println("Encrypted Data Preview: " + java.util.Arrays.toString(java.util.Arrays.copyOf(encryptedData, 20)));

            fileEntity.setData(encryptedData);

        } catch (Exception e) {
            // If encryption fails,Return a IOException
            throw new IOException("Could not encrypt file data: " + e.getMessage(), e);
        }

        fileEntity.setUser(user);
        fileEntity.setUploadedAt(LocalDateTime.now());

        return fileRepository.save(fileEntity);
    }

    // ---downloadFile method ---
    @Override
    public byte[] downloadFile(Long fileId, String requesterEmail) throws IOException {

        FileEntity fe = getFileByIdForUser(fileId, requesterEmail);

        try {
            // Decrypt the data before returning it
            return decrypt(fe.getData());

        } catch (Exception e) {
            // If decryption fails, wrap it in an IOException
            throw new IOException("Could not decrypt file data: " + "id" + fileId + " - " + e.getMessage(), e);
        }
    }
    // Get all files for Admin(metadata only)
    @Override
    public List<FileDTO> getAllFilesForUser(String userEmail, String query) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FileEntity> files;

        if(query != null && !query.isBlank()) {
            files = fileRepository.findByUserIdAndFilenameContainingIgnoreCase(user.getId(), query);
        }
        else{
            files = fileRepository.findByUserId(user.getId());
        }
        return files.stream()
                .map(file -> new FileDTO(
                        file.getId(),
                        file.getFilename(),
                        file.getFileType(),
                        file.getFileSize(),
                        user.getEmail(),
                        file.getUploadedAt()
                ))
                .toList();
    }

    // Delete file (only owner or admin)
    @Override
    public void deleteFile(Long fileId, String requesterEmail) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Owner allowed
        if (fileEntity.getUser() != null &&
                fileEntity.getUser().getEmail().equalsIgnoreCase(requesterEmail)) {
            fileRepository.deleteById(fileId);
            return;
        }

        // Admin allowed
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Requester user not found"));

        if ("ROLE_ADMIN".equalsIgnoreCase(requester.getRole())) {
            fileRepository.deleteById(fileId);
            return;
        }

        throw new RuntimeException("Access denied: you are not the owner or admin");
    }

    // Get all files for admin (metadata only)
    @Override
    public List<FileDTO> getAllFiles(String query) {
        List<FileEntity> files;
        if (query != null && !query.isBlank()) {
            files = fileRepository.findByFilenameContainingIgnoreCase(query);
        } else {
            files = fileRepository.findAll();
        }

        return files.stream()
                .map(file -> new FileDTO(
                        file.getId(),
                        file.getFilename(),
                        file.getFileType(),
                        file.getFileSize(),
                        file.getUser() != null ? file.getUser().getEmail() : "Unknown",
                        file.getUploadedAt()
                ))
                .toList();
    }

    // This method is fine, it just fetches the entity
    @Override
    public FileEntity getFileByIdForUser(Long fileId, String requesterEmail) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Owner can access
        if (fileEntity.getUser() != null &&
                fileEntity.getUser().getEmail().equalsIgnoreCase(requesterEmail)) {
            return fileEntity;
        }
        throw new RuntimeException("Access denied: you are not the owner");
    }
}