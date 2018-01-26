/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertNotNull;
import ru.skbkontur.sdk.extern.model.DraftTest;
import ru.skbkontur.sdk.extern.model.TestData;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.Draft;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftDocument;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class AbstractTest {

	protected static TestData[] getTestClientInfos() throws ExternSDKException {
		Gson gson = new Gson();
		InputStream is = ExternSDKDraftTest.class.getClassLoader().getResourceAsStream("clientInfosTest.json");
		if (is == null) {
			throw new ExternSDKException(ExternSDKException.UNKNOWN);
		}
		try {
			return gson.fromJson(new java.io.InputStreamReader(is), TestData[].class);
		}
		finally {
			try {
				is.close();
			}
			catch (java.io.IOException x) {
			}
		}
	}

	/**
	 * Создаем черновик и добавляем документы вместе с подписями
	 * Исходные данные берутся из test/resources/clientInfosTest.json
	 * @param apiDraft ExternSDKDraft
	 * @param td TestData
	 * @return DraftTest
	 * @throws ExternSDKException 
	 */
	protected DraftTest addDecryptedDocument(ExternSDKDraft apiDraft,TestData td) throws ExternSDKException {
		DraftTest draftTest = new DraftTest();
		
		DraftMeta ci = td.getClientInfo();
		Draft draft = apiDraft.createDraft(ci);
		assertNotNull(draft);
		
		String draftId = draft.getId().toString();
		draftTest.setId(draftId);
		
		for (String p: td.getDocs()) {
			byte[] documentBody = loadDocument(p);
			DraftDocument result = apiDraft.addDecryptedDocument(draftId,documentBody,new File(p).getName());
			assertNotNull(result);
			draftTest.addDraftDocumentId(result.getId().toString());
		}
		
		return draftTest;
	}
	
	protected DocumentContents createDocumentContents(ExternSDKDraft apiDraft, String path) throws ExternSDKException {
		return apiDraft.createDocumentContents(loadDocument(path), new File(path).getName());
	}
	
	private byte[] loadDocument(String path) throws ExternSDKException {
		try (InputStream is = getClass().getResourceAsStream(path)) {
			if (is != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ru.argosgrp.cryptoservice.utils.IOUtil.copyStream(is, os);
				return os.toByteArray();
			}
			else {
				throw new ExternSDKException(ExternSDKException.C_RESOURCE_NOT_FOUND,path);
			}
		}
		catch (IOException x) {
			throw new ExternSDKException(ExternSDKException.UNKNOWN,x);
		}
	}
}
