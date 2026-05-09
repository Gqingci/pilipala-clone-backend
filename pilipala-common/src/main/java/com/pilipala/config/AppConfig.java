package com.pilipala.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${admin.account:}")
    private String adminAccount;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private String esHostPort;

    @Value("${es.index.video.name:pilipala_video}")
    private String esIndexVideoName;

    @Value("${showFFmpegLog:true}")
    private Boolean showFFmpegLog;
}
