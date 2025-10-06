package com.pilipala.service.impl;

import com.pilipala.component.RedisComponent;
import com.pilipala.config.AppConfig;
import com.pilipala.dto.SysSettingDTO;
import com.pilipala.dto.UploadingFileDTO;
import com.pilipala.entity.enums.*;
import com.pilipala.entity.po.Video;
import com.pilipala.entity.po.VideoFile;
import com.pilipala.entity.po.VideoFilePost;
import com.pilipala.entity.query.VideoFilePostQuery;
import com.pilipala.entity.query.VideoFileQuery;
import com.pilipala.entity.query.VideoPostQuery;
import com.pilipala.entity.query.SimplePage;
import com.pilipala.entity.po.VideoPost;
import com.pilipala.entity.vo.PaginationResultVO;
import com.pilipala.exception.BusinessException;
import com.pilipala.mappers.VideoFileMapper;
import com.pilipala.mappers.VideoFilePostMapper;
import com.pilipala.mappers.VideoMapper;
import com.pilipala.mappers.VideoPostMapper;
import com.pilipala.utils.Constants;
import com.pilipala.utils.CopyUtils;
import com.pilipala.utils.FFmpegUtils;
import com.pilipala.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.pilipala.service.VideoPostService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gqingci
 * @Description: 视频信息对应的ServiceImpl
 * @date: 2025/10/02
 */

@Service("videoPostService")
@Slf4j
public class VideoPostServiceImpl implements VideoPostService {

    @Resource
    private VideoPostMapper<VideoPost, VideoPostQuery> videoPostMapper;

    @Resource
    private VideoFilePostMapper<VideoFilePost, VideoFilePostQuery> videoFilePostMapper;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoFileMapper videoFileMapper;

