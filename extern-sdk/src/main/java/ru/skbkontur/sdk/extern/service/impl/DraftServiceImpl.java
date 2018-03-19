/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

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
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public class DraftServiceImpl extends BaseService implements DraftService {

	private static final String EN_DFT = "Черновик";
	private static final String EN_DOC = "Документ";
	private static final String EN_SIGN = "Подпись";

	private DraftsAdaptor draftsApi;
	
	public void setDraftsApi(DraftsAdaptor draftsApi) {
		this.draftsApi = draftsApi;
	}

	@Override
	public CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization) {
		QueryContext<UUID> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftMeta(new DraftMeta(sender,recipient,organization))
			.applyAsync(draftsApi::createDraft);
	}

	@Override
	public QueryContext<UUID> create(QueryContext<?> parent) {
		QueryContext<UUID> cxt = createQueryContext(parent,EN_DFT);
		return cxt.apply(draftsApi::createDraft);
	}
	
	@Override
	public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId) {
		QueryContext<Draft> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.applyAsync(draftsApi::lookup);
	}

	@Override
	public QueryContext<Draft> lookup(QueryContext<?> parent) {
		QueryContext<Draft> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::lookup);
	}
	
	@Override
	public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId) {
		QueryContext<Void> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.applyAsync(draftsApi::delete);
	}

	@Override
	public QueryContext<Void> delete(QueryContext<?> parent) {
		QueryContext<Void> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::delete);
	}

	@Override
	public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId) {
		QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.applyAsync(draftsApi::lookupDraftMeta);
	}
	
	@Override
	public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> parent) {
		QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::lookupDraftMeta);
	}
	
	@Override
	public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta) {
		QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.setDraftMeta(draftMeta)
			.applyAsync(draftsApi::updateDraftMeta);
	}
	
	@Override
	public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> parent) {
		QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::updateDraftMeta);
	}
	
	@Override
	public CompletableFuture<QueryContext<Map<String,Object>>> checkAsync(String draftId) {
		QueryContext<Map<String,Object>> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.applyAsync(draftsApi::check);
	}

	@Override
	public QueryContext<Map<String,Object>> check(QueryContext<?> parent) {
		QueryContext<Map<String,Object>> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::check);
	}

	@Override
	public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId) {
		QueryContext<PrepareResult> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.applyAsync(draftsApi::prepare);
	}

	@Override
	public QueryContext<PrepareResult> prepare(QueryContext<?> parent) {
		QueryContext<PrepareResult> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::prepare);
	}

	@Override
	public CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId) {
		QueryContext<List<Docflow>> cxt = createQueryContext(EN_DFT);
		return cxt
			.setDraftId(draftId)
			.setDeffered(true)
			.setForce(true)
			.applyAsync(draftsApi::send);
	}
	
	@Override
	public QueryContext<List<Docflow>> send(QueryContext<?> parent) {
		QueryContext<List<Docflow>> cxt = createQueryContext(parent, EN_DFT);
		return cxt.apply(draftsApi::send);
	}
	
	@Override
	public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId) {
		QueryContext<Void> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::deleteDocument);
	}

	@Override
	public QueryContext<Void> deleteDocument(QueryContext<?> parent) {
		QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::deleteDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId) {
		QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::lookupDocument);
	}

	@Override
	public QueryContext<DraftDocument> lookupDocument(QueryContext<?> parent) {
		QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::lookupDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents) {
		QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setDocumentContents(documentContents)
			.applyAsync(draftsApi::updateDocument);
	}
	
	@Override
	public QueryContext<DraftDocument> updateDocument(QueryContext<?> parent) {
		QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::updateDocument);
	}
	
	@Override
	public CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId) {
		QueryContext<String> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::printDocument);
	}

	@Override
	public QueryContext<String> printDocument(QueryContext<?> parent) {
		QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::printDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(String draftId, DocumentContents documentContents) {
		QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentContents(documentContents)
			.applyAsync(draftsApi::addDecryptedDocument);
	}

	@Override
	public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> parent) {
		QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::addDecryptedDocument);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId) {
		QueryContext<String> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getDecryptedDocumentContent);
	}

	@Override
	public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> parent) {
		QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::getDecryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content) {
		QueryContext<Void> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setContent(content)
			.applyAsync(draftsApi::updateDecryptedDocumentContent);
	}

	@Override
	public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> parent) {
		QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::updateDecryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId) {
		QueryContext<String> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getEncryptedDocumentContent);
	}

	@Override
	public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> parent) {
		QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::getEncryptedDocumentContent);
	}

	@Override
	public CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId) {
		QueryContext<String> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.applyAsync(draftsApi::getSignatureContent);
	}

	@Override
	public QueryContext<String> getSignatureContent(QueryContext<?> parent) {
		QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::getSignatureContent);
	}

	@Override
	public CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content) {
		QueryContext<Void> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDraftId(draftId)
			.setDocumentId(documentId)
			.setContent(content)
			.applyAsync(draftsApi::updateSignature);
	}

	@Override
	public QueryContext<Void> updateSignature(QueryContext<?> parent) {
		QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(draftsApi::updateSignature);
	}
}
