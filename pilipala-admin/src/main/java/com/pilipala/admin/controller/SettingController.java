package com.pilipala.admin.controller;

import com.pilipala.component.RedisComponent;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.entity.query.UsersQuery;
import com.pilipala.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/setting")
@Validated
@Slf4j
public class SettingController extends ABaseController {
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() {
        return getSuccessResponseVo(redisComponent.getSysSettingDTO());
    }

    @RequestMapping("/saveSetting")
    public ResponseVO saveSetting(SysSettingDTO settingDTO) {
        redisComponent.saveSysSettingDTO(settingDTO);
        return getSuccessResponseVo(null);
    }
}
