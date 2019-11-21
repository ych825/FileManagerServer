package com.ym.service;

import com.ym.domain.FileDO;

import java.util.List;

/**
 * 文件上传
 * 
 * @author tzy
 * @email tang86100@163.com
 * @date 2018-06-25 16:26:05
 */
public interface FileService {

	FileDO get(String id);

	List<FileDO> list(Integer page, Integer limit);

	Long count();

	int save(FileDO file);

	boolean remove(String id);
}
