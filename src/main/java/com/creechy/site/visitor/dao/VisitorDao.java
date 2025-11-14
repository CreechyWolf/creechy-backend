package com.creechy.site.visitor.dao;

import com.creechy.site.jooq.model.tables.Visitor;
import com.creechy.site.jooq.model.tables.records.VisitorRecord;
import com.creechy.site.visitor.dto.VisitorDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class VisitorDao {
    private final DSLContext dsl;

    public List<VisitorDTO> getAllVisitors() {
        return dsl.select(Visitor.VISITOR.IP_ADDRESS)
                .from(Visitor.VISITOR)
                .fetchInto(VisitorDTO.class);
    }

    public boolean saveNewVisitor(VisitorRecord record) {
        record.attach(dsl.configuration());
        int rows = record.store();
        return rows > 0;
    }

    public VisitorRecord loadExistingVisitor(String ipAddress) {
        return dsl.selectFrom(Visitor.VISITOR)
                .where(Visitor.VISITOR.IP_ADDRESS.eq(ipAddress))
                .fetchOneInto(VisitorRecord.class);
    }
}
