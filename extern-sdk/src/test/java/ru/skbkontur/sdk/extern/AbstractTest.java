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
import ru.argosgrp.cryptoservice.utils.Base64;
import ru.argosgrp.cryptoservice.utils.IOUtil;
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

	protected static final Base64.Decoder DECODER_BASE64 = Base64.getDecoder();
	protected static final Base64.Encoder ENCODER_BASE64 = Base64.getEncoder();
	
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
			draftTest.addDraftDocumentId(result.getId().toString(),p);
		}
		
		return draftTest;
	}
	
	protected DocumentContents createDocumentContents(ExternSDKDraft apiDraft, String path) throws ExternSDKException {
		return apiDraft.createDocumentContents(loadDocument(path), new File(path).getName());
	}

	protected byte[] loadUpdatedDocument(String path) throws ExternSDKException {
		String name = new File(path).getName();
		String updatedPath = (new File(path).getParentFile().getPath() + "/" + IOUtil.getFileNameWithoutExt(name)).replaceAll("\\\\", "/");
		return loadDocument(updatedPath + "/" + name);
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
