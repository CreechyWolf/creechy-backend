package com.creechy.site.user.dao;

import static org.jooq.impl.DSL.selectFrom;

import com.creechy.site.jooq.model.tables.User;
import com.creechy.site.jooq.model.tables.records.UserRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserDao {
    private final DSLContext dsl;

    public UserRecord fetchUser(String username) {
        return dsl.selectFrom(User.USER).where(User.USER.USERNAME.eq(username)).fetchOne();
    }

    public boolean checkUsernameAvailable(String username) {
        return dsl.fetchExists(selectFrom(User.USER).where(User.USER.USERNAME.eq(username)));
    }

    public boolean createUser(UserRecord user) {
        int rows = dsl.insertInto(User.USER).set(user).execute();
        return rows > 0;
    }
}
