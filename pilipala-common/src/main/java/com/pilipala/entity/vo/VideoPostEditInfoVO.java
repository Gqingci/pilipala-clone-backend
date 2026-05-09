package com.pilipala.entity.vo;

import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.po.VideoPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoPostEditInfoVO {
    private VideoPost videoInfo;
    private List<VideoFilePost> videoInfoFileList;
}
