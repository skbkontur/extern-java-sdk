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
	
	private static ExternSDKDraft api;
	private static	DraftMeta[] clientInfos;
	
	public ExternSDKDraftTest() {
	}
	
	@BeforeClass
	public static void setUpClass() throws ExternSDKException {
		api = new ExternSDK().draft;
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
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testCreateDraft() throws ExternSDKException {
		System.out.println("createDraft");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> result = api.createDraft(ci);
			assertNotNull(result);
		}
	}

	/**
	 * Test of getDraftById method, of class ExternSDKDraft.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDraft() throws ExternSDKException {
		System.out.println("getDraft");
		if (clientInfos != null) {
			for (DraftMeta ci : clientInfos) {
				Map<String, Object> draft = api.createDraft(ci);
				String draftId = (String) draft.get("id");
				Object r = api.getDraftById(draftId);
				assertNotNull(r);
			}
		}
	}

	/**
	 * Test of deleteDraftById method, of class ExternSDKDraft.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testDeleteDraft() throws ExternSDKException {
		System.out.println("deleteDraft");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = api.createDraft(ci);
			assertNotNull(draft);
			api.deleteDraft((String)draft.get("id"));
		}
	}

	/**
	 * Test of getDraftMetaById method, of class ExternSDKDraft.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testGetDraftMeta() throws ExternSDKException {
		System.out.println("getDraftMeta");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = api.createDraft(ci);
			assertNotNull(draft);
			Object result = api.getDraftMeta((String)draft.get("id"));
			assertNotNull(result);
		}
	}

	/**
	 * Test of updateDraftMetaById method, of class ExternSDKDraft.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testUpdateDraftMeta() throws ExternSDKException {
		System.out.println("getDraftMeta");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = api.createDraft(ci);
			assertNotNull(draft);
			String name = "new " + ci.getOrganization().getFullName();
			ci.getOrganization().setFullName(name);
			Object result = api.updateDraftMeta((String)draft.get("id"),ci);
			assertNotNull(result);
		}
	}

	/**
	 * Test of send method, of class ExternSDKDraft.
	 * @throws ru.skbkontur.sdk.extern.ExternSDKException
	 */
	@Test
	public void testSend() throws ExternSDKException {
		System.out.println("send");
		for (DraftMeta ci: clientInfos) {
			Map<String, Object> draft = api.createDraft(ci);
			assertNotNull(draft);
			String name = "new " + ci.getOrganization().getFullName();
			ci.getOrganization().setFullName(name);
			api.send((String)draft.get("id"));
		}
	}
	
	private static DraftMeta[] getTestClientInfos() {
		Gson gson = new Gson();
		InputStream is = ExternSDKDraftTest.class.getClassLoader().getResourceAsStream("clientInfosTest.json");
		InputStreamReader reader = new java.io.InputStreamReader(is);
		return gson.fromJson(reader, DraftMeta[].class);
	}
}
