/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import ru.skbkontur.sdk.extern.model.DraftTest;
import ru.skbkontur.sdk.extern.model.TestData;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.Document;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentDescription;
import ru.skbkontur.sdk.extern.rest.swagger.model.Signature;

/**
 *
 * @author AlexS
 */
@Ignore
public class ExternSDKDocflowTest extends AbstractTest {

	private static ExternSDKDraft apiDraft;
	private static ExternSDKDocflow apiDocflow;
	private static TestData[] testData;

	public ExternSDKDocflowTest() {
	}

	@BeforeClass
	public static void setUpClass() throws ExternSDKException {
		apiDraft = new ExternSDK().draft;
		apiDocflow = new ExternSDK().docflow;
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
	 * Test of getDocflow method, of class ExternSDKDocflow.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testGetDocflow() throws Exception {
		System.out.println("getDocflow");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			docflow = apiDocflow.getDocflow(docflow.getId().toString());
			assertNotNull(docflow);
		}		
	}

	/**
	 * Test of getDocuments method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDocuments() throws ExternSDKException {
		System.out.println("getDocuments");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			List<Document> documents = apiDocflow.getDocuments(draftId);
			assertNotNull(documents);
		}		
	}

	/**
	 * Test of getDocument method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDocument() throws ExternSDKException {
		System.out.println("getDocument");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			String docflowId = docflow.getId().toString();
			for (Document d: docflow.getDocuments()) {
				String documentId = d.getId().toString();
				Document document = apiDocflow.getDocument(docflowId, documentId);
				assertNotNull(document);
			}
		}		
	}

	/**
	 * Test of getDocumentMeta method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDocumentMeta() throws ExternSDKException {
		System.out.println("getDocumentMeta");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			String docflowId = docflow.getId().toString();
			for (Document d: docflow.getDocuments()) {
				String documentId = d.getId().toString();
				DocumentDescription documentDescription = apiDocflow.getDocumentMeta(docflowId, documentId);
				assertNotNull(documentDescription);
			}
		}		
	}

	/**
	 * Test of getSignatures method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetSignatures() throws ExternSDKException {
		System.out.println("getSignatures");
		System.out.println("getDocumentMeta");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			String docflowId = docflow.getId().toString();
			for (Document d: docflow.getDocuments()) {
				String documentId = d.getId().toString();
				List<Signature> links = apiDocflow.getSignatures(docflowId, documentId);
				assertNotNull(links);
			}
		}		
	}

	/**
	 * Test of sendReplyDocument method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testSendReplyDocument() throws ExternSDKException {
		System.out.println("getSignatures");
		System.out.println("getDocumentMeta");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			String docflowId = docflow.getId().toString();
			for (Document d: docflow.getDocuments()) {
				String documentId = d.getId().toString();
				apiDocflow.sendReplyDocument(docflowId, documentId);
				//List<Link> links = apiDocflow.getDocumentType(`, documentId)getDSignatures(docflowId, documentId);
				//assertNotNull(links);
			}
		}		
	}
	
	/**
	 * Test of getReplyDocument method, of class ExternSDKDocflow.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetReplyDocument() throws ExternSDKException {
		System.out.println("getSignatures");
		System.out.println("getDocumentMeta");
		for (TestData td: testData) {
			DraftTest dt = this.addDecryptedDocument(apiDraft, td);
			assertNotNull(dt);
			String draftId = dt.getId();
			Docflow docflow = apiDraft.send(draftId);
			assertNotNull(docflow);
			String docflowId = docflow.getId().toString();
			for (Document d: docflow.getDocuments()) {
				String documentId = d.getId().toString();
				//List<Link> links = apiDocflow.getDocumentType(`, documentId)getDSignatures(docflowId, documentId);
				//assertNotNull(links);
			}
		}		
	}
}
