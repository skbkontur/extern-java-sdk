/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service;

import org.joda.time.DateTime;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.provider.Providers;


/**
 * @author AlexS
 */
public interface DocflowService extends Providers {

    CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);

    QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

    CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);

    QueryContext<List<Document>> getDocuments(QueryContext<?> parent);

    CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId, String documentId);

    QueryContext<Document> lookupDocument(QueryContext<?> parent);

    CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(String docflowId, String documentId);

    QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId, String documentId);

    QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId, String documentId);

    QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId, String documentId);

    QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);

    CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId, String documentId, String signatureId);

    QueryContext<Signature> getSignature(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId, String documentId, String signatureId);

    QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<DocumentToSend>> generateDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, String x509Base64);

    QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> parent);

    CompletableFuture<QueryContext<Docflow>> addDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, DocumentToSend documentToSend);

    QueryContext<Docflow> addDocumentTypeReply(QueryContext<?> parent);

    CompletableFuture<QueryContext<List<DocumentToSend>>> generateRepliesAsync(Docflow docflow, String signerX509Base64);

    QueryContext<List<DocumentToSend>> generateReplies(QueryContext<?> parent);

    CompletableFuture<QueryContext<Docflow>> createReplyAsync(Docflow docflow);

    QueryContext<Docflow> sendReply(QueryContext<?> parent);

    CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(boolean finished, boolean incoming, long skip, int take, String innKpp, DateTime updatedFrom, DateTime updatedTo, DateTime createdFrom, DateTime createdTo, String type);

    QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);

    CompletableFuture<QueryContext<String>> printAsync(String docflowId, String documentId, String documentContentBase64);

    QueryContext<String> print(QueryContext<?> parent);
}
