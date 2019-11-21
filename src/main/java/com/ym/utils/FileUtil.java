package com.ym.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

public class FileUtil {

	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + "/"+ fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	public static byte[] getFileByte(String filePath, HttpServletResponse response) throws IOException {
		File file = new File(filePath);
		// 判断文件是否存在
		if (!file.exists()) {
			PrintWriter writer = null;
			try {
				//文件不存在，跳转404页面
				response.setContentType("text/html;charset=UTF-8");
				writer = response.getWriter();
				writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">File Server</p>");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		long fileSize = file.length();
		FileInputStream is = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;

		while (offset < buffer.length
				&& (numRead = is.read(buffer, offset, buffer.length - offset)) >= 0) {

			offset += numRead;

		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		is.close();
		return buffer;
	}

	public static void downloadFile(byte[] data, String fileName, HttpServletResponse response) throws Exception {
		//1.应用程序强制下载
		response.setContentType("application/force-download");
		//2、Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，
		// 保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
		response.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));

		OutputStream os = response.getOutputStream();
		os.write(data);
		os.flush();
		os.close();
	}
	/*
	 * Java文件操作 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {

		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static String renameToUUID(String fileName) {
		return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
	}
}
