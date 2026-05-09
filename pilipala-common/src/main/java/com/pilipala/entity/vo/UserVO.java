package com.pilipala.entity.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserVO implements Serializable {
    private static final long serialVersionUID = 5654467644282863595L;

    private String id;
    private String username;
    private String avatar;
    private Integer gender;
    private String biography;
    private String spaceAnnouncement;
    private String grade;
    private String birthday;
    private String school;
    private Integer fansCount;
    private Integer focusCount;
    private Integer likeCount;
    private Integer playCount;
    private Boolean haveFocus;
}
