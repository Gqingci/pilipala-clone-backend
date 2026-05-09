package com.pilipala.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoPlayInfoDTO implements Serializable {
    private static final long serialVersionUID = 8429069135432287607L;
    private String videoId;
    private String userId;
    private Integer fileIndex;
}
