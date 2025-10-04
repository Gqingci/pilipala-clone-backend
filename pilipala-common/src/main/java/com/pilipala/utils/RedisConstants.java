package com.pilipala.utils;

public class RedisConstants {
    public static final String REDIS_KEY_PREFIX = "pilipala:";

    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = 86400000;
    public static final Integer TIME_SECONDS_ONE_DAY = 86400;

    // 验证码有效时间
    public static final Integer CAPTCHA_TTL = 3000000;
    // 登录有效时间
    public static final Integer LOGIN_USER_TTL = 43200000;
    public static final Integer TIME_SECONDS_DAY = 43200;

    // 登录验证码前缀
    public static final String REDIS_LOGIN_CAPTCHA_KEY = REDIS_KEY_PREFIX + "login:captcha:";
    // 注册验证码前缀
    public static final String REDIS_REGISTER_CAPTCHA_KEY = REDIS_KEY_PREFIX + "register:captcha:";
    // 前端Token前缀
    public static final String REDIS_WEB_TOKEN_KEY = REDIS_KEY_PREFIX + "token:web:";

    // Token
    public static final String TOKEN_WEB = "token";

    // 管理端Token前缀
    public static final String REDIS_ADMIN_TOKEN_KEY = REDIS_KEY_PREFIX + "token:admin:";
    public static final String TOKEN_ADMIN = "adminToken";
    public static final String REDIS_ADMIN_CAPTCHA_KEY = REDIS_KEY_PREFIX + "admin:captcha:";
    public static final String REDIS_CATEGORY_LIST_KEY = REDIS_KEY_PREFIX + "category:list:";

    // 待上传文件信息
    public static final String REDIS_UPLOADING_FILE_KEY = REDIS_KEY_PREFIX + "uploading:";

    // 系统设置
    public static final String REDIS_SYS_SETTING_KEY = REDIS_KEY_PREFIX + "sysSetting:";

    // 待删除文件列表
    public static final String REDIS_FILE_DELETE_LIST_KEY = REDIS_KEY_PREFIX + "file:list:del";

    // 转码队列
    public static final String REDIS_FILE_TRANSFER_QUEUE_KEY = REDIS_KEY_PREFIX + "queue:transfer";
}
