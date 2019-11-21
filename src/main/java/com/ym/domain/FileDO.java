package com.ym.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 文件上传
 */

@Data
@NoArgsConstructor
public class FileDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	// 文件名称
	private String name;

	//文件类型
	private String type;

	// 文件大小
	private Long size;

	//URL地址
	private String url;

	// 加密信封
	private String content;

	//创建时间
	private Date createDate;

	public FileDO(String id, String name, String type, Long size ,String url, String content,  Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.size = size;
		this.url = url;
		this.content = content;
		this.createDate = createDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
