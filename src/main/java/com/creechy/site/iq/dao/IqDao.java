package com.creechy.site.iq.dao;

import com.creechy.site.iq.dto.IqDTO;
import com.creechy.site.jooq.model.tables.IqCount;
import com.creechy.site.jooq.model.tables.User;
import com.creechy.site.jooq.model.tables.records.IqCountRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class IqDao {
    private final DSLContext dsl;

    public List<IqDTO> getTopTenUsers() {
        return dsl.select(User.USER.USERNAME, IqCount.IQ_COUNT.AMOUNT)
                .from(IqCount.IQ_COUNT)
                .join(User.USER)
                .on(User.USER.ID.eq(IqCount.IQ_COUNT.USER_ID))
                .orderBy(IqCount.IQ_COUNT.AMOUNT.desc())
                .limit(10)
                .fetchInto(IqDTO.class);
    }

    public IqCountRecord loadUserIqByUsername(String username) {
        return dsl.select(IqCount.IQ_COUNT.fields())
                .from(IqCount.IQ_COUNT)
                .join(User.USER)
                .on(User.USER.ID.eq(IqCount.IQ_COUNT.USER_ID))
                .where(User.USER.USERNAME.eq(username))
                .fetchOneInto(IqCountRecord.class);
    }

    public boolean saveUserIq(IqCountRecord record) {
        record.attach(dsl.configuration());
        int rows = record.store();
        return rows > 0;
    }
}
