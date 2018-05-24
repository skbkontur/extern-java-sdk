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

package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfoV2;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;


/**
 * @author AlexS
 */
public class DraftServiceImpl extends AbstractService<DraftsAdaptor> implements DraftService {

    private static final String EN_DFT = "Черновик";
    private static final String EN_DOC = "Документ";
    private static final String EN_SIGN = "Подпись";

    private final DraftsAdaptor draftsAdaptor;
    
    public DraftServiceImpl(DraftsAdaptor draftsAdaptor) {
        this.draftsAdaptor = draftsAdaptor;
    }
    
    @Override
	public DraftService serviceBaseUriProvider(UriProvider serviceBaseUriProvider) {
		super.serviceBaseUriProvider = serviceBaseUriProvider;
        return this;
	}

    @Override
	public DraftService authenticationProvider(AuthenticationProvider authenticationProvider) {
		super.authenticationProvider = authenticationProvider;
        return this;
	}

    @Override
	public DraftService accountProvider(AccountProvider accountProvider) {
		super.accountProvider = accountProvider;
        return this;
	}

    @Override
	public DraftService apiKeyProvider(ApiKeyProvider apiKeyProvider) {
		super.apiKeyProvider = apiKeyProvider;
        return this;
	}

    @Override
	public DraftService cryptoProvider(CryptoProvider cryptoProvider) {
		super.cryptoProvider = cryptoProvider;
        return this;
	}

    @Override
    public CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient, Organization organization) {
        QueryContext<UUID> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftMeta(new DraftMeta(sender, recipient, organization))
                .applyAsync(draftsAdaptor::createDraft);
    }

    @Override
    public QueryContext<UUID> create(QueryContext<?> parent) {
        QueryContext<UUID> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::createDraft);
    }

    @Override
    public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId) {
        QueryContext<Draft> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(draftsAdaptor::lookup);
    }

    @Override
    public QueryContext<Draft> lookup(QueryContext<?> parent) {
        QueryContext<Draft> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::lookup);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId) {
        QueryContext<Void> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(draftsAdaptor::delete);
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::delete);
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId) {
        QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(draftsAdaptor::lookupDraftMeta);
    }

    @Override
    public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> parent) {
        QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::lookupDraftMeta);
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId, DraftMeta draftMeta) {
        QueryContext<DraftMeta> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .setDraftMeta(draftMeta)
                .applyAsync(draftsAdaptor::updateDraftMeta);
    }

    @Override
    public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> parent) {
        QueryContext<DraftMeta> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::updateDraftMeta);
    }

    @Override
    public CompletableFuture<QueryContext<Map<String, Object>>> checkAsync(String draftId) {
        QueryContext<Map<String, Object>> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(draftsAdaptor::check);
    }

    @Override
    public QueryContext<Map<String, Object>> check(QueryContext<?> parent) {
        QueryContext<Map<String, Object>> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::check);
    }

    @Override
    public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId) {
        QueryContext<PrepareResult> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .applyAsync(draftsAdaptor::prepare);
    }

    @Override
    public QueryContext<PrepareResult> prepare(QueryContext<?> parent) {
        QueryContext<PrepareResult> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::prepare);
    }

    @Override
    public CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId) {
        QueryContext<List<Docflow>> cxt = createQueryContext(EN_DFT);
        return cxt
                .setDraftId(draftId)
                .setDeffered(true)
                .setForce(true)
                .applyAsync(draftsAdaptor::send);
    }

    @Override
    public QueryContext<List<Docflow>> send(QueryContext<?> parent) {
        QueryContext<List<Docflow>> cxt = createQueryContext(parent, EN_DFT);
        return cxt.apply(draftsAdaptor::send);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::deleteDocument);
    }

    @Override
    public QueryContext<Void> deleteDocument(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::deleteDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId, String documentId) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::lookupDocument);
    }

    @Override
    public QueryContext<DraftDocument> lookupDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::lookupDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId, String documentId, DocumentContents documentContents) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setDocumentContents(documentContents)
                .applyAsync(draftsAdaptor::updateDocument);
    }

    @Override
    public QueryContext<DraftDocument> updateDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::updateDocument);
    }

    @Override
    public CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::printDocument);
    }

    @Override
    public QueryContext<String> printDocument(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::printDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId, DocumentContents documentContents) {
        QueryContext<DraftDocument> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentContents(documentContents)
                .applyAsync(draftsAdaptor::addDecryptedDocument);
    }

    @Override
    public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> parent) {
        QueryContext<DraftDocument> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::addDecryptedDocument);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::getDecryptedDocumentContent);
    }

    @Override
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::getDecryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId, String documentId, byte[] content) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setContent(content)
                .applyAsync(draftsAdaptor::updateDecryptedDocumentContent);
    }

    @Override
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::updateDecryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::getEncryptedDocumentContent);
    }

    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::getEncryptedDocumentContent);
    }

    @Override
    public CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId, String documentId) {
        QueryContext<String> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .applyAsync(draftsAdaptor::getSignatureContent);
    }

    @Override
    public QueryContext<String> getSignatureContent(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::getSignatureContent);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId, byte[] content) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setContent(content)
                .applyAsync(draftsAdaptor::updateSignature);
    }

    @Override
    public QueryContext<Void> updateSignature(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::updateSignature);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> createUSN1Async(String draftId, String documentId, UsnServiceContractInfo usn) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setUsnServiceContractInfo(usn)
                .applyAsync(draftsAdaptor::createUSN1);
    }

    @Override
    public QueryContext<Void> createUSN1(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::createUSN1);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> createUSN2Async(String draftId, String documentId, UsnServiceContractInfoV2 usn) {
        QueryContext<Void> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDraftId(draftId)
                .setDocumentId(documentId)
                .setUsnServiceContractInfoV2(usn)
                .applyAsync(draftsAdaptor::createUSN2);
    }

    @Override
    public QueryContext<Void> createUSN2(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(draftsAdaptor::createUSN2);
    }

}
