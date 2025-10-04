package com.pilipala.web.controller;

import com.pilipala.component.RedisComponent;
import com.pilipala.config.AppConfig;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.dto.UploadingFileDTO;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.entity.enums.DateTimePatternEnum;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.utils.Constants;
import com.pilipala.utils.DateUtils;
import com.pilipala.utils.FFmpegUtils;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@RestController
@RequestMapping("/file")
@Validated
@Slf4j
public class FileController extends ABaseController {
    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) {
        if (StringUtils.pathIsOk(sourceName)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String suffix = StringUtils.getFileSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, sourceName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if (!file.exists()) {
            return;
        }
        try (OutputStream out = response.getOutputStream();
             FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("读取文件异常", e);
        }
    }

    @RequestMapping("/preUploadVideo")
    public ResponseVO preUploadVideo(@NotEmpty String fileName, @NotNull Integer chunks) {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        String uploadId = redisComponent.savePreVideoFileInfo(userTokenInfoDTO.getId(), fileName, chunks);
        return getSuccessResponseVo(uploadId);
    }

    @RequestMapping("/uploadVideo")
    public ResponseVO uploadVideo(@NotNull MultipartFile chunkFile,
                                  @NotNull Integer chunkIndex,
                                  @NotEmpty String uploadId) throws IOException {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UploadingFileDTO fileDTO = redisComponent.getUploadVideoFile(userTokenInfoDTO.getId(), uploadId);
        if (fileDTO == null) {
            throw new BusinessException("文件不存在，请重新上传。");
        }
        SysSettingDTO sysSettingDTO = redisComponent.getSysSettingDTO();
        if (fileDTO.getFileSize() > sysSettingDTO.getVideoSize() * Constants.GB_SIZE) {
            throw new BusinessException("文件超过大小限制");
        }

        // 判断分片
        if ((chunkIndex - 1) > fileDTO.getChunkIndex() || chunkIndex > fileDTO.getChunks() - 1) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER_TEMP + fileDTO.getFilePath();
        File targetFile = new File(folder + "/" + chunkIndex);
        chunkFile.transferTo(targetFile);
        fileDTO.setChunkIndex(chunkIndex);
        fileDTO.setFileSize(fileDTO.getFileSize() + chunkFile.getSize());
        redisComponent.updateVideoFileInfo(userTokenInfoDTO.getId(), fileDTO);

        return getSuccessResponseVo(null);
    }

    @RequestMapping("/delUploadVideo")
    public ResponseVO delUploadVideo(@NotEmpty String uploadId) throws IOException {
        UserTokenInfoDTO userTokenInfoDTO = getUserTokenInfoDTO();
        UploadingFileDTO fileDTO = redisComponent.getUploadVideoFile(userTokenInfoDTO.getId(), uploadId);
        if (fileDTO == null) {
            throw new BusinessException("文件不存在，请重新上传");
        }

        redisComponent.delVideoFileInfo(userTokenInfoDTO.getId(), uploadId);
        FileUtils.deleteDirectory(
                new File(appConfig.getProjectFolder()
                        + Constants.FILE_FOLDER
                        + Constants.FILE_FOLDER_TEMP
                        + fileDTO.getFilePath()));

        return getSuccessResponseVo(uploadId);
    }

    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        String day = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMMDD.getPattern());
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_COVER + day;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringUtils.getFileSuffix(fileName);
        String realFileName = StringUtils.getRandomString(Constants.LENGTH_30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if (createThumbnail != null && createThumbnail) {
            fFmpegUtils.createImageThumbnail(filePath);
        }

        return getSuccessResponseVo(Constants.FILE_COVER + day + "/" + realFileName);
    }
}
