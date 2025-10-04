package com.pilipala.entity.enums;

import lombok.Getter;

/**
 * @author Gqingci
 */
@Getter
public enum DateTimePatternEnum {

    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"), YYYY_MM_DD("yyyy-MM-dd"), YYYY_MM("yyyyMM"), YYYYMMDD("yyyyMMdd");

    private String pattern;

    DateTimePatternEnum(String pattern) {
        this.pattern = pattern;
    }
}
