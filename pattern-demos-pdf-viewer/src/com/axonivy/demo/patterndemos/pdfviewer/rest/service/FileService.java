package com.axonivy.demo.patterndemos.pdfviewer.rest.service;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FileService {
	
	private static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";

	public Response getFileContent(String filename) {
		if (isEmpty(filename)) {
			return Response.status(BAD_REQUEST).build();
		}

		try {
			File file = new File(filename);
			InputStream inputStream = new FileInputStream(file.getJavaFile());
			return Response.status(OK).type(MEDIA_TYPE_APPLICATION_PDF).entity(inputStream).build();
		} catch (IOException e) {
			Ivy.log().error("Error when reading file {0}", e, filename);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}
}
