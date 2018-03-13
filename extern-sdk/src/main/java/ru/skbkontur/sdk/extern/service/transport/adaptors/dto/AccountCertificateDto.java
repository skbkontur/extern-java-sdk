/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.AccountCertificate;

/**
 *
 * @author AlexS
 */
public class AccountCertificateDto {

	public AccountCertificate fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountCertificate dto) {
		AccountCertificate cert = new AccountCertificate();
		
		cert.setContent(dto.getContent());
		cert.setThumbprint(dto.getThumbprint());
		
		return cert;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountCertificate toDto(AccountCertificate cert) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountCertificate dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountCertificate();
		
		dto.setContent(cert.getContent());
		dto.setThumbprint(cert.getThumbprint());
		
		return dto;
	}
}
