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
 * @author AlexS
 */
public interface DocflowService {

  CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);

  QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

  CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);

  QueryContext<List<Document>> getDocuments(QueryContext<?> parent);

  CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId,
      String documentId);

  QueryContext<Document> lookupDocument(QueryContext<?> parent);

  CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(
      String docflowId, String documentId);

  QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);

  CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId,
      String documentId);

  QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);

  CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId,
      String documentId);

  QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);

  CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId,
      String documentId);

  QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);

  CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId,
      String documentId, String signatureId);

  QueryContext<Signature> getSignature(QueryContext<?> parent);

  CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId,
      String documentId, String signatureId);

  QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);

  CompletableFuture<QueryContext<DocumentToSend>> generateDocumentTypeReplyAsync(
      String docflowId, String documentType, String documentId, String x509Base64);

  QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> parent);

  CompletableFuture<QueryContext<Docflow>> addDocumentTypeReplyAsync(String docflowId,
      String documentType, String documentId, DocumentToSend documentToSend);

  QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent);

  CompletableFuture<QueryContext<List<DocumentToSend>>> generateRepliesAsync(Docflow docflow,
      String signerX509Base64);

  QueryContext<List<DocumentToSend>> generateReplies(QueryContext<?> parent);

  CompletableFuture<QueryContext<Docflow>> createReplyAsync(Docflow docflow);

  QueryContext<Docflow> sendReply(QueryContext<?> parent);

  CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(boolean finished,
      boolean incoming, long skip, int take, String innKpp, DateTime updatedFrom,
      DateTime updatedTo, DateTime createdFrom, DateTime createdTo, String type);

  QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);

  CompletableFuture<QueryContext<String>> printAsync(String docflowId, String documentId,
      String documentContentBase64);

  QueryContext<String> print(QueryContext<?> parent);
}
