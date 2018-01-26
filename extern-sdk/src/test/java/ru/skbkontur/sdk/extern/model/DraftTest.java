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
	private String id;
	private List<String> draftDocumentIdList = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getDraftDocumentIdList() {
		return draftDocumentIdList;
	}

	public void setDraftDocumentIdList(List<String> draftDocumentIdList) {
		this.draftDocumentIdList = draftDocumentIdList;
	}
	
	public void addDraftDocumentId(String draftDocumentId) {
		draftDocumentIdList.add(draftDocumentId);
	}
}
