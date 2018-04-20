/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import java.util.List;
import java.util.Map;
import ru.skbkontur.sdk.extern.providers.ServiceError;

/**
 *
 * @author AlexS
 */
public class ServiceErrorImpl implements ServiceError {

	private ErrorCode errorCode;

	private String message;

	private int responseCode;

	private Map<String, List<String>> responseHeaders;

	private String responseBody = null;
	
	private Throwable cause;

	public ServiceErrorImpl(ErrorCode errorCode, String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody, Throwable cause) {
		this.errorCode = errorCode;

		this.message = message;

		this.responseCode = responseCode;

		this.responseHeaders = responseHeaders;

		this.responseBody = responseBody;
		
		this.cause = cause;
	}
	
	public ServiceErrorImpl(ErrorCode errorCode, String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody) {
		this(errorCode, message, responseCode, responseHeaders, responseBody, null);
	}
	
	public ServiceErrorImpl(ErrorCode errorCode) {
		this(errorCode, errorCode.message(), 0, null, null);
	}

	public ServiceErrorImpl(ErrorCode errorCode, String message) {
		this(errorCode, message, 0, null, null);
	}

	public ServiceErrorImpl(String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody) {
		this(ErrorCode.server, message, responseCode, responseHeaders, responseBody);
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

    @Override
    public Throwable getCause() {
        return cause;
    }
    
	@Override
	public String toString() {
		final String EOL = "\r\n";
		StringBuilder errorMsg = new StringBuilder("Message error: ").append(message == null ? errorCode.message() : message).append(EOL);
		if (responseCode != 0) {
			errorMsg.append("  Response code: ").append(responseCode).append(EOL);
			if (responseHeaders!= null) {
				errorMsg.append("  Headers:").append(EOL);
				responseHeaders.keySet().forEach((k) -> {
					List<String> values = responseHeaders.get(k);
					if (values != null) {
						StringBuilder headerLine = new StringBuilder("    ").append(k).append(": ");
						values.forEach((v) -> {
							headerLine.append(v).append("; ");
						});
						errorMsg.append(headerLine).append(EOL);
					}
				});
			}
			if (responseBody != null) {
				String cleanText = responseBody.replaceAll("\n", " ").replaceAll("\r", "");
				errorMsg.append("  Response body: ").append(cleanText).append(EOL);
			}
		}
		return errorMsg.toString();
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
