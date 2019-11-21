package com.ym.dao;


import com.ym.domain.FileDO;

import java.util.List;

/**
 * 文件上传
 */
public interface FileDao {
	//通过id获得上传的文件信息
	FileDO get(String id);
	//分页
	List<FileDO> list(Integer currentPage, Integer linesize);
	//获得上传文件总数
	Long count();
	//保存上传文件信息
	int save(FileDO file);
	//删除上传文件信息
	int remove(String id);
}
