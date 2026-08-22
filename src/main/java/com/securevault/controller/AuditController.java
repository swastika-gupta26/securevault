package com.securevault.controller;

import com.securevault.entity.AuditLog;
import com.securevault.repository.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(
        name = "Audit Logs",
        description = "Admin-only APIs for viewing security and activity logs"
)
@RestController
@RequestMapping("/api/audit-logs")
@SecurityRequirement(name = "bearerAuth")
public class AuditController {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Operation(
            summary = "Get all audit logs",
            description = "Returns security and credential activity logs"
    )
    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
