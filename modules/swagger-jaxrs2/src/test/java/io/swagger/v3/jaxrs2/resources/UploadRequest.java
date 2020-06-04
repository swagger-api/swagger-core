package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

@Schema(name = "UploadRequest", title="Schema for Upload")
public class UploadRequest {

	@Hidden
	@FormDataParam("picture")
	private FormDataContentDisposition disposition;

	public FormDataContentDisposition getDisposition() {
		return disposition;
	}
	public void setDisposition(FormDataContentDisposition disposition) {
		this.disposition = disposition;
	}


	@FormDataParam("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Schema(name="picture", type="string", format="binary")
	@FormDataParam("picture")
	private InputStream upload;


	public InputStream getUpload() {
		return upload;
	}
	public void setUpload(InputStream upload) {
		this.upload = upload;
	}

	@Override
	public String toString() {
		final String NL = System.lineSeparator();
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("name: ").append(name).append(NL);
		return sb.toString();
	}

}
