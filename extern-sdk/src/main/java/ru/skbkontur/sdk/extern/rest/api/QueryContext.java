/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentToSend;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 * 
 * Api Query Context
 * @param <R> some return type
 */
public class QueryContext<R> implements Serializable {
	private final Map<String,Object> params;

	public static final String ACCOUNT_ID = "accountId";
	public static final String ENTITY_NAME = "entityName";
	public static final String ENTITY_ID = "entityId";
	public static final String DRAFT_ID = "draftId";
	public static final String DOCUMENT_ID = "documentId";
	public static final String DOCFLOW_ID = "docflowId";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String REPLY_ID = "replyId";
	public static final String DOCUMENT_TO_SEND = "documentToSend";
	public static final String CLIENT_INFO = "clientInfo";
	public static final String DEFFERED = "deffered";
	public static final String DOCUMENT_CONTENT = "documentContent";
	public static final String FILE_NAME = "fileName";
	public static final String DOCUMENT_CONTENTS = "documentContents";
	public static final String CONTENT = "content";
	
	private R result;
	
	public QueryContext(UUID accountId, String entityName) {
		this.params = new ConcurrentHashMap<>();
		this.setAccountId(accountId);
		this.setEntityName(entityName);
	}
	
	public QueryContext<R> setResult(R result) {
		this.result = result;
		return this;
	}

	public R get() {
		return result;
	}
	
	public QueryContext<R> create(UUID accountId, String entityName) {
		return new QueryContext<>(accountId,entityName);
	}
	
	public UUID getAccountId() {
		return (UUID)params.get(ACCOUNT_ID);
	}
	
	public QueryContext<R> setAccountId(String accountId) {
		return set(ACCOUNT_ID, UUID.fromString(accountId));
	}
	
	public String getEntityName() {
		return (String)params.get(ENTITY_NAME);
	}
	
	public final QueryContext<R> setEntityName(String entityName) {
		return set(ENTITY_NAME, entityName);
	}
	
	public String getEntityId() {
		return (String)params.get(ENTITY_ID);
	}
	
	public QueryContext<R> setEntityId(String entityId) {
		return set(ENTITY_ID, entityId);
	}
	
	public final QueryContext<R> setAccountId(UUID accountId) {
		return set(ACCOUNT_ID, accountId);
	}
	
	public UUID getDraftId() {
		return (UUID)params.get(DRAFT_ID);
	}
	
	public QueryContext<R> setDraftId(String draftId) {
		return set(DRAFT_ID, UUID.fromString(draftId));
	}
	
	public UUID getDocflowId() {
		return (UUID)params.get(DOCFLOW_ID);
	}
	
	public QueryContext<R> setDocflowId(String docflowId) {
		return set(DOCFLOW_ID, UUID.fromString(docflowId));
	}
	
	public UUID getDocumentId() {
		return (UUID)params.get(DOCUMENT_ID);
	}
	
	public QueryContext<R> setDocumentId(String documentId) {
		return set(DOCUMENT_ID, UUID.fromString(documentId));
	}
	
	public String getDocumentType() {
		return (String)params.get(DOCUMENT_TYPE);
	}
	
	public QueryContext<R> setDocumentType(String documentType) {
		return set(DOCUMENT_TYPE, documentType);
	}
	
	public UUID getReplyId() {
		return (UUID)params.get(REPLY_ID);
	}
	
	public QueryContext<R> setReplyId(String replyId) {
		return set(REPLY_ID, UUID.fromString(replyId));
	}
	
	public DocumentToSend getDocumentToSend() {
		return (DocumentToSend)params.get(DOCUMENT_TO_SEND);
	}
	
	public QueryContext<R> setDocumentToSend(DocumentToSend documentToSend) {
		return set(DOCUMENT_TO_SEND, documentToSend);
	}
	
	public DraftMeta getClientInfo() {
		return (DraftMeta)params.get(CLIENT_INFO);
	}
	
	public QueryContext<R> setClientInfo(DraftMeta clientInfo) {
		return set(CLIENT_INFO, clientInfo);
	}
	
	public boolean getDeffered() {
		return (boolean)params.get(DEFFERED);
	}
	
	public QueryContext<R> setDeffered(boolean deffered) {
		return set(DEFFERED, deffered);
	}
	
	public byte[] getDocumentContent() {
		return (byte[])params.get(DOCUMENT_CONTENT);
	}
	
	public QueryContext<R> setDocumentContent(byte[] documentContent) {
		return set(DOCUMENT_CONTENT, documentContent);
	}
	
	public String getFileName() {
		return (String)params.get(FILE_NAME);
	}
	
	public QueryContext<R> setFileName(String fileName) {
		return set(FILE_NAME, fileName);
	}
	
	public DocumentContents getDocumentContents() {
		return (DocumentContents)params.get(DOCUMENT_CONTENTS);
	}
	
	public QueryContext<R> setDocumentContents(DocumentContents documentContents) {
		return set(DOCUMENT_CONTENTS, documentContents);
	}

	public byte[] getContent() {
		return (byte[])params.get(CONTENT);
	}
	
	public QueryContext<R> setContent(byte[] content) {
		return set(CONTENT,content);
	}
	
	public QueryContext<R> set(String name, Object val) {
		params.put(name, val);
		return this;
	}

	public Object get(String name) {
		return params.get(name);
	}
}
