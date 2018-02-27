/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt;

import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;

/**
 *
 * @author AlexS
 */
public class CryptoProviderMSCapi implements CryptoProvider {
	
	private CryptoService cryptoService;
	private Key signKey;
	
	public CryptoProviderMSCapi(String thumbprint) throws CryptoException {
		cryptoService = new MSCapi(true);
	}
	
//	private Key
}
