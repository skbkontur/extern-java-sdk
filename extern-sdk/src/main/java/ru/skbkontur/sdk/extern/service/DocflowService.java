/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.joda.time.DateTime;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.DocflowPage;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.DocumentToSend;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public interface DocflowService {
	
	public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);
	public QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);
	public QueryContext<List<Document>> getDocuments(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId, String documentId);
	public QueryContext<Document> lookupDocument(QueryContext<?> parent);

	public CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(String docflowId, String documentId);
	public QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId, String documentId);
	public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId, String documentId);
	public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId, String documentId);
	public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId, String documentId, String signatureId);
	public QueryContext<Signature> getSignature(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId, String documentId, String signatureId);
	public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);
	
	public CompletableFuture<QueryContext<DocumentToSend>> generateDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, byte[] x509Base64);
	public QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> parent);

	public CompletableFuture<QueryContext<Docflow>> addDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, DocumentToSend documentToSend);
	public QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent);

	public CompletableFuture<QueryContext<List<DocumentToSend>>> generateRepliesAsync(Docflow docflow);
	public QueryContext<List<DocumentToSend>> generateReplies(QueryContext<?> parent);

	public CompletableFuture<QueryContext<Docflow>> createReplyAsync(Docflow docflow);
	public QueryContext<Docflow> sendReply(QueryContext<?> parent);

	public CompletableFuture<QueryContext<DocflowPage>> getDocflows(boolean finished,boolean incoming,long skip,int take,String innKpp,DateTime updatedFrom,DateTime updatedTo,DateTime createdFrom,DateTime createdTo,String type);
	public QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);
}
