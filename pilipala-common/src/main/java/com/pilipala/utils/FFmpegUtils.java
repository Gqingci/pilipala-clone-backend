package com.pilipala.utils;

import com.pilipala.config.AppConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;

@Component
public class FFmpegUtils {
    @Resource
    private AppConfig appConfig;

    // 创建图片缩略图
    public void createImageThumbnail(String filePath) {
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"$s\"";
        CMD = String.format(CMD, filePath, filePath + Constants.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
    }

    // 获取视频时长
    public Integer getVideoDuration(String completeVideo) {
        final String CMD = "ffprobe -v error -show_entries format=duration -of default=noprint_wrapper=1:nokey=1 \"%s\"";
        String CMD_GET_CODE = String.format(CMD, completeVideo);
        String result = ProcessUtils.executeCommand(CMD_GET_CODE, appConfig.getShowFFmpegLog());
        if (StringUtils.isEmpty(result)) {
            return 0;
        }
        result = result.replace("\n", "");
        return new BigDecimal(result).intValue();
    }

    // 获取视频编码
    public String getVideoCodec(String videoFilePath) {
        final String CMD = "ffprobe -v error -select_streams v:0 -show_entries stream=codec_name \"%s\"";
        String CMD_GET_CODE = String.format(CMD, videoFilePath);
        String result = ProcessUtils.executeCommand(CMD_GET_CODE, appConfig.getShowFFmpegLog());
        result = result.replace("\n", "");
        result = result.substring(result.indexOf("=") + 1);
        return result.substring(0, result.indexOf("["));
    }

    // 转换视频编码
    public void convertHevcToH264(String newFileName, String videoFilePath) {
        final String CMD = "ffmpeg -i \"%s\" -c:v libx264 -crf 20 \"%s\" -y";
        String CMD_CONVERT = String.format(CMD, newFileName, videoFilePath);
        ProcessUtils.executeCommand(CMD_CONVERT, appConfig.getShowFFmpegLog());
    }

    // 转换视频为ts并切片
    public void convertVideo2Ts(File tsFolder, String videoFilePath) {
        final String CMD = "ffmpeg -y -i \"%s\"  -vcodec copy -acodec copy -bsf:v h264_mp4toannexb \"%s\"";
        // 切片
        final String CMD_CUT_TS = "ffmpeg -i \"%s\" -c copy -map 0 -f segment -segment_list \"%s\" -segment_time 20 %s/%%4d.ts";
        String tsPath = tsFolder + "/" + Constants.TS_NAME;
        // 生成ts
        String CMD_TRANSFER_2_TS = String.format(CMD, videoFilePath, tsPath);
        ProcessUtils.executeCommand(CMD_TRANSFER_2_TS, appConfig.getShowFFmpegLog());
        // 生成.m3u8和切片.ts
        String CMD_TS = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath() + "/" + Constants.M3U8_NAME, tsFolder.getPath());
        ProcessUtils.executeCommand(CMD_TS, appConfig.getShowFFmpegLog());
        // 删除index.ts
        new File(tsPath).delete();
    }
}
