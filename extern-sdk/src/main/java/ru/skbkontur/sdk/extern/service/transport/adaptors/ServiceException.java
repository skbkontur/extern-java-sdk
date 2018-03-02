/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import ru.skbkontur.sdk.extern.providers.ServiceError;

/**
 *
 * @author AlexS
 */
public class ServiceException extends RuntimeException {
	public ServiceException(ServiceError serviceError) {
		super(serviceError.toString());
	}
}
