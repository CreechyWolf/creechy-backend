package com.creechy.site.user.dao;

import static org.jooq.impl.DSL.selectFrom;

import com.creechy.site.jooq.model.tables.Users;
import com.creechy.site.jooq.model.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserDao {
    private final DSLContext dsl;

    public UsersRecord fetchUser(String username) {
        return dsl.selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOne();
    }

    public boolean checkUsernameAvailable(String username) {
        return dsl.fetchExists(selectFrom(Users.USERS).where(Users.USERS.USERNAME.eq(username)));
    }

    public boolean createUser(UsersRecord user) {
        int rows = dsl.insertInto(Users.USERS).set(user).execute();
        return rows > 0;
    }
}
