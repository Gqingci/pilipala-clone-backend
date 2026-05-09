package com.pilipala.utils;

import com.pilipala.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;

@Component
@Slf4j
public class FFmpegUtils {
    @Resource
    private AppConfig appConfig;

    // 创建图片缩略图
    public void createImageThumbnail(String filePath) {
        log.info("创建运行");
        String CMD = "ffmpeg -i \"%s\" -vf scale=200:-1 \"%s\"";
        log.info("路径:{}",String.format(CMD, filePath, filePath + Constants.IMAGE_THUMBNAIL_SUFFIX));
        CMD = String.format(CMD, filePath, filePath + Constants.IMAGE_THUMBNAIL_SUFFIX);
        ProcessUtils.executeCommand(CMD, appConfig.getShowFFmpegLog());
    }

    // 获取视频时长
    public Integer getVideoDuration(String completeVideo) {
        final String CMD = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 \"%s\"";
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

    public void convertVideo2Ts(File tsFolder, String videoFilePath) {
        // 生成临时 ts 文件
        String tsPath = tsFolder + Constants.TS_NAME;
        String CMD_TRANSFER_2_TS = String.format(
                "ffmpeg -y -i \"%s\" -vcodec copy -acodec copy -bsf:v h264_mp4toannexb \"%s\"",
                videoFilePath, tsPath
        );
        ProcessUtils.executeCommand(CMD_TRANSFER_2_TS, appConfig.getShowFFmpegLog());

        // 切片并生成 m3u8
        String segmentList = tsFolder + "/index.m3u8";
        String CMD_CUT_TS = String.format(
                "ffmpeg -i \"%s\" -c copy -map 0 -f segment -segment_list \"%s\" -segment_time 10 \"%s/%%04d.ts\"",
                tsPath, segmentList, tsFolder.getPath().replace("\\", "/")
        );
        ProcessUtils.executeCommand(CMD_CUT_TS, appConfig.getShowFFmpegLog());

        // 删除临时 ts
        new File(tsPath).delete();
    }

}
