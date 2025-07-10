package com.axonivy.demo.patterndemos.pdfviewer.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.axonivy.demo.patterndemos.pdfviewer.enums.ViewType;
import com.axonivy.demo.patterndemos.pdfviewer.model.PdfFile;

import ch.ivyteam.ivy.environment.Ivy;


public class PdfViewerCtrl {
	private List<PdfFile> files;

	private ViewType viewType;
	private PdfFile selectedFile;
	private String url;
	private StreamedContent fileContent;

	public PdfViewerCtrl() {
		this.files = new ArrayList<>();
		this.viewType = ViewType.MEDIA;
		this.selectedFile = null;
	}

	public List<PdfFile> getFiles() {
		return files;
	}

	public void setFiles(List<PdfFile> files) {
		this.files = files;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}

	public PdfFile getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(PdfFile selectedFile) {
		this.selectedFile = selectedFile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public StreamedContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}
	
	public void onRowSelect(SelectEvent<PdfFile> event) {
		onViewFile(event.getObject(), viewType);
    }

	public void onUploadFile(FileUploadEvent event) {
		PdfFile pdfFile = convertToPdfFile(event.getFile().getFileName(), event.getFile().getContent());
		onViewFile(pdfFile, viewType);
		files.add(pdfFile);
	}

	public void onViewFile(PdfFile file, ViewType viewType) {
		this.selectedFile = file;
		this.viewType = viewType;
		fileContent = createStreamedContent(this.selectedFile);

	}

	public StreamedContent getDownloadFile(PdfFile file) {
		return createStreamedContent(file);
	}

	private PdfFile convertToPdfFile(String name, byte[] content) {
		PdfFile pdfFile = new PdfFile();
		pdfFile.setName(name);
		pdfFile.setContent(content);
		return pdfFile;
	}

	private StreamedContent createStreamedContent(PdfFile file) {
		if (file.getContent() == null) {
			return null;
		}

		try {
			InputStream inputStream = new ByteArrayInputStream(file.getContent());
			StreamedContent streamedContent = DefaultStreamedContent.builder()
					.contentType("application/pdf")
					.name(file.getName())
					.stream(() -> inputStream)
					.build();
			return streamedContent;
		} catch (Exception e) {
			Ivy.log().error("Error when streaming file {0} is error {1}", e, file.getName(), e.getMessage());
		}

		return null;
	}
}
