package com.pilipala.entity.enums;

import lombok.Getter;

@Getter
public enum VideoFileUpdateTypeEnum {
    NO_UPDATE(0, "无更新"),
    UPDATE(1, "更新");
    private Integer status;
    private String desc;

    VideoFileUpdateTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
