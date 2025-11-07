package com.creechy.site.user.service;

import com.creechy.site.jooq.model.tables.records.UsersRecord;
import com.creechy.site.user.dao.UserDao;
import com.creechy.site.user.request.UserRequest;
import com.creechy.site.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserDao dao;
    private final SecurityUtil securityUtil;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String username)
            throws UsernameNotFoundException {
        var user = dao.fetchUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.builder()
                .username(username)
                .password(user.getHashedPassword())
                .roles("USER")
                .build();
    }

    public boolean createUser(UserRequest request) {
        if (dao.checkUsernameAvailable(request.getUsername())) {
            var user = new UsersRecord();
            user.setUsername(request.getUsername());
            user.setHashedPassword(securityUtil.hashPassword(request.getPassword()));
            return dao.createUser(user);
        }
        return false;
    }
}
