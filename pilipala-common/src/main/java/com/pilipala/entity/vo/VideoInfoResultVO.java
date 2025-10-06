package com.pilipala.entity.vo;

import com.pilipala.entity.po.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoResultVO {
    private Video video;
    private List userActionList;
}
