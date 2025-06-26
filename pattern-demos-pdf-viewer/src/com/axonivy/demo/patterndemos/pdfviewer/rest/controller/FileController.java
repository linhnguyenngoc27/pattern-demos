package com.axonivy.demo.patterndemos.pdfviewer.rest.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.axonivy.demo.patterndemos.pdfviewer.rest.service.FileService;

@Path("appservices/file")
public class FileController {
	
	@GET
	@Path("/{fileName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })	
	public Response getQueryDocumentContent(@PathParam("fileName") String fileName) {
			
		FileService service = new FileService();
		return service.getFileContent(fileName);
	}
	
}
