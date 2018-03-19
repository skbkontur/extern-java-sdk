/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers;

import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public interface CryptoProvider {
	CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content);
	QueryContext<byte[]> sign(QueryContext<byte[]> cxt);
	
	CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint);
	QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> cxt);

	CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content);
	QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt);
}
