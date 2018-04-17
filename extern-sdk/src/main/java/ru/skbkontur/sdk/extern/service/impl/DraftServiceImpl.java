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

package ru.skbkontur.sdk.extern.service.impl;

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
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


/**
 * @author AlexS
 */
public class DraftServiceImpl extends BaseService<DraftsAdaptor> implements DraftService {

    private static final String EN_DFT = "Черновик";
    private static final String EN_DOC = "Документ";
    private static final String EN_SIGN = "Подпись";

    @Override
    public CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization) {
        QueryContext<UUID> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftMeta(new DraftMeta(sender, recipient, organization))
                .applyAsync(api::createDraft);
    }

    @Override
    public QueryContext<UUID> create(QueryContext<?> parent) {
        QueryContext<UUID> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::createDraft);
    }

    @Override
    public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId) {
        QueryContext<Draft> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(api::lookup);
    }

    @Override
    public QueryContext<Draft> lookup(QueryContext<?> parent) {
        QueryContext<Draft> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::lookup);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId) {
        QueryContext<Void> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(api::delete);
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::delete);
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId) {
        QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(api::lookupDraftMeta);
    }

    @Override
    public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> parent) {
        QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::lookupDraftMeta);
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta) {
        QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .setDraftMeta(draftMeta)
                .applyAsync(api::updateDraftMeta);
    }

    @Override
    public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> parent) {
        QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::updateDraftMeta);
    }

    @Override
    public CompletableFuture<QueryContext<Map<String, Object>>> checkAsync(String draftId) {
        QueryContext<Map<String, Object>> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(api::check);
    }

    @Override
    public QueryContext<Map<String, Object>> check(QueryContext<?> parent) {
        QueryContext<Map<String, Object>> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::check);
    }

    @Override
    public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId) {
        QueryContext<PrepareResult> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(api::prepare);
    }

    @Override
    public QueryContext<PrepareResult> prepare(QueryContext<?> parent) {
        QueryContext<PrepareResult> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::prepare);
    }

    @Override
    public CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId) {
        QueryContext<List<Docflow>> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .setDeffered(true)
                .setForce(true)
                .applyAsync(api::send);
    }

    @Override
    public QueryContext<List<Docflow>> send(QueryContext<?> parent) {
        QueryContext<List<Docflow>> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(api::send);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::deleteDocument);
    }

    @Override
    public QueryContext<Void> deleteDocument(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::deleteDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::lookupDocument);
    }

    @Override
    public QueryContext<DraftDocument> lookupDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::lookupDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setDocumentContents(documentContents)
                .applyAsync(api::updateDocument);
    }

    @Override
    public QueryContext<DraftDocument> updateDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::updateDocument);
    }

    @Override
    public CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::printDocument);
    }

    @Override
    public QueryContext<String> printDocument(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::printDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId, DocumentContents documentContents) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentContents(documentContents)
                .applyAsync(api::addDecryptedDocument);
    }

    @Override
    public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::addDecryptedDocument);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::getDecryptedDocumentContent);
    }

    @Override
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::getDecryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setContent(content)
                .applyAsync(api::updateDecryptedDocumentContent);
    }

    @Override
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::updateDecryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::getEncryptedDocumentContent);
    }

    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::getEncryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(api::getSignatureContent);
    }

    @Override
    public QueryContext<String> getSignatureContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::getSignatureContent);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setContent(content)
                .applyAsync(api::updateSignature);
    }

    @Override
    public QueryContext<Void> updateSignature(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::updateSignature);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> createUSN1Async(String draftId, String documentId, UsnServiceContractInfo usn) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setUsnServiceContractInfo(usn)
                .applyAsync(api::createUSN1);
    }

    @Override
    public QueryContext<Void> createUSN1(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::createUSN1);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> createUSN2Async(String draftId, String documentId, UsnServiceContractInfoV2 usn) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setUsnServiceContractInfoV2(usn)
                .applyAsync(api::createUSN2);
    }

    @Override
    public QueryContext<Void> createUSN2(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(api::createUSN2);
    }

}
