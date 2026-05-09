package com.pilipala.dto;

import lombok.Data;

@Data
public class UserMessageExtendDTO {
    private String messageContent;
    private String messageContentReply;
    private Integer auditStatus;
}
