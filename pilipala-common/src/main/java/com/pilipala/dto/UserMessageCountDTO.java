package com.pilipala.dto;

import lombok.Data;

@Data
public class UserMessageCountDTO {
    public Integer messageType;
    private Integer messageCount;
}
