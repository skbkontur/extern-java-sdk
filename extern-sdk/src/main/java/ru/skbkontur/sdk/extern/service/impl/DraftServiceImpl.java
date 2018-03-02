/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

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
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public class DraftServiceImpl extends BaseServiceImpl implements DraftService {

	private static final String EN_DFT = "Черновик";
	private static final String EN_DOC = "Документ";
	private static final String EN_SIGN = "Подпись";

	private DraftsAdaptor draftsApi;
	
	public void setDraftsApi(DraftsAdaptor draftsApi) {
		this.draftsApi = draftsApi;
	}

	@Override
	public CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization, String ipAddress) {
		return createQueryContext(EN_DFT)
			.setDraftMeta(new DraftMeta(sender,recipient,organization,ipAddress))
			.applyAsync(draftsApi::createDraft);
	}

	@Override
	public QueryContext<UUID> create(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::createDraft);
	}
	
	@Override
	public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.applyAsync(draftsApi::lookup);
	}

	@Override
	public QueryContext<Draft> lookup(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::lookup);
	}
	
	@Override
	public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.applyAsync(draftsApi::delete);
	}

	@Override
	public QueryContext<Void> delete(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::delete);
	}

	@Override
	public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.applyAsync(draftsApi::lookupDraftMeta);
	}
	
	@Override
	public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::lookupDraftMeta);
	}
	
	@Override
	public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.setDraftMeta(draftMeta)
			.applyAsync(draftsApi::updateDraftMeta);
	}
	
	@Override
	public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::updateDraftMeta);
	}
	
	@Override
	public CompletableFuture<QueryContext<Map<String,Object>>> checkAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.applyAsync(draftsApi::check);
	}

	@Override
	public QueryContext<Map<String,Object>> check(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::check);
	}

	@Override
	public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.applyAsync(draftsApi::prepare);
	}

	@Override
	public QueryContext<PrepareResult> prepare(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::prepare);
	}

	@Override
	public CompletableFuture<QueryContext<Docflow>> sendAsync(String draftId) {
		return createQueryContext(EN_DFT)
			.setDraftId(draftId)
			.setDeffered(true)
			.setForce(true)
			.applyAsync(draftsApi::send);
	}
	
	@Override
	public QueryContext<Docflow> send(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFT).apply(draftsApi::send);
	}
	
	@Override
	public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::deleteDocument);
	}

	@Override
	public QueryContext<Void> deleteDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::deleteDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::lookupDocument);
	}

	@Override
	public QueryContext<DraftDocument> lookupDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::lookupDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setDocumentContents(documentContents)
			.applyAsync(draftsApi::updateDocument);
	}
	
	@Override
	public QueryContext<DraftDocument> updateDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::updateDocument);
	}
	
	@Override
	public CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::printDocument);
	}

	@Override
	public QueryContext<String> printDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::printDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(String draftId, DocumentContents documentContents) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentContents(documentContents)
			.applyAsync(draftsApi::addDecryptedDocument);
	}

	@Override
	public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::addDecryptedDocument);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getDecryptedDocumentContent);
	}

	@Override
	public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::getDecryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setContent(content)
			.applyAsync(draftsApi::updateDecryptedDocumentContent);
	}

	@Override
	public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::updateDecryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getEncryptedDocumentContent);
	}

	@Override
	public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::getEncryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getSignatureContent);
	}

	@Override
	public QueryContext<String> getSignatureContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::getSignatureContent);
	}

	@Override
	public CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content) {
		return createQueryContext(EN_DOC)
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setContent(content)
			.applyAsync(draftsApi::updateSignature);
	}

	@Override
	public QueryContext<Void> updateSignature(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(draftsApi::updateSignature);
	}
}
