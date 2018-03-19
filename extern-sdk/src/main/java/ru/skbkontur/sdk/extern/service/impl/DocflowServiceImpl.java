/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.service.DocflowService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.model.DocumentToSend;

/**
 *
 * @author AlexS
 */
public class DocflowServiceImpl extends BaseService implements DocflowService {
	private static final String EN_DFW = "Документооборот";
	private static final String EN_DOC = "Документ";
	private static final String EN_SGN = "Подпись";
	
	private DocflowsAdaptor docflowsApi;
	
	public void setDraftsApi(DocflowsAdaptor docflowsApi) {
		this.docflowsApi = docflowsApi;
	}

	/**
	 * Allow API user to get Docflow object
	 * 
	 * GET /v1/{accountId}/docflows/{docflowId}
	 * 
	 * @param docflowId String an docflow identity
	 * 
	 * @return CompletableFuture&lt;QueryContext&lt;Docflow&gt;&gt;
	 */
	@Override
	public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId) {
		QueryContext<Docflow> cxt = createQueryContext(EN_DFW);
		return cxt
			.setDocflowId(docflowId)
			.applyAsync(docflowsApi::lookupDocflow);
	}

	@Override
	public QueryContext<Docflow> lookupDocflow(QueryContext<?> parent) {
		QueryContext<Docflow> cxt = createQueryContext(parent, EN_DFW);
		return cxt.apply(docflowsApi::lookupDocflow);
	}

	@Override
	public CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId) {
		QueryContext<List<Document>> cxt = createQueryContext(EN_DFW);
		return cxt
			.setDocflowId(docflowId)
			.applyAsync(docflowsApi::getDocuments);
	}

	@Override
	public QueryContext<List<Document>> getDocuments(QueryContext<?> parent) {
		QueryContext<List<Document>> cxt = createQueryContext(parent, EN_DFW);
		return cxt.apply(docflowsApi::getDocuments);
	}
	
	@Override
	public CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId, String documentId) {
		QueryContext<Document> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::lookupDocument);
	}

	@Override
	public QueryContext<Document> lookupDocument(QueryContext<?> parent) {
		QueryContext<Document> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::lookupDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(String docflowId, String documentId) {
		QueryContext<DocumentDescription> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::lookupDescription);
	}

	@Override
	public QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent) {
		QueryContext<DocumentDescription> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::lookupDescription);
	}
	
	@Override
	public CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId, String documentId) {
		QueryContext<byte[]> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getEncryptedContent);
	}

	@Override
	public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent) {
		QueryContext<byte[]> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::getEncryptedContent);
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId, String documentId) {
		QueryContext<byte[]> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getDecryptedContent);
	}

	@Override
	public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent) {
		QueryContext<byte[]> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::getDecryptedContent);
	}

	@Override
	public CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId, String documentId) {
		QueryContext<List<Signature>> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignatures);
	}

	@Override
	public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent) {
		QueryContext<List<Signature>> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::getSignatures);
	}

	@Override
	public CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId, String documentId, String signatureId) {
		QueryContext<Signature> cxt = createQueryContext(EN_SGN);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignature);
	}

	@Override
	public QueryContext<Signature> getSignature(QueryContext<?> parent) {
		QueryContext<Signature> cxt = createQueryContext(parent, EN_SGN);
		return cxt.apply(docflowsApi::getSignature);
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId, String documentId, String signatureId) {
		QueryContext<byte[]> cxt = createQueryContext(EN_SGN);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignatureContent);
	}

	@Override
	public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent) {
		QueryContext<byte[]> cxt = createQueryContext(parent, EN_SGN);
		return cxt.apply(docflowsApi::getSignatureContent);
	}

	@Override
	public CompletableFuture<QueryContext<DocumentToSend>> getDocumentTypeReplyAsync(String docflowId, String documentType, String documentId) {
		QueryContext<DocumentToSend> cxt = createQueryContext(EN_SGN);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentType(documentType)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getDocumentTypeReply);
	}

	@Override
	public QueryContext<DocumentToSend> getDocumentTypeReply(QueryContext<?> parent) {
		QueryContext<DocumentToSend> cxt = createQueryContext(parent, EN_SGN);
		return cxt.apply(docflowsApi::getDocumentTypeReply);
	}

	@Override
	public CompletableFuture<QueryContext<Docflow>> addDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, DocumentToSend documentToSend) {
		QueryContext<Docflow> cxt = createQueryContext(EN_DOC);
		return cxt
			.setDocflowId(docflowId)
			.setDocumentType(documentType)
			.setDocumentId(documentId)
			.setDocumentToSend(documentToSend)
			.applyAsync(docflowsApi::addDocumentTypeReply);
	}

	@Override
	public QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent) {
		QueryContext<Docflow> cxt = createQueryContext(parent, EN_DOC);
		return cxt.apply(docflowsApi::addDocumentTypeReply);
	}
	
	@Override
	public CompletableFuture<QueryContext<List<DocumentToSend>>> getRepliesAsync(Docflow docflow) {
		QueryContext<List<DocumentToSend>> cxt = createQueryContext(EN_DFW);
		return cxt
			.setDocflow(docflow)
			.applyAsync(docflowsApi::lookupReplies);
	}

	@Override
	public QueryContext<List<DocumentToSend>> getReplies(QueryContext<?> parent) {
		QueryContext<List<DocumentToSend>> cxt = createQueryContext(parent, EN_DFW);
		return cxt.apply(docflowsApi::lookupReplies);
	}

	@Override
	public CompletableFuture<QueryContext<Docflow>> createReplyAsync(Docflow docflow) {
		QueryContext<Docflow> cxt = createQueryContext(EN_DFW);
		return cxt.applyAsync(docflowsApi::createReply);
	}
	
	@Override
	public QueryContext<Docflow> createReply(QueryContext<?> parent) {
		QueryContext<Docflow> cxt = createQueryContext(parent, EN_DFW);
		return cxt.apply(docflowsApi::createReply);
	}
}
