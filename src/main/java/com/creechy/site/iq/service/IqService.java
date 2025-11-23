package com.creechy.site.iq.service;

import com.creechy.site.iq.dao.IqDao;
import com.creechy.site.iq.dto.IqDTO;
import com.creechy.site.jooq.model.tables.records.IqCountRecord;
import com.creechy.site.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IqService {
    private final IqDao dao;
    private final UserService userService;

    public boolean updateUserIq(UserDetails userDetails, int amount) {
        var userIq = loadUserRecordFromDetails(userDetails);
        if (userIq == null) {
            return false;
        }
        int currentUserIq = userIq.getAmount();
        userIq.setAmount(amount > 0 ? currentUserIq + amount : currentUserIq + 1);
        return dao.saveUserIq(userIq);
    }

    public IqDTO loadCurrentIqByDetails(UserDetails userDetails) {
        var userIq = loadUserRecordFromDetails(userDetails);
        if (userIq == null) {
            return null;
        }
        return IqDTO.builder()
                .username(userDetails.getUsername())
                .amount(userIq.getAmount())
                .build();
    }

    public List<IqDTO> getTopTenUsers() {
        return dao.getTopTenUsers();
    }

    private IqCountRecord loadUserRecordFromDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        var user = userService.loadUserRecordByUsername(userDetails.getUsername());
        if (user == null) {
            return null;
        }
        var userIq = dao.loadUserIqByUsername(userDetails.getUsername());
        if (userIq == null) {
            userIq = new IqCountRecord();
            userIq.setUserId(user.getId());
            userIq.setAmount(0);
        }
        return userIq;
    }
}
