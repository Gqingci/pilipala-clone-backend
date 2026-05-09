package com.pilipala.entity.vo;

import com.pilipala.entity.po.UserAction;
import com.pilipala.entity.po.VideoComment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VideoCommentResultVO {
    private PaginationResultVO<VideoComment> commentData;
    private List<UserAction> userActionList;
}