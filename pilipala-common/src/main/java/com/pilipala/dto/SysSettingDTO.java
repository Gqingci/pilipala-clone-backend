package com.pilipala.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SysSettingDTO implements Serializable {
    private static final long serialVersionUID = 129858603121L;
    private Integer registerCoinCount = 10;
    private Integer postVideoCoinCount = 5;
    private Integer videoSize = 10;
    private Integer videoPCount = 100;
    private Integer videoCount = 10;
    private Integer commentCount = 20;
    private Integer danmuCount = 20;
}
