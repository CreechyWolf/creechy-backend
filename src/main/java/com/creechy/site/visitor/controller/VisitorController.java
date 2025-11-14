package com.creechy.site.visitor.controller;

import com.creechy.site.visitor.dto.VisitorDTO;
import com.creechy.site.visitor.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visitor")
@RequiredArgsConstructor
public class VisitorController {
    private final VisitorService service;

    @PostMapping("/track")
    public ResponseEntity<?> trackVisitor(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        if (!service.saveVisitor(ipAddress)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/heatmap")
    public ResponseEntity<List<VisitorDTO>> loadHeatmap() {
        return ResponseEntity.ok(service.getAllVisitors());
    }
}
