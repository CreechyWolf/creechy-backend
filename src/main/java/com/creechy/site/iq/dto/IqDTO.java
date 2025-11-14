package com.creechy.site.iq.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IqDTO {
    private final String username;
    private final int amount;
}
