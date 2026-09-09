package com.securestorage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = true)
    private String details;

    public AuditLog() {}

    public AuditLog(String username, String action, String details) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

//    public String setUsername(String username) {
//        this.username = username;
//        return this.username;
//    }
//    public String setAction(String action) {
//        this.action = action;
//        return this.action;
//    }
//    public String setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//        return this.timestamp.toString();
//    }
//    public String setDetails(String details) {
//        this.details = details;
//        return this.details;
//    }

}