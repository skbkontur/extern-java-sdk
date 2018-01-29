/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AlexS
 */
public class DraftTest {
	public static class TestDocument {
		public String draftDocumentId;
		public String path;
		
		public TestDocument(String draftDocumentId, String path) {
			this.draftDocumentId = draftDocumentId;
			this.path = path;
		}
	}
	
	private String id;
	private final List<TestDocument> draftDocumentIdList = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TestDocument> getDraftDocumentIdList() {
		return draftDocumentIdList;
	}

	public void addDraftDocumentId(String draftDocumentId, String path) {
		draftDocumentIdList.add(new TestDocument(draftDocumentId, path));
	}
}
