/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class ExternSDKDocumentTest {
	
	private static ExternSDKDraft apiDraft;
	private static ExternSDKDocument apiDoc;
	private static	DraftMeta[] clientInfos;
	
	public ExternSDKDocumentTest() {
	}
	
	@BeforeClass
	public static void setUpClass() throws ExternSDKException {
		apiDraft = new ExternSDK().draft;
		apiDoc = new ExternSDK().document;
		clientInfos = getTestClientInfos();
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
	 * Test of addUncryptedDocument method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testAddUncryptedDocument() throws ExternSDKException {
		System.out.println("addUncryptedDocument");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> result = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(result);
		}		
	}

	/**
	 * Test of deleteDocument method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testDeleteDocument() throws ExternSDKException {
		System.out.println("deleteDocument");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			apiDoc.deleteDocument(draftId, documentId);
		}		
	}

	/**
	 * Test of getDocument method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDocument() throws ExternSDKException {
		System.out.println("getDocument");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			document = apiDoc.getDocument(draftId, documentId);
			assertNotNull(document);
		}		
	}

	/**
	 * Test of updateDocument method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDocument() throws ExternSDKException {
		System.out.println("updateDocument");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			DocumentContents documentContents = new DocumentContents();
			document = apiDoc.updateDocument(draftId, documentId, documentContents);
			assertNotNull(document);
		}		
	}

	/**
	 * Test of getDecryptedDocumentContent method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDecryptedDocumentContent() throws ExternSDKException {
		System.out.println("getDecryptedDocumentContent");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			Object result = apiDoc.getDecryptedDocumentContent(draftId, documentId);
			assertNotNull(result);
		}		
	}

	/**
	 * Test of updateDecryptedDocumentContent method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDecryptedDocumentContent() throws ExternSDKException {
		System.out.println("updateDecryptedDocumentContent");
		// Map<String, Object> result = instance.updateDecryptedDocumentContent(draftId, documentId);
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getEecryptedDocumentContent method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetEecryptedDocumentContent() throws ExternSDKException {
		System.out.println("getEecryptedDocumentContent");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			Object result = apiDoc.getEncryptedDocumentContent(draftId, documentId);
			assertNotNull(result);
		}		
	}

	/**
	 * Test of getSignatureContent method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetSignatureContent() throws ExternSDKException {
		System.out.println("getSignatureContent");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> document = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(document);
			String documentId = (String)document.get("id");
			Object result = apiDoc.getSignatureContent(draftId, documentId);
			assertNotNull(result);
		}		
	}

	/**
	 * Test of updateSignature method, of class ExternSDKDocument.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateSignature() throws ExternSDKException {
		System.out.println("updateSignature");
//		Map<String, Object> result = instance.updateSignature(draftId, documentId);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	private static DraftMeta[] getTestClientInfos() {
		Gson gson = new Gson();
		InputStream is = ExternSDKDocumentTest.class.getClassLoader().getResourceAsStream("clientInfosTest.json");
		InputStreamReader reader = new java.io.InputStreamReader(is);
		return gson.fromJson(reader, DraftMeta[].class);
	}
}
