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
import ru.skbkontur.sdk.extern.model.Reply;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.service.DocflowService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.model.DocumentToSend;

/**
 *
 * @author AlexS
 */
public class DocflowServiceImpl extends BaseServiceImpl implements DocflowService {
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
	
		return createQueryContext(EN_DFW)
			.setDocflowId(docflowId)
			.applyAsync(docflowsApi::lookupDocflow);
	}

	@Override
	public QueryContext<Docflow> lookupDocflow(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFW).apply(docflowsApi::lookupDocflow);
	}

	@Override
	public CompletableFuture<QueryContext<List<Document>>> getDocuments(String docflowId) {
		return createQueryContext(EN_DFW)
			.setDocflowId(docflowId)
			.applyAsync(docflowsApi::getDocuments);
	}

	@Override
	public QueryContext<List<Document>> getDocuments(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DFW).apply(docflowsApi::getDocuments);
	}
	
	@Override
	public CompletableFuture<Document> lookupDocument(String docflowId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::lookupDocument);
	}

	@Override
	public QueryContext<Document> lookupDocument(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::lookupDocument);
	}

	@Override
	public CompletableFuture<QueryContext<DocumentDescription>> lookupDescription(String docflowId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::lookupDescription);
	}

	@Override
	public QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::lookupDescription);
	}
	
	@Override
	public CompletableFuture<QueryContext<byte[]>> getEncryptedContent(String docflowId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getEncryptedContent);
	}

	@Override
	public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::getEncryptedContent);
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> getDecryptedContent(String docflowId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getDecryptedContent);
	}

	@Override
	public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::getDecryptedContent);
	}

	@Override
	public CompletableFuture<QueryContext<List<Signature>>> getSignatures(String docflowId, String documentId) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignatures);
	}

	@Override
	public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::getSignatures);
	}

	@Override
	public CompletableFuture<QueryContext<Signature>> getSignature(String docflowId, String documentId, String signatureId) {
		return createQueryContext(EN_SGN)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignature);
	}

	@Override
	public QueryContext<Signature> getSignature(QueryContext<?> parent) {
		return createQueryContext(parent, EN_SGN).apply(docflowsApi::getSignature);
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> getSignatureContent(String docflowId, String documentId, String signatureId) {
		return createQueryContext(EN_SGN)
			.setDocflowId(docflowId)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getSignatureContent);
	}

	@Override
	public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent) {
		return createQueryContext(parent, EN_SGN).apply(docflowsApi::getSignatureContent);
	}

	@Override
	public CompletableFuture<QueryContext<Reply>> getDocumentTypeReply(String docflowId, String documentType, String documentId) {
		return createQueryContext(EN_SGN)
			.setDocflowId(docflowId)
			.setDocumentType(documentType)
			.setDocumentId(documentId)
			.applyAsync(docflowsApi::getDocumentTypeReply);
	}

	@Override
	public QueryContext<Reply> getDocumentTypeReply(QueryContext<?> parent) {
		return createQueryContext(parent, EN_SGN).apply(docflowsApi::getDocumentTypeReply);
	}

	@Override
	public CompletableFuture<QueryContext<Docflow>> addDocumentTypeReply(String docflowId, String documentType, String documentId, DocumentToSend documentToSend) {
		return createQueryContext(EN_DOC)
			.setDocflowId(docflowId)
			.setDocumentType(documentType)
			.setDocumentId(documentId)
			.setDocumentToSend(documentToSend)
			.applyAsync(docflowsApi::addDocumentTypeReply);
	}

	@Override
	public QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent) {
		return createQueryContext(parent, EN_DOC).apply(docflowsApi::addDocumentTypeReply);
	}
}
