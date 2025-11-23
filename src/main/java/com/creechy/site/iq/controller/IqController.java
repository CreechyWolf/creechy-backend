package com.creechy.site.iq.controller;

import com.creechy.site.iq.dto.IqDTO;
import com.creechy.site.iq.service.IqService;
import com.creechy.site.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/iq")
@RequiredArgsConstructor
public class IqController {
    private final UserService userService;
    private final IqService service;

    @RolesAllowed({"USER"})
    @PostMapping("/update")
    public ResponseEntity<?> updateIqCount(@AuthenticationPrincipal UserDetails userDetails) {
        if (!service.updateUserIq(userDetails, 0)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/current")
    public ResponseEntity<IqDTO> fetchCurrentAmount(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.loadCurrentIqByDetails(userDetails));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<IqDTO>> fetchLeaderboardStats() {
        return ResponseEntity.ok(service.getTopTenUsers());
    }

    @PostMapping("/upload-session")
    public ResponseEntity<?> uploadGuestIq(
            @RequestBody Map<String, Integer> body,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        request.getSession().removeAttribute("guestIq");
        int amount = body.getOrDefault("amount", 0);
        if (amount <= 0) {
            return ResponseEntity.ok().build();
        }
        log.info("Uploading {} guest IQ points for user {}", amount, userDetails.getUsername());
        service.updateUserIq(userDetails, amount);

        return ResponseEntity.ok().build();
    }
}
