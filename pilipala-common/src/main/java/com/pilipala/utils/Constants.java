package com.pilipala.utils;

public class Constants {
    // 默认用户名前缀
    public static final String USERNAME_PREFIX = "霹雳啪啦";
    public static final Integer ZERO = 0;
    public static final Integer TREE = 3;
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_30 = 30;

    // 视频大小
    public static final Long MB_SIZE = 1024 * 1024L;
    public static final Long GB_SIZE = 1024 * 1024 * 1024L;

    // 密码正则表达式
    public static final String PASSWORD_REGEXP = "^(?=.*[a-zA-Z])(?=.*\\d).{6,18}$";
    // 用户ID最大长度
    public static final Integer USERID_MAX_LENGTH = 11;

    // 图片后缀
    public static final String IMAGE_THUMBNAIL_SUFFIX = "_thumbnail.jpg";

    public static final String FILE_FOLDER_TEMP = "temp/";

    public static final String FILE_FOLDER = "file/";

    public static final String FILE_COVER = "cover/";

    public static final String FILE_VIDEO = "video/";

    public static final String TEMP_VIDEO = "/temp.mp4";

    public static final String VIDEO_CODE_HEVC = "hevc";

    public static final String VIDEO_CODE_TEMP_FILE_SUFFIX = "_temp";

    public static final String TS_NAME = "index.ts";

    public static final String M3U8_NAME = "index.m3u8";
}
