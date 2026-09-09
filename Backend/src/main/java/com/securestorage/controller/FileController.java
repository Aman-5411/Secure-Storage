package com.securestorage.controller;

import com.securestorage.dto.FileDTO;
import com.securestorage.model.FileEntity;
import com.securestorage.model.User;
import com.securestorage.repository.UserRepository;
import com.securestorage.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import com.securestorage.aop.Audit;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final UserRepository userRepository;

    public FileController(FileService fileService, UserRepository userRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    // Upload a file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             Authentication authentication) throws IOException {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        fileService.uploadFile(file, user);
        return ResponseEntity.ok("File uploaded successfully by " + user.getEmail());
    }

    // Download a file by ID (only owner)
    @GetMapping("/download/{id}")
    @Audit(action = "DOWNLOAD_FILE")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        try {
            FileEntity file = fileService.getFileByIdForUser(id, email);
            byte[] decryptedData = fileService.downloadFile(id, email);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(new ByteArrayResource(decryptedData));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  List all files uploaded by user
    @GetMapping
    public List<FileDTO> getAllFiles(Authentication authentication, @RequestParam(required = false) String query) {
        String email = authentication.getName();
        return fileService.getAllFilesForUser(email, query);
    }

    //  Delete a file by ID (only owner or admin)
    @DeleteMapping("/{id}")
    @Audit(action = "DELETE_FILE")
    public ResponseEntity<String> deleteFile(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        fileService.deleteFile(id, email);
        return ResponseEntity.ok(" File deleted successfully");
    }

    // (ADMIN ONLY) View all uploaded files (metadata only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<FileDTO> getAllFilesForAdmin(Authentication authentication, @RequestParam(required = false) String query) {
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"ROLE_ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Access denied — Only ADMIN can view all files.");
        }

        return fileService.getAllFiles(query);
    }
}
