package fr.datasyscom.scopiom.restclient.exception;

import com.sun.jersey.api.client.ClientResponse;

public class RestException extends Exception {

	private static final long serialVersionUID = 1L;

	private Integer httpCode;

	public RestException(String message, Integer httpCode) {
		super(message);
		this.httpCode = httpCode;
	}

	public RestException(ClientResponse response) {
		super(response.getEntity(String.class));
		this.httpCode = response.getStatus();
	}
	
	public Integer getHttpCode() {
		return httpCode;
	}

}
