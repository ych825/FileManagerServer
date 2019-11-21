package com.ym.service.impl;

import com.ym.dao.FileDao;
import com.ym.domain.FileDO;
import com.ym.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private FileDao fileDao;
	
	@Override
	public FileDO get(String id){
		return fileDao.get(id);
	}
	
	@Override
	public List<FileDO> list(Integer page, Integer limit){
		Integer currentPage = (page-1)*limit;
		Integer linesize = page*limit;
		return fileDao.list(currentPage, linesize);
	}
	
	@Override
	public Long count(){
		return fileDao.count();
	}
	
	@Override
	public int save(FileDO file){
		return fileDao.save(file);
	}
	
	@Override
	public boolean remove(String id){
		return fileDao.remove(id) > 0;
	}

}
