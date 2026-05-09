package com.pilipala.entity.vo;

import com.pilipala.entity.po.UserVideoSeries;
import com.pilipala.entity.po.UserVideoSeriesVideo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserVideoSeriesDetailVO {
    private UserVideoSeries userVideoSeries;
    private List<UserVideoSeriesVideo> userVideoSeriesVideoList;
}
