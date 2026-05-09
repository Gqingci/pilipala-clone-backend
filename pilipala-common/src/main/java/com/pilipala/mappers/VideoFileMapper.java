package com.pilipala.mappers;

import com.pilipala.entity.query.VideoFileQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author Gqingci
 * @Description: 视频文件信息的Mapper类
 * @date: 2025/10/02
 */

@Mapper
public interface VideoFileMapper<T,P> extends BaseMapper {
	/**
	 * 根据FileId查询
	 */
	 T selectByFileId(@Param("fileId") String fileId);

	/**
	 * 根据FileId更新
	 */
	 Integer updateByFileId(@Param("bean") T t, @Param("fileId") String fileId);

	/**
	 * 根据FileId删除
	 */
	 Integer deleteByFileId(@Param("fileId") String fileId);

	/**
	 * 根据条件删除视频文件
	 */
	Integer deleteByParam(@Param("query") VideoFileQuery query);
}