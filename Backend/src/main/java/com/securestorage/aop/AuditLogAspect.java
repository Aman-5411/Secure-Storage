package com.securestorage.aop;

import com.securestorage.model.AuditLog;
import com.securestorage.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    public AuditLogAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @AfterReturning(pointcut = "@annotation(auditAnnotation)")
    public void logAudit(JoinPoint joinPoint, Audit auditAnnotation) {

        // Get the current user's email
        String username = "ANONYMOUS"; // Default if no user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        }

        // Get the action from the annotation
        String action = auditAnnotation.action();

        // Get details (like file ID) from the request
        String details = "No details";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            // This is a simple way to get a path variable like /api/files/{id}
            String path = request.getRequestURI();
            String[] parts = path.split("/");
            if (parts.length > 0) {
                details = "File ID: " + parts[parts.length - 1];
            }
        } catch (Exception e) {
            // Ignore if we can't get request details
        }

        // Save the log
        AuditLog log = new AuditLog(username, action, details);
        auditLogRepository.save(log);
    }
}