/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.io.File;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.skbkontur.sdk.extern.model.DraftTest;
import ru.skbkontur.sdk.extern.model.DraftTest.TestDocument;
import ru.skbkontur.sdk.extern.model.TestData;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.Draft;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftDocument;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class ExternSDKDraftTest extends AbstractTest {

	private static ExternSDKDraft apiDraft;
	private static TestData[] testData;

	public ExternSDKDraftTest() {
	}

	@BeforeClass
	public static void setUpClass() throws ExternSDKException {
		apiDraft = new ExternSDK().draft;
		testData = getTestClientInfos();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of createDraft method, of class ExternSDKDraft.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testCreateDraft() throws ExternSDKException {
		System.out.println("createDraft");
		for (TestData td : testData) {
			Draft draft = apiDraft.createDraft(td.getClientInfo());
			assertNotNull(draft);
		}
	}

	/**
	 * Test of getDraftById method, of class ExternSDKDraft.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDraft() throws ExternSDKException {
		System.out.println("getDraft");
		if (testData != null) {
			for (TestData td : testData) {
				Draft draft = apiDraft.createDraft(td.getClientInfo());
				String draftId = draft.getId().toString();
				Draft result = apiDraft.getDraft(draftId);
				assertNotNull(result);
			}
		}
	}

	/**
	 * DELETE /v1/{billingAccountId}/drafts/{draftId} Test of deleteDraft method, of class ExternSDKDraft.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testDeleteDraft() throws ExternSDKException {
		System.out.println("deleteDraft");
		for (TestData td : testData) {
			Draft draft = apiDraft.createDraft(td.getClientInfo());
			assertNotNull(draft);
			apiDraft.deleteDraft(draft.getId().toString());
		}
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * Test of getDraftMeta method, of class ExternSDKDraft.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDraftMeta() throws ExternSDKException {
		System.out.println("getDraftMeta");
		for (TestData td : testData) {
			Draft draft = apiDraft.createDraft(td.getClientInfo());
			assertNotNull(draft);
			DraftMeta result = apiDraft.getDraftMeta(draft.getId().toString());
			assertNotNull(result);
		}
	}

	/**
	 * Test of updateDraftMeta method, of class ExternSDKDraft.
	 *
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDraftMeta() throws ExternSDKException {
		System.out.println("updateDraftMeta");
		for (TestData td : testData) {
			DraftMeta ci = td.getClientInfo();
			Draft draft = apiDraft.createDraft(td.getClientInfo());
			assertNotNull(draft);
			String name = "new " + ci.getOrganization().getFullName();
			ci.getOrganization().setFullName(name);
			DraftMeta draftMeta = apiDraft.updateDraftMeta(draft.getId().toString(), ci);
			assertNotNull(draftMeta);
			assertEquals(name, draftMeta.getOrganization().getFullName());
		}
	}

	/**
	 * Test of check method, of class ExternSDKDraft.
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/check
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testCheck() throws ExternSDKException {
		System.out.println("check");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			Map<String, Object> protocol = apiDraft.check(dt.getId(), true);
			assertNotNull(protocol);
		}
	}

	/**
	 * Test of prepare method, of class ExternSDKDraft.
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/prepare
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testPrepare() throws ExternSDKException {
		System.out.println("prepare");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			Map<String, Object> protocol = apiDraft.prepare(dt.getId(), true);
			assertNotNull(protocol);
		}
	}

	/**
	 * Test of send method, of class ExternSDKDraft.
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/send
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testSend() throws ExternSDKException {
		System.out.println("send");
		for (TestData td : testData) {
			DraftMeta ci = td.getClientInfo();
			Draft draft = apiDraft.createDraft(td.getClientInfo());
			assertNotNull(draft);
			String draftId = draft.getId().toString();
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			DraftDocument draftDocument = apiDraft.addDecryptedDocument(draftId, documentBody, "unformal-" + draftId + ".txt");
			assertNotNull(draftDocument);
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
		}
	}

	/**
	 * Test of addDecryptedDocument method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testAddDecryptedDocument() throws ExternSDKException {
		System.out.println("addUncryptedDocument");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
		}
	}

	/**
	 * Test of deleteDocument method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testDeleteDocument() throws ExternSDKException {
		System.out.println("deleteDocument");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				apiDraft.deleteDocument(draftId, d.draftDocumentId);
			}
		}
	}

	/**
	 * Test of getDocument method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDocument() throws ExternSDKException {
		System.out.println("getDocument");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				DraftDocument draftDocument = apiDraft.getDocument(draftId, d.draftDocumentId);
				assertNotNull(draftDocument);
			}
		}
	}

	/**
	 * Test of updateDocument method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDocument() throws ExternSDKException {
		System.out.println("updateDocument");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				DraftDocument draftDocument = apiDraft.getDocument(draftId, d.draftDocumentId);
				assertNotNull(draftDocument);
				String path = "/docs/NO_PRIB_7810_7810_6653000832665325934_20180125_7C66E6A9-90D5-49D0-9325-75AB620DF370.xml";
				DocumentContents documentContents = createDocumentContents(apiDraft, path);
				draftDocument = apiDraft.updateDocument(draftId, d.draftDocumentId, documentContents);
				assertNotNull(draftDocument);
				String fileName = new File(path).getName();
				assertEquals(fileName, draftDocument.getMeta().getFilename());
			}
		}
	}

	/**
	 * Test of getDecryptedDocumentContent method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDecryptedDocumentContent() throws ExternSDKException {
		System.out.println("getDecryptedDocumentContent");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				String result = apiDraft.getDecryptedDocumentContent(draftId, d.draftDocumentId);
				assertNotNull(result);
			}
		}
	}

	/**
	 * Test of updateDecryptedDocumentContent method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDecryptedDocumentContent() throws ExternSDKException {
		System.out.println("updateDecryptedDocumentContent");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				byte[] content = loadUpdatedDocument(d.path);
				apiDraft.updateDecryptedDocumentContent(draftId, d.draftDocumentId, content);
			}
		}
	}

	/**
	 * Test of getEecryptedDocumentContent method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetEncryptedDocumentContent() throws ExternSDKException {
		System.out.println("getEecryptedDocumentContent");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			Map<String, Object> protocol = apiDraft.prepare(dt.getId(), true);
			assertNotNull(protocol);

			for (TestDocument d : dt.getDraftDocumentIdList()) {
				String result = apiDraft.getEncryptedDocumentContent(draftId, d.draftDocumentId);
				assertNotNull(result);
			}
		}
	}

	/**
	 * Test of getSignatureContent method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetSignatureContent() throws ExternSDKException {
		System.out.println("getSignatureContent");
		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				Object result = apiDraft.getSignatureContent(draftId, d.draftDocumentId);
				assertNotNull(result);
			}
		}
	}

	/**
	 * Test of updateSignature method, of class ExternSDKDocument.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateSignature() throws ExternSDKException {
		System.out.println("updateSignature");
		Key signKey  = apiDraft.externSDK.getEnvironment().signKey;
		if (signKey == null) {
			fail("no key signature.");
		}

		for (TestData td : testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			String draftId = dt.getId();
			for (TestDocument d : dt.getDraftDocumentIdList()) {
				String base64 = apiDraft.getDecryptedDocumentContent(draftId, d.draftDocumentId);
				assertNotNull(base64);
				byte[] documentContent = DECODER_BASE64.decode(base64);
				PKCS7 p7p = new PKCS7(apiDraft.externSDK.getEnvironment().cryptoService);
				try {
					byte[] signatureContent = p7p.sign(signKey, null, documentContent, false);
					apiDraft.updateSignature(draftId, d.draftDocumentId, signatureContent);
				}
				catch (CryptoException x) {
					throw new ExternSDKException(ExternSDKException.C_CRYPTO_ERROR,x);
				}
			}
		}
	}
}
