package com.axonivy.demo.patterndemos.zip.model;

import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

public class ZipModel {
	private String fileName;
	private Long size;
	private Long compressedSize;

	public ZipModel(String fileName, Long size, Long compressedSize) {
		super();
		this.fileName = fileName;
		this.size = size;
		this.compressedSize = compressedSize;
	}

	public String getFileName() {
		return fileName;
	}
	
	public Long getSize() {
		return size;
	}
	
	public Long getCompressedSize() {
		return compressedSize;
	}
	
	public String getSizeDisplay() {
		return byteCountToDisplaySize(ofNullable(this.size).orElse(0L));
	}
	
	public String getCompressedSizeDisplay() {
		return byteCountToDisplaySize(ofNullable(this.compressedSize).orElse(0L));
	}
}
