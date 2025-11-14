package com.creechy.site.visitor.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VisitorDTO {
    private final String ipAddress;
}
