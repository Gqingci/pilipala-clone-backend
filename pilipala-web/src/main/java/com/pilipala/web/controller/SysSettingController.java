package com.pilipala.web.controller;

import com.pilipala.component.RedisComponent;
import com.pilipala.config.AppConfig;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.dto.UploadingFileDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.Constants;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/sysSetting")
@Validated
@Slf4j
public class SysSettingController extends ABaseController {
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        return getSuccessResponseVo(redisComponent.getSysSettingDTO());
    }
}
