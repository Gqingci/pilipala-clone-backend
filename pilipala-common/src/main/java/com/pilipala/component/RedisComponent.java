package com.pilipala.component;

import com.pilipala.config.AppConfig;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.dto.UploadingFileDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.DateTimePatternEnum;
import com.pilipala.entity.po.Category;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.redis.RedisUtils;
import com.pilipala.utils.Constants;
import com.pilipala.utils.DateUtils;
import com.pilipala.utils.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.pilipala.utils.RedisConstants;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AppConfig appConfig;

    // 存储用户注册验证码
    public String saveRegisterCaptcha(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }

    // 存储用户登录验证码
    public String saveLoginCaptcha(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }

    // 获取用户注册验证码
    public String getRegisterCaptcha(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey);
    }

    // 获取用户登录验证码
    public String getLoginCaptcha(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey);
    }

    // 清除用户注册验证码
    public void cleanRegisterCaptcha(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_REGISTER_CAPTCHA_KEY + captchaKey);
    }

    // 清除用户登录验证码
    public void cleanLoginCaptcha(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_LOGIN_CAPTCHA_KEY + captchaKey);
    }

    // 存储用户的Token
    public void saveToken(UserTokenInfoDTO userTokenInfoDTO) {
        String token = UUID.randomUUID().toString();
        userTokenInfoDTO.setExpireAt(System.currentTimeMillis() + RedisConstants.LOGIN_USER_TTL);
        userTokenInfoDTO.setToken(token);
        redisUtils.setex(RedisConstants.REDIS_WEB_TOKEN_KEY + token, userTokenInfoDTO, RedisConstants.LOGIN_USER_TTL);
    }

    // 清除用户的Token
    public void cleanToken(String token) {
        redisUtils.delete(RedisConstants.REDIS_WEB_TOKEN_KEY + token);
    }

    // 获取用户的相关信息
    public UserTokenInfoDTO getUserTokenInfoDTO(String token) {
        return (UserTokenInfoDTO) redisUtils.get(RedisConstants.REDIS_WEB_TOKEN_KEY + token);
    }

    // 存储管理员的Token
    public String saveToken4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_ADMIN_TOKEN_KEY + token, account, RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY);
        return token;
    }

    // 获取管理员的Token
    public String getTokenInfo4Admin(String token) {
        return (String) redisUtils.get(RedisConstants.REDIS_ADMIN_TOKEN_KEY + token);
    }

    // 清除管理员的Token
    public void cleanToken4Admin(String token) {
        redisUtils.delete(RedisConstants.REDIS_ADMIN_TOKEN_KEY + token);
    }

    // 获取管理员登录的验证码
    public String getLoginCaptcha4Admin(String captchaKey) {
        return (String) redisUtils.get(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey);
    }

    // 清除管理员登录的验证码
    public void cleanLoginCaptcha4Admin(String captchaKey) {
        redisUtils.delete(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey);
    }

    // 存储管理员登录的验证码
    public String saveLoginCaptcha4Admin(String text) {
        String captchaKey = UUID.randomUUID().toString();
        redisUtils.setex(RedisConstants.REDIS_ADMIN_CAPTCHA_KEY + captchaKey, text, RedisConstants.CAPTCHA_TTL);
        return captchaKey;
    }

    // 存储分类列表到redis中
    public void saveCategoryList(List<Category> categoryList) {
        redisUtils.set(RedisConstants.REDIS_CATEGORY_LIST_KEY, categoryList);
    }

    // 从redis中获取分类列表
    public List<Category> getCategoryList() {
        List<Category> list = (List<Category>) redisUtils.get(RedisConstants.REDIS_CATEGORY_LIST_KEY);
        return list != null ? list : Collections.emptyList();
    }

    // 保存待上传的视频信息
    public String savePreVideoFileInfo(String userId, String fileName, Integer chunks) {
        String uploadId = StringUtils.getRandomString(Constants.LENGTH_10);
        UploadingFileDTO fileDTO = new UploadingFileDTO();
        fileDTO.setChunks(chunks);
        fileDTO.setUploadId(uploadId);
        fileDTO.setChunkIndex(0);
        String day = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String filePath = day + "/" + userId + uploadId;
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TEMP + filePath;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        fileDTO.setFilePath(filePath);
        redisUtils.setex(
                RedisConstants.REDIS_UPLOADING_FILE_KEY + userId + uploadId,
                fileDTO,
                RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY);
        return uploadId;
    }

    // 获取待上传的视频信息
    public UploadingFileDTO getUploadVideoFile(String userId, String uploadId) {
        return (UploadingFileDTO) redisUtils.get(RedisConstants.REDIS_UPLOADING_FILE_KEY + userId + uploadId);
    }

    // 获取待上传视频设置
    public SysSettingDTO getSysSettingDTO() {
        SysSettingDTO sysSettingDTO = (SysSettingDTO) redisUtils.get(RedisConstants.REDIS_SYS_SETTING_KEY);
        if (sysSettingDTO == null) {
            sysSettingDTO = new SysSettingDTO();
        }
        return sysSettingDTO;
    }

    // 更新待上传视频信息
    public void updateVideoFileInfo(String userId, UploadingFileDTO fileDTO) {
        redisUtils.setex(
                RedisConstants.REDIS_UPLOADING_FILE_KEY + userId + fileDTO.getUploadId(),
                fileDTO,
                RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    // 删除待上传视频信息
    public void delVideoFileInfo(String userId, String uploadId) {
        redisUtils.delete(RedisConstants.REDIS_UPLOADING_FILE_KEY + userId + uploadId);
    }

    // 添加待删除的文件到消息队列
    public void addFile2DeleteQueue(String videoId, List<String> deleteFilePathList) {
        redisUtils.lpushAll(RedisConstants.REDIS_FILE_DELETE_LIST_KEY + videoId,
                deleteFilePathList,
                RedisConstants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    // 获取待删除的文件
    public List<String> getDelFileList(String videoId) {
        return redisUtils.getQueueList(RedisConstants.REDIS_FILE_DELETE_LIST_KEY + videoId);
    }

    // 清空待删除的文件
    public void cleanDelFileList(String videoId) {
        redisUtils.delete(RedisConstants.REDIS_FILE_DELETE_LIST_KEY + videoId);
    }

    // 添加待转码的文件到消息队列
    public void addFile2TransferQueue(List<VideoFilePost> addFileList) {
        redisUtils.lpushAll(RedisConstants.REDIS_FILE_TRANSFER_QUEUE_KEY,
                addFileList,
                0);
    }

    // 获取待转码的文件
    public VideoFilePost getFile2TransferQueue() {
        return (VideoFilePost) redisUtils.rpop(RedisConstants.REDIS_FILE_TRANSFER_QUEUE_KEY);
    }

    public Integer reportVideoPlayOnline(String fileId, String deviceId) {
        String userPlayOnlineKey = String.format(RedisConstants.REDIS_VIDEO_PLAY_COUNT_USER_KEY, fileId, deviceId);

        String playOnlineCountKey = String.format(RedisConstants.REDIS_VIDEO_PLAY_COUNT_ONLINE_KEY, fileId);

        if (!redisUtils.keyExists(userPlayOnlineKey)) {
            redisUtils.setex(userPlayOnlineKey, fileId, RedisConstants.REDIS_EXPIRES_ONE_SECONDS * 2);
            return redisUtils.incrementex(playOnlineCountKey, RedisConstants.REDIS_EXPIRES_ONE_SECONDS * 4).intValue();
        }
        redisUtils.expire(playOnlineCountKey, RedisConstants.REDIS_EXPIRES_ONE_SECONDS * 4);
        redisUtils.expire(userPlayOnlineKey, RedisConstants.REDIS_EXPIRES_ONE_SECONDS * 2);
        Integer count = (Integer) redisUtils.get(playOnlineCountKey);
        return count == null ? 1 : count;
    }

    public void decrementPlayOnlineCount(String key) {
        redisUtils.decrement(key);
    }
}
