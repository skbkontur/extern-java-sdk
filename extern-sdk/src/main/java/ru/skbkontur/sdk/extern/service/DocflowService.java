/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.DocumentToSend;
import ru.skbkontur.sdk.extern.model.Reply;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public interface DocflowService {
	
	public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);
	public QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<List<Document>>> getDocuments(String docflowId);
	public QueryContext<List<Document>> getDocuments(QueryContext<?> parent);
	
	public CompletableFuture<Document> lookupDocument(String docflowId, String documentId);
	public QueryContext<Document> lookupDocument(QueryContext<?> parent);

	public CompletableFuture<QueryContext<DocumentDescription>> lookupDescription(String docflowId, String documentId);
	public QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getEncryptedContent(String docflowId, String documentId);
	public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getDecryptedContent(String docflowId, String documentId);
	public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<List<Signature>>> getSignatures(String docflowId, String documentId);
	public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<Signature>> getSignature(String docflowId, String documentId, String signatureId);
	public QueryContext<Signature> getSignature(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getSignatureContent(String docflowId, String documentId, String signatureId);
	public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<Reply>> getDocumentTypeReply(String docflowId, String documentType, String documentId);
	public QueryContext<Reply> getDocumentTypeReply(QueryContext<?> parent);

	public CompletableFuture<QueryContext<Docflow>> addDocumentTypeReply(String docflowId, String documentType, String documentId, DocumentToSend documentToSend);
	public QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent);
}
