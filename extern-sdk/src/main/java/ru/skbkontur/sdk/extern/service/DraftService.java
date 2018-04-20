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
import ru.skbkontur.sdk.extern.model.UsnServiceContractInfo;
import ru.skbkontur.sdk.extern.model.UsnServiceContractInfoV2;
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
	CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient,
			Organization organization);
	QueryContext<UUID> create(QueryContext<?> cxt);

	/**
	 * lookup a draft by an identifier
	 *
	 * GET /v1/{accountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * 
	 * @return Draft
	 */
	CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId);
	QueryContext<Draft> lookup(QueryContext<?> cxt);

	CompletableFuture<QueryContext<Void>> deleteAsync(String draftId);
	QueryContext<Void> delete(QueryContext<?> cxt);

	CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId);
	QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

	CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId,
			DraftMeta draftMeta);
	QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

	CompletableFuture<QueryContext<Map<String,Object>>> checkAsync(String draftId);
	QueryContext<Map<String,Object>> check(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId);
	QueryContext<PrepareResult> prepare(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId);
	QueryContext<List<Docflow>> send(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId);
	QueryContext<Void> deleteDocument(QueryContext<?> cxt);

	CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId,
			String documentId);
	QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt);

	CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId,
			String documentId, DocumentContents documentContents);
	QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId);
	QueryContext<String> printDocument(QueryContext<?> cxt);

	CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId,
			DocumentContents documentContents);
	QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId,
			String documentId);
	QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId,
			String documentId, byte[] content);
	QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

	CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId,
			String documentId);
	QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);
	
	CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId,
			String documentId);
	QueryContext<String> getSignatureContent(QueryContext<?> cxt);

	CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId,
			byte[] content);
	QueryContext<Void> updateSignature(QueryContext<?> cxt);

	CompletableFuture<QueryContext<Void>> createUSN1Async(String draftId, String documentId,
			UsnServiceContractInfo usn);
	QueryContext<Void> createUSN1(QueryContext<?> cxt);

	CompletableFuture<QueryContext<Void>> createUSN2Async(String draftId, String documentId,
			UsnServiceContractInfoV2 usn);
	QueryContext<Void> createUSN2(QueryContext<?> cxt);
}
