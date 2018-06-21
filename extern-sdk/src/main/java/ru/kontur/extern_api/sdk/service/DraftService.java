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

import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfoV2;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.provider.Providers;


/**
 * @author AlexS
 */
public interface DraftService extends Providers {

    /**
     * Create new a draft
     * <p>
     * POST /v1/{accountId}/drafts
     *
     * @param sender       Sender отправитель декларации
     * @param recipient    Recipient получатель декларации
     * @param organization Organization организация, на которую создана декларация
     * @return CompletableFuture&lt;QueryContext&lt;UUID&gt;&gt; идентификатор черновика
     */
    CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization);

    QueryContext<UUID> create(QueryContext<?> cxt);

    /**
     * lookup a draft by an identifier
     * <p>
     * GET /v1/{accountId}/drafts/{draftId}
     *
     * @param draftId String a draft identifier
     * @return Draft
     */
    CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId);

    QueryContext<Draft> lookup(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> deleteAsync(String draftId);

    QueryContext<Void> delete(QueryContext<?> cxt);

    CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId);

    QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

    CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta);

    QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Map<String, Object>>> checkAsync(String draftId);

    QueryContext<Map<String, Object>> check(QueryContext<?> cxt);

    CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId);

    QueryContext<PrepareResult> prepare(QueryContext<?> cxt);

    CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId);

    QueryContext<List<Docflow>> send(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId);

    QueryContext<Void> deleteDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId);

    QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents);

    QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId);

    QueryContext<String> printDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId, DocumentContents documentContents);

    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId);

    QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content);

    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

    CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId);

    QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);

    CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId);

    QueryContext<String> getSignatureContent(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content);

    QueryContext<Void> updateSignature(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> createUSN1Async(String draftId, String documentId, UsnServiceContractInfo usn);

    QueryContext<Void> createUSN1(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> createUSN2Async(String draftId, String documentId, UsnServiceContractInfoV2 usn);

    QueryContext<Void> createUSN2(QueryContext<?> cxt);

    CompletableFuture<QueryContext<SignInitiation>> cloudSignAsync(String draftId);

    QueryContext<SignInitiation> cloudSign(QueryContext<?> cxt);

    CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync(String draftId, String requestId, String code);

    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt, String code);
}
