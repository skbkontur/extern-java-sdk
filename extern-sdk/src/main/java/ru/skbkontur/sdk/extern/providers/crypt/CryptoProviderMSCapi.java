/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.service.SDKException;
import static ru.skbkontur.sdk.extern.service.SDKException.C_CRYPTO_ERROR;
import static ru.skbkontur.sdk.extern.service.SDKException.C_CRYPTO_ERROR_INIT;

/**
 *
 * @author AlexS
 */
public class CryptoProviderMSCapi implements CryptoProvider {
	
	private final CryptoService cryptoService;
	private final Map<String,Key> cacheSignKey;
	
	public CryptoProviderMSCapi() throws SDKException {
		try {
			cryptoService = new MSCapi(true);
			cacheSignKey = new ConcurrentHashMap<>();
		}
		catch (CryptoException x) {
			throw new SDKException(C_CRYPTO_ERROR_INIT, x);
		}
	}
	
	@Override
	public byte[] sign(String thumbprint, byte[] content) throws SDKException {
		try {
			Key key = getKeyByThumbprint(thumbprint);
			if (key == null)
				throw new SDKException(SDKException.C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint);
			
			PKCS7 p7p = new PKCS7(cryptoService);
			
			return p7p.sign(key, null, content, false);
		}
		catch (CryptoException x) {
			throw new SDKException(C_CRYPTO_ERROR, x);
		}
	}

	@Override
	public byte[] getSignerCertificate(String thumbprint) throws SDKException {
		try {
			Key key = getKeyByThumbprint(thumbprint);
			if (key == null)
				throw new SDKException(SDKException.C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint);
			
			return key.getX509ctx();
		}
		catch (CryptoException x) {
			throw new SDKException(C_CRYPTO_ERROR, x);
		}
	}
	
	public void removeKey(String thumbprint) {
		cacheSignKey.remove(thumbprint);
	}
	
	public void removeAllKeys() {
		cacheSignKey.clear();
	}
	
	private Key getKeyByThumbprint(String thumbprint) throws CryptoException {
		Key key = cacheSignKey.get(thumbprint);
		if (key == null) {
			Key[] keys = cryptoService.getKeys();
			if (keys != null && keys.length > 0) {
				byte[] t = IOUtil.hexToBytes(thumbprint);
				key = Stream.of(keys).filter(k->Arrays.equals(k.getThumbprint(),t)).findAny().orElse(null);
				if (key != null)
					cacheSignKey.put(thumbprint, key);
			}
		}
		return key;
	}
}
