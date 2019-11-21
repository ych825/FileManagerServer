package com.ym.controller;

import com.ym.config.FileServerConfiger;
import com.ym.domain.FileDO;
import com.ym.service.FileService;
import com.ym.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 文件上传下载
 */

@Controller
@RequestMapping("/api")
public class FileController {
	@Autowired
	private FileService fileService;

	@Autowired
	private FileServerConfiger fileServerConfiger;

	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Integer page, Integer limit){
		//查询列表数据
		List<FileDO> fileList = fileService.list(page, limit);
		Long total = fileService.count();
		return R.ok(fileList, total);
	}

	/**
	 * 删除
	 */

	@PostMapping( "/del")
	@ResponseBody
	public R remove(@RequestParam String id){
        FileDO fileDO = fileService.get(id);
        if (fileDO == null){
        	return R.error("文件不存在");
		}
		if (!fileService.remove(id)){
        	return R.error("删除失败，请重试");
		}
		String filePath = fileDO.getUrl() + fileDO.getId() +"." + fileDO.getType();
		if (!FileUtil.deleteFile(filePath)){
			return R.error("删除失败");
		}
		return R.ok("删除成功");
	}

	/**
	 * 上传文件
	 */
	@Transactional
	@ResponseBody
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

		String fileName = file.getOriginalFilename();//获得上传文件名
		Long fileSize = file.getSize();//获得上传文件大小
		String newFileName = FileUtil.renameToUUID(fileName);//给上传文件生成唯一识别码
		String filePath = fileServerConfiger.getPathDirectory()+DateUtil.getNowDate()+"/";//x'x'x
		//判断文件上传类型899‘；
		String fileType = FileType.fileType(fileName);
		if (fileType.equals("0")){
			return R.error("文件名为空");
		}
		if (fileType.equals("1")){
			return R.error("不支持当前文件类型的上传");
		}
		// 生成随机密钥
		String secretKey = AESEncUtil.getSecretKey();

		// 使用 RSA非对称加密算法对随机密钥加密(公钥加密)
		String rsaMessage = RsaSignature.rsaEncrypt(secretKey, fileServerConfiger.getServerPublicKey());

		FileDO sysFile = new FileDO(FileUtil.getFileNameNoEx(newFileName),FileUtil.getFileNameNoEx(fileName), fileType, fileSize, filePath, rsaMessage, new Date());
		if (fileService.save(sysFile) != 1) {
			return R.error();
		}

		// AES对称加密   对文件内容加密
		byte[] encrypt = AESEncUtil.encrypt(file.getBytes(), secretKey);
		FileUtil.uploadFile(encrypt, filePath, newFileName);
		return R.ok().put("id",sysFile.getId());

	}

	/**
	 * 下载文件
	 */
	@ResponseBody
	@GetMapping("/download/{id}")
	public void download(@PathVariable("id") String id, HttpServletResponse response) throws Exception {

		FileDO fileDO = fileService.get(id);

		String filePath = fileDO.getUrl();
		String fileName = fileDO.getName();
		filePath += id + "." + fileDO.getType();
		fileName += "." + fileDO.getType();
		// Ras私钥解密
		String content = fileDO.getContent();
		String decrypt = RsaSignature.rsaDecrypt(content, fileServerConfiger.getServerPrivateKey());
		byte[] bytes = FileUtil.getFileByte(filePath, response);
		// AES 解密
		byte[] data = AESEncUtil.decrypt(bytes, decrypt);
		try {
            FileUtil.downloadFile(data,fileName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
