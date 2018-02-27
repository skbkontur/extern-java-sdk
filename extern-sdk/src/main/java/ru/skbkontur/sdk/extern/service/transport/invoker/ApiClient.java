/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.invoker;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import java.io.File;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author AlexS
 */
public class ApiClient extends ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiClient {

	/**
	 * Serialize the given Java object into request body according to the object's class and the request Content-Type.
	 *
	 * @param obj The Java object
	 * @param contentType The request Content-Type
	 * @return The serialized request body
	 * @throws ApiException If fail to serialize the given object
	 */
	@Override
	public RequestBody serialize(Object obj, String contentType) throws ApiException {
		if (obj instanceof byte[]) {
			// Binary (byte array) body parameter support.
			return RequestBody.create(MediaType.parse(contentType), (byte[]) obj);
		}
		else if (obj instanceof String) {
			// File body parameter support.
			return RequestBody.create(MediaType.parse(contentType), (String) obj);
		}
		else if (obj instanceof File) {
			// File body parameter support.
			return RequestBody.create(MediaType.parse(contentType), (File) obj);
		}
		else if (isJsonMime(contentType)) {
			String content;
			if (obj != null) {
				content = getJSON().serialize(obj);
			}
			else {
				content = null;
			}
			return RequestBody.create(MediaType.parse(contentType), content);
		}
		else {
			throw new ApiException("Content type \"" + contentType + "\" is not supported");
		}
	}
}
