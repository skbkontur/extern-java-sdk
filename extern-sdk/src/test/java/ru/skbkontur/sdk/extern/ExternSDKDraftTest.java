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
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class ExternSDKDraftTest {

	private static ExternSDKDraft apiDraft;
	private static ExternSDKDocument apiDoc;
	private static DraftMeta[] clientInfos;

	public ExternSDKDraftTest() {
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
	 * Test of createDraft method, of class ExternSDKDraft.
	 *
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testCreateDraft() throws ExternSDKException {
		System.out.println("createDraft");
		for (DraftMeta ci : clientInfos) {
			Map<String, Object> result = apiDraft.createDraft(ci);
			assertNotNull(result);
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
		if (clientInfos != null) {
			for (DraftMeta ci : clientInfos) {
				Map<String, Object> draft = apiDraft.createDraft(ci);
				String draftId = (String) draft.get("id");
				Object r = apiDraft.getDraft(draftId);
				assertNotNull(r);
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
		for (DraftMeta ci : clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			apiDraft.deleteDraft((String) draft.get("id"));
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
		for (DraftMeta ci : clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			Object result = apiDraft.getDraftMeta((String) draft.get("id"));
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
		System.out.println("getDraftMeta");
		for (DraftMeta ci : clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String name = "new " + ci.getOrganization().getFullName();
			ci.getOrganization().setFullName(name);
			Object result = apiDraft.updateDraftMeta((String) draft.get("id"), ci);
			assertNotNull(result);
		}
	}

	/**
	 * Test of send method, of class ExternSDKDraft.
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/check
	 * 
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testCheck() throws ExternSDKException {
		System.out.println("check");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> result = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(result);
			apiDraft.check(draftId, true);
		}		
	}
	
	/**
	 * Test of send method, of class ExternSDKDraft.
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/prepare
	 * 
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testPrepare() throws ExternSDKException {
		System.out.println("prepare");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> result = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(result);
			apiDraft.prepare(draftId, true);
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
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = apiDraft.createDraft(ci);
			assertNotNull(draft);
			String draftId = (String)draft.get("id");
			byte[] documentBody = ("This is a simple text document: " + draftId).getBytes();
			Map<String,Object> result = apiDoc.addUncryptedDocument(draftId, documentBody, "unformal-"+draftId+".txt");
			assertNotNull(result);
			apiDraft.send(draftId);
		}		
	}
	
	private static DraftMeta[] getTestClientInfos() {
		Gson gson = new Gson();
		InputStream is = ExternSDKDraftTest.class.getClassLoader().getResourceAsStream("clientInfosTest.json");
		InputStreamReader reader = new java.io.InputStreamReader(is);
		return gson.fromJson(reader, DraftMeta[].class);
	}
}
