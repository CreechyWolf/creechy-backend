package com.creechy.site.visitor.service;

import com.creechy.site.jooq.model.tables.records.VisitorRecord;
import com.creechy.site.visitor.dao.VisitorDao;
import com.creechy.site.visitor.dto.VisitorDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VisitorService {
    private final VisitorDao dao;

    public boolean saveVisitor(String ipAddress) {
        var visitor = dao.loadExistingVisitor(ipAddress);
        if (visitor == null) {
            visitor = new VisitorRecord();
            visitor.setIpAddress(ipAddress);
        }
        visitor.setLastVisited(LocalDateTime.now());

        return dao.saveNewVisitor(visitor);
    }

    public List<VisitorDTO> getAllVisitors() {
        return dao.getAllVisitors();
    }
}
