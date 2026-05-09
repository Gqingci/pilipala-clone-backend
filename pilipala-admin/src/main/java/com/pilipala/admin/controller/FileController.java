package com.pilipala.admin.controller;

import com.pilipala.config.AppConfig;
import com.pilipala.dto.UserTokenInfoDTO;
import com.pilipala.dto.VideoPlayInfoDTO;
import com.pilipala.entity.enums.DateTimePatternEnum;
import com.pilipala.entity.enums.ResponseCodeEnum;
import com.pilipala.entity.po.VideoFile;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.vo.ResponseVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.service.VideoFilePostService;
import com.pilipala.service.VideoFileService;
import com.pilipala.utils.Constants;
import com.pilipala.utils.DateUtils;
import com.pilipala.utils.FFmpegUtils;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    FFmpegUtils fFmpegUtils;

    @Resource
    private VideoFilePostService videoFilePostService;

    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail)
            throws IOException {
        String month = DateUtils.format(new Date(), DateTimePatternEnum.YYYY_MM.getPattern());
        String folder =
                appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_COVER + month;
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringUtils.getFileSuffix(fileName);
        String realFileName = StringUtils.getRandomString(Constants.LENGTH_30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if (createThumbnail) {
            // 生成缩略图
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return getSuccessResponseVo(Constants.FILE_COVER + month + "/" + realFileName);
    }

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) {
        if (!StringUtils.pathIsOk(sourceName)) {
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

    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void adminVideoResourceTs(@PathVariable String fileId, @PathVariable String ts, HttpServletResponse response) {
        VideoFilePost videoFilePost = videoFilePostService.getByFileId(fileId);
        String filePath = videoFilePost.getFilePath();
        readFile(response, filePath + "/" + ts);
    }

    @RequestMapping("/videoResource/{fileId}")
    public void adminVideoResource(@PathVariable String fileId, HttpServletResponse response) {
        VideoFilePost videoFilePost = videoFilePostService.getByFileId(fileId);
        String filePath = videoFilePost.getFilePath();
        response.setContentType("application/vnd.apple.mpegurl");
        readFile(response, filePath + "/" + Constants.M3U8_NAME);
    }
}
