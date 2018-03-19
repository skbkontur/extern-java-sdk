/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.DocumentContents;
import ru.skbkontur.sdk.extern.model.Draft;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.model.DraftMeta;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.PrepareResult;
import ru.skbkontur.sdk.extern.model.Recipient;
import ru.skbkontur.sdk.extern.model.Sender;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public interface DraftService {
	/**
	 * Create new a draft
	 *
	 * POST /v1/{accountId}/drafts
	 *
	 * @param sender Sender отправитель декларации
	 * @param recipient Recipient получатель декларации
	 * @param organization Organization организация, на которую создана декларация
	 * @return CompletableFuture&lt;QueryContext&lt;UUID&gt;&gt; идентификатор черновика 
	 */
	public CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization);
	public QueryContext<UUID> create(QueryContext<?> cxt);

	/**
	 * lookup a draft by an identifier
	 *
	 * GET /v1/{accountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * 
	 * @return Draft
	 */
	public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId);
	public QueryContext<Draft> lookup(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId);
	public QueryContext<Void> delete(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId);
	public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta);
	public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<Map<String,Object>>> checkAsync(String draftId);
	public QueryContext<Map<String,Object>> check(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId);
	public QueryContext<PrepareResult> prepare(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId);
	public QueryContext<List<Docflow>> send(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId);
	public QueryContext<Void> deleteDocument(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId);
	public QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents);
	public QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId);
	public QueryContext<String> printDocument(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(String draftId, DocumentContents documentContents);
	public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId);
	public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content);
	public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId);
	public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);
	
	public CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId);
	public QueryContext<String> getSignatureContent(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content);
	public QueryContext<Void> updateSignature(QueryContext<?> cxt);
}
