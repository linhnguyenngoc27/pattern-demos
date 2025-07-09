package com.axonivy.demo.patterndemos.pdfviewer.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.axonivy.demo.patterndemos.pdfviewer.enums.ViewType;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Binary;
import ch.ivyteam.ivy.scripting.objects.File;

public class PdfViewerCtrl {
	private List<File> files;
	private ViewType viewType;
	private File selectedFile;
	private String url;
	private StreamedContent fileContent;

	public PdfViewerCtrl() {
		this.files = new ArrayList<>();
		this.viewType = ViewType.MEDIA;
		this.selectedFile = null;
	}
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File selectedFile) {
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
	
	public void onRowSelect(SelectEvent<File> event) {
		onViewFile(event.getObject(), viewType);
    }

	public void onUploadFile(FileUploadEvent event) {
		File file = storeFile(event.getFile());
		onViewFile(file, viewType);
		files.add(file);
	}

	public void onViewFile(File file, ViewType viewType) {
		this.selectedFile = file;
		this.viewType = viewType;
		fileContent = createStreamedContent(this.selectedFile);

	}

	public StreamedContent getDownloadFile(File file) {
		return createStreamedContent(file);
	}

	private File storeFile(UploadedFile uploadedFile) {

		try {

			// The file will be store at
			// ../workspaces/work/designer/files-yyyymmdd-???/application

			File file = new File(uploadedFile.getFileName());
			file.writeBinary(new Binary(uploadedFile.getContent()));

			return file;
		} catch (IOException e) {
			Ivy.log().error("Error when storing file {0}", e, uploadedFile.getFileName());
		}
		return null;
	}

	private StreamedContent createStreamedContent(File file) {
		if (file == null) {
			return null;
		}

		try {
			InputStream inputStream = new FileInputStream(file.getJavaFile());
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
