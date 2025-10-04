package com.pilipala.entity.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoStatusCountVO {
    private Integer auditPassCount;
    private Integer auditFailCount;
    private Integer inProgress;

}