    @Resource
    RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<VideoPost> findListByParam(VideoPostQuery query) {
        return this.videoPostMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    @Override
    public Integer findCountByParam(VideoPostQuery query) {
        return this.videoPostMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    @Override
    public PaginationResultVO<VideoPost> findListByPage(VideoPostQuery query) {
        Integer count = this.findCountByParam(query);
        Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<VideoPost> list = this.findListByParam(query);
        PaginationResultVO<VideoPost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(VideoPost bean) {
        return this.videoPostMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<VideoPost> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoPostMapper.insertBatch(listBean);
    }

    /**
     * 新增或者修改
     */
    @Override
    public Integer addOrUpdate(VideoPost videoPost) {
        return this.videoPostMapper.insertOrUpdate(videoPost);
    }

    /**
     * 批量新增或修改
     */
    @Override
    public Integer addOrUpdateBatch(List<VideoPost> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.videoPostMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据VideoId查询
     */
    @Override
    public VideoPost getByVideoId(String videoId) {
        return this.videoPostMapper.selectByVideoId(videoId);
    }

    /**
     * 根据VideoId更新
     */
    @Override
    public Integer updateByVideoId(VideoPost bean, String videoId) {
        return this.videoPostMapper.updateByVideoId(bean, videoId);
    }

    /**
     * 根据VideoId删除
     */
    @Override
    public Integer deleteByVideoId(String videoId) {
        return this.videoPostMapper.deleteByVideoId(videoId);
    }

    /**
     * 保存视频信息
     *
     * @param videoPost      视频信息
     * @param uploadFileList 上传文件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideo(VideoPost videoPost, List<VideoFilePost> uploadFileList) {
        if (uploadFileList.size() > redisComponent.getSysSettingDTO().getVideoPCount()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (!StringUtils.isEmpty(videoPost.getVideoId())) {
            VideoPost videoPostDB = this.videoPostMapper.selectByVideoId(videoPost.getVideoId());
            if (videoPostDB == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            if (ArrayUtils
                    .contains(new Integer[]{VideoStatusEnum.STATUS0.getStatus(), VideoStatusEnum.STATUS2.getStatus()}
                            , videoPostDB.getStatus())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
        }

        Date currentDate = new Date();
        String videoId = videoPost.getVideoId();
        List<VideoFilePost> deleteFileList = new ArrayList<>();
        List<VideoFilePost> addFileList = uploadFileList;

        if (StringUtils.isEmpty(videoId)) {
            videoId = StringUtils.getRandomString(Constants.LENGTH_10);
            videoPost.setVideoId(videoId);
            videoPost.setCreateTime(currentDate);
            videoPost.setLastUpdateTime(currentDate);
            videoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            this.videoPostMapper.insert(videoPost);
        } else {
            VideoFilePostQuery query = new VideoFilePostQuery();
            query.setVideoId(videoId);
            query.setUserId(videoPost.getUserId());
            List<VideoFilePost> videoFileList = this.videoFilePostMapper.selectList(query);
            Map<String, VideoFilePost> uploadFileMap = uploadFileList
                    .stream()
                    .collect(Collectors.toMap(item -> item.getUploadId(), Function.identity(), (item1, item2) -> item2));

            Boolean isUpdate = false;
            for (VideoFilePost videoFilePost : videoFileList) {
                VideoFilePost uploadFile = uploadFileMap.get(videoFilePost.getUploadId());
                if (uploadFile == null) {
                    // 删除
                    deleteFileList.add(videoFilePost);
                } else if (!uploadFile.getFileName().equals(videoFilePost.getFileName())) {
                    // 修改
                    isUpdate = true;
                }
            }

            addFileList = uploadFileList
                    .stream()
                    .filter(item -> item.getFileId() == null)
                    .collect(Collectors.toList());

            videoPost.setLastUpdateTime(currentDate);

            Boolean changeVideo = this.changeVideo(videoPost);
            if (addFileList != null && !addFileList.isEmpty()) {
                videoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            } else if (changeVideo || isUpdate) {
                videoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
            }
            this.videoPostMapper.updateByVideoId(videoPost, videoId);
        }

        // 视频文件修改
        if (!deleteFileList.isEmpty()) {
            List<String> deleteFileIdList = deleteFileList
                    .stream()
                    .map(item -> item.getFileId())
                    .collect(Collectors.toList());
            this.videoFilePostMapper.deleteBatchByFileIdList(deleteFileIdList, videoPost.getUserId());

            List<String> deleteFilePathList = deleteFileList
                    .stream()
                    .map(item -> item.getFilePath())
                    .collect(Collectors.toList());

            // 添加文件删除列表
            redisComponent.addFile2DeleteQueue(videoId, deleteFilePathList);
        }

        // 更新视频
        Integer index = 1;
        for (VideoFilePost videoFilePost : uploadFileList) {
            videoFilePost.setFileIndex(index);
            videoFilePost.setVideoId(videoId);
            videoFilePost.setUserId(videoFilePost.getUserId());
            if (videoFilePost.getFileId() == null) {
                videoFilePost.setFileId(StringUtils.getRandomString(Constants.LENGTH_10));
                videoFilePost.setUpdateType(VideoFileUpdateTypeEnum.UPDATE.getStatus());
                videoFilePost.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
            }
        }
        this.videoFilePostMapper.insertOrUpdateBatch(uploadFileList);

        if (addFileList != null && !addFileList.isEmpty()) {
            for (VideoFilePost videoFilePost : addFileList) {
                videoFilePost.setUserId(videoPost.getUserId());
                videoFilePost.setVideoId(videoId);
            }
            redisComponent.addFile2TransferQueue(addFileList);
        }
    }

    @Override
    public void transferVideoFile(VideoFilePost videoFilePost) {
        VideoFilePost updateFilePost = new VideoFilePost();
        try {
            UploadingFileDTO FileDTO = redisComponent.getUploadVideoFile(videoFilePost.getUserId(), videoFilePost.getUploadId());

            String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP + Constants.FILE_FOLDER_TEMP + FileDTO.getFilePath();
            File tempFile = new File(tempFilePath);

            // 创建目标文件
            String targetFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.FILE_FOLDER + Constants.FILE_VIDEO + FileDTO.getFilePath();
            File targetFile = new File(targetFilePath);

            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }

            FileUtils.copyDirectory(tempFile, targetFile);

            // 删除临时目录
            FileUtils.forceDelete(tempFile);

            // 删除上传文件信息
            redisComponent.delVideoFileInfo(videoFilePost.getUserId(), videoFilePost.getUploadId());

            // 合并文件
            String completeVideo = targetFilePath + Constants.TEMP_VIDEO;
            this.union(targetFilePath, completeVideo, true);

            // 获取播放时长
            Integer duration = fFmpegUtils.getVideoDuration(completeVideo);
            updateFilePost.setDuration(duration);
            updateFilePost.setFilePath(Constants.FILE_VIDEO + FileDTO.getFilePath());
            updateFilePost.setFileSize(new File(completeVideo).length());
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());

            // 转码
            this.convertVideo2Ts(completeVideo);

        } catch (Exception e) {
            log.error("文件转码失败", e);
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
        } finally {
            videoFilePostMapper.updateByUploadIdAndUserId(updateFilePost,
                    videoFilePost.getUploadId(),
                    videoFilePost.getUserId());
            VideoFilePostQuery query = new VideoFilePostQuery();
            query.setVideoId(videoFilePost.getVideoId());
            query.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());

            // 判断是否有失败文件
            Integer failCount = videoFilePostMapper.selectCount(query);
            if (failCount > 0) {
                VideoPost videoUpdate = new VideoPost();
                videoUpdate.setStatus(VideoStatusEnum.STATUS1.getStatus());
                videoPostMapper.updateByVideoId(videoUpdate, videoFilePost.getVideoId());
                return;
            }


            query.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());

            // 判断是否转码完成
            Integer transferCount = videoFilePostMapper.selectCount(query);
            if (transferCount == 0) {
                Integer duration = videoFilePostMapper.sumDuration(videoFilePost.getVideoId());
                VideoPost videoUpdate = new VideoPost();
                videoUpdate.setDuration(duration);
                videoUpdate.setStatus(VideoStatusEnum.STATUS2.getStatus());
                videoPostMapper.updateByVideoId(videoUpdate, videoFilePost.getVideoId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditVideo(String videoId, Integer status, String reason) {
        VideoStatusEnum statusEnum = VideoStatusEnum.getByStatus(status);
        if (statusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        VideoPost videoPost = new VideoPost();
        videoPost.setStatus(status);

        VideoPostQuery videoPostQuery = new VideoPostQuery();
        videoPostQuery.setStatus(VideoStatusEnum.STATUS2.getStatus());
        videoPostQuery.setVideoId(videoId);
        Integer auditCount = this.videoPostMapper.updateByParam(videoPost, videoPostQuery);

        if (auditCount == 0) {
            throw new BusinessException("审核失败，请稍后再试。");
        }

        VideoFilePost videoFilePost = new VideoFilePost();
        videoFilePost.setUpdateType(VideoFileUpdateTypeEnum.NO_UPDATE.getStatus());

        VideoFilePostQuery FilePostQuery = new VideoFilePostQuery();
        FilePostQuery.setVideoId(videoId);
        this.videoFilePostMapper.updateByParam(videoFilePost, FilePostQuery);

        if (VideoStatusEnum.STATUS4 == statusEnum) {
            return;
        }

        VideoPost post = this.videoPostMapper.selectByVideoId(videoId);

        Video videoDB = this.videoPostMapper.selectByVideoId(videoId);
        if (videoDB == null) {
            SysSettingDTO sysSettingDTO = redisComponent.getSysSettingDTO();
            // TODO 给用户加硬币
        }

        // 更新发布信息到正式表
        Video video = CopyUtils.copy(post, Video.class);
        this.videoMapper.insertOrUpdate(video);

        // 更新视频信息到正式表（先删除再添加）
        VideoFileQuery videoFileQuery = new VideoFileQuery();
        videoFileQuery.setVideoId(videoId);
        this.videoFileMapper.deleteByParam(videoFileQuery);

        VideoFilePostQuery videoFilePostQuery = new VideoFilePostQuery();
        videoFilePostQuery.setVideoId(videoId);
        List<VideoFilePost> videoFilePostList = this.videoFilePostMapper.selectList(videoFilePostQuery);

        List<VideoFile> videoFileList = CopyUtils.copyList(videoFilePostList, VideoFile.class);
        this.videoFileMapper.insertBatch(videoFileList);

        // 删除文件
        List<String> filePathList = redisComponent.getDelFileList(videoId);
        if (filePathList != null) {
            for (String path : filePathList) {
                File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + path);
                if (file.exists()) {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        log.error("文件删除失败", e);
                    }
                }
            }
        }
        redisComponent.cleanDelFileList(videoId);

        // TODO 保存信息到es
    }

    private void convertVideo2Ts(String completeVideo) {
        File videoFile = new File(completeVideo);
        File tsFolder = videoFile.getParentFile();
        String codec = fFmpegUtils.getVideoCodec(completeVideo);
        if (Constants.VIDEO_CODE_HEVC.equals(codec)) {
            String tempFileName = completeVideo + Constants.VIDEO_CODE_TEMP_FILE_SUFFIX;
            new File(completeVideo).renameTo(new File(tempFileName));
            fFmpegUtils.convertHevcToH264(tempFileName, completeVideo);
            new File(tempFileName).delete();
        }

        fFmpegUtils.convertVideo2Ts(tsFolder, completeVideo);
        videoFile.delete();
    }

    private void union(String dirPath, String toFilePath, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new BusinessException("目录不存在");
        }
        File[] fileList = dir.listFiles();
        File targetFile = new File(toFilePath);
        try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
            byte[] b = new byte[1024 * 10];
            for (int i = 0; i < fileList.length; i++) {
                int len = -1;
                // 创建读取文件的对象
                File chunkFile = new File(dirPath + File.separator + i);
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) != -1) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    log.error("合并分片失败", e);
                    throw new BusinessException("合并文件失败");
                } finally {
                    if (readFile != null) {
                        readFile.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("合并文件" + dirPath + "出错了");
        } finally {
            if (delSource) {
                for (int i = 0; i < fileList.length; i++) {
                    fileList[i].delete();
                }
            }
        }
    }

    private Boolean changeVideo(VideoPost videoPost) {
        VideoPost videoPostDB = this.videoPostMapper.selectByVideoId(videoPost.getVideoId());

        // 判断视频信息是否修改
        return !videoPostDB.getVideoName().equals(videoPost.getVideoName()) ||
                !videoPostDB.getVideoCover().equals(videoPost.getVideoCover()) ||
                !videoPostDB.getIntroduction().equals(videoPost.getIntroduction()) ||
                !videoPostDB.getTags().equals(videoPost.getTags());
    }

}