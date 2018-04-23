/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import ru.skbkontur.sdk.extern.service.transport.invoker.ApiException;

/**
 *
 * @author alexs
 */
class ApiExceptionDto {

	public ApiException fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException dto) {
		return new ApiException(dto.getCode(), dto.getMessage(), dto.getResponseHeaders(), dto.getResponseBody());
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException toDto(ApiException x) {
		return new ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException(
			x.getCode(), x.getMessage(), x.getResponseHeaders(), x.getResponseBody()
		);
	}
}
