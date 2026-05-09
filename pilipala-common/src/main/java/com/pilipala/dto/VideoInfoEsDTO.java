package com.pilipala.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class VideoInfoEsDTO {
    private String videoId;
    private String videoCover;
    private String videoName;
    private String userId;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String tags;
    private Integer playCount;
    private Integer danmuCount;
    private Integer collectCount;

}
