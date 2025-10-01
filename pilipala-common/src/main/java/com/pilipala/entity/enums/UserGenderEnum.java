package com.pilipala.entity.enums;

public enum UserGenderEnum {
    Male(1, "男"),
    Female( 0, "女"),
    SECRECY(2, "保密");

    private Integer type;
    private String desc;

    UserGenderEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static UserGenderEnum getByType(Integer type) {
        for (UserGenderEnum item : UserGenderEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}
