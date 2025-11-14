package com.creechy.site.iq.service;

import com.creechy.site.iq.dao.IqDao;
import com.creechy.site.iq.dto.IqDTO;
import com.creechy.site.jooq.model.tables.records.UserRecord;
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

    public boolean updateUserIq(UserDetails userDetails) {
        var user = loadUserRecordFromDetails(userDetails);
        if (user == null) {
            return false;
        }
        var userIq = dao.loadUserIqByUsername(userDetails.getUsername());
        userIq.setAmount(userIq.getAmount() + 1);
        return dao.saveUserIq(userIq);
    }

    public IqDTO loadCurrentIqByDetails(UserDetails userDetails) {
        var user = loadUserRecordFromDetails(userDetails);
        if (user == null) {
            return null;
        }
        String username = user.getUsername();
        var userIq = dao.loadUserIqByUsername(username);
        return IqDTO.builder().username(username).amount(userIq.getAmount()).build();
    }

    public List<IqDTO> getTopTenUsers() {
        return dao.getTopTenUsers();
    }

    private UserRecord loadUserRecordFromDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userService.loadUserRecordByUsername(userDetails.getUsername());
    }
}
