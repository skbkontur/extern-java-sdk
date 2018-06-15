/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HttpsURLConnection;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfoV2;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.ServiceError.ErrorCode;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.provider.UriProvider;

/**
 * {@code QueryContext} класс предоставляет контекст функционального интерфейса
 *
 * @author Сухоруков А., St.Petersburg 25/04/2018
 * @param <R>
 * @since 1.2
 */
public class QueryContext<R> implements Serializable {

    private static final Map<String, List<String>> EMPTY_MAP = new HashMap<String, List<String>>();

    private static final long serialVersionUID = -2919303896965835578L;

    public static final String SESSION_ID = "sessionId";
    public static final String AUTH_PREFIX = "authPrefix";
    public static final String ENTITY_NAME = "entityName";
    public static final String ENTITY_ID = "entityId";
    public static final String DRAFT_ID = "draftId";
    public static final String DRAFT = "draft";
    public static final String DOCUMENT_ID = "documentId";
    public static final String DOCUMENT = "document";
    public static final String DOCUMENTS = "documents";
    public static final String DOCFLOW_ID = "docflowId";
    public static final String DOCUMENT_TYPE = "documentType";
    public static final String REPLY_ID = "replyId";
    public static final String DOCUMENT_TO_SEND = "documentToSend";
    public static final String DOCUMENT_TO_SENDS = "documentToSends";
    public static final String DRAFT_META = "draftMeta";
    public static final String DEFFERED = "deffered";
    public static final String FORCE = "force";
    public static final String CONTENT_BYTES = "contentBytes";
    public static final String CONTENT_STRING = "contentString";
    public static final String FILE_NAME = "fileName";
    public static final String DOCUMENT_CONTENTS = "documentContents";
    public static final String CONTENT = "content";
    public static final String MAP = "map";
    public static final String PREPARE_RESULT = "prepareResult";
    public static final String DOCFLOW = "docflow";
    public static final String DOCFLOWS = "docflows";
    public static final String DRAFT_DOCUMENT = "draftDocument";
    public static final String DOCUMENT_DESCRIPTION = "documentDescription";
    public static final String SIGNATURE_ID = "signatureId";
    public static final String SIGNATURES = "signatures";
    public static final String SIGNATURE = "signature";
    public static final String THUMBPRINT = "thumbprint";
    public static final String LINKS = "links";
    public static final String ACCOUNT_ID = "accountId";
    public static final String ACCOUNT = "account";
    public static final String ACCOUNT_LIST = "accountList";
    public static final String DOCFLOW_PAGE = "docflowPage";
    public static final String CREATE_ACCOUNT_REQUEST = "createAccountRequest";
    public static final String FINISHED = "finished";
    public static final String INCOMING = "incoming";
    public static final String SKIP = "skip";
    public static final String TAKE = "take";
    public static final String INN_KPP = "innKpp";
    public static final String INN = "inn";
    public static final String KPP = "kpp";
    public static final String UPDATED_FROM = "updatedFrom";
    public static final String UPDATED_TO = "updatedTo";
    public static final String CREATED_FROM = "createdFrom";
    public static final String CREATED_TO = "createdTo";
    public static final String TYPE = "type";
    public static final String CERTIFICATE = "certificate";
    public static final String CERTIFICATE_LIST = "certificateList";
    public static final String USN_SERVICE_CONTRACT_INFO = "usnServiceContractInfo";
    public static final String USN_SERVICE_CONTRACT_INFO_V2 = "usnServiceContractInfoV2";
    public static final String FROM_ID = "fromId";
    public static final String SIZE = "size";
    public static final String EVENTS_PAGE = "eventsPage";
    public static final String NOTHING = "nothing";
    public static final String OBJECT = "object";
    public static final String VERSION = "version";
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY = "company";
    public static final String COMPANY_GENERAL = "companyGeneral";
    public static final String NAME = "name";
    public static final String COMPANY_BATCH = "companyBatch";

    private final Map<String, Object> params;

    private String result;

    private ServiceError serviceError;

    private UriProvider serviceBaseUriProvider;

    private AuthenticationProvider authenticationProvider;

    private AccountProvider accountProvider;

    private ApiKeyProvider apiKeyProvider;

    public QueryContext() {
        this.params = new ConcurrentHashMap<>();
        this.serviceError = null;
        this.result = null;
    }

    public QueryContext(String entityName) {
        this();
        this.setEntityName(entityName);
    }

    public QueryContext(QueryContext<?> parent, String entityName) {
        this.params = new ConcurrentHashMap<>();
        this.params.putAll(parent.params);
        this.serviceError = parent.getServiceError();
        this.result = null;
        this.setEntityName(entityName);
    }

    public QueryContext<R> setResult(R result, String key) {
        this.result = key;
        this.serviceError = null;
        return key.equals(NOTHING) ? this : set(key, result);
    }

    public ServiceError getServiceError() {
        return serviceError;
    }

    public QueryContext<R> setServiceError(String message) {
        return setServiceError(ErrorCode.business, message, 0, EMPTY_MAP, null, null);
    }

    public QueryContext<R> setServiceError(QueryContext<?> queryContext) {
        this.result = null;
        this.serviceError = queryContext.serviceError;
        if (serviceError.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            set(SESSION_ID, null);
        }
        return this;
    }

    public QueryContext<R> setServiceError(ServiceError serviceError) {
        this.result = null;
        this.serviceError = serviceError;
        if (serviceError.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            set(SESSION_ID, null);
        }
        return this;
    }

    public QueryContext<R> setServiceError(ServiceError.ErrorCode errorCode) {
        return setServiceError(errorCode, errorCode.message(), 0, EMPTY_MAP, null, null);
    }

    public QueryContext<R> setServiceError(ApiException x) {
        return setServiceError(ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody(), x.getCause());
    }

    public QueryContext<R> setServiceError(ServiceError.ErrorCode errorCode, String message, int code, Map<String, List<String>> responseHeaders, String responseBody, Throwable thrown) {
        return setServiceError(
            new ServiceError() {
            @Override
            public ErrorCode getErrorCode() {
                return errorCode;
            }

            @Override
            public int getResponseCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public Map<String, List<String>> getResponseHeaders() {
                return responseHeaders;
            }

            @Override
            public String getResponseBody() {
                return responseBody;
            }

            @Override
            public Throwable getCause() {
                return thrown;
            }

            @Override
            public String toString() {
                return prettyErrorPrint(this);
            }
        }
        );
    }

    public QueryContext<R> setServiceError(String message, Throwable x) {
        return setServiceError(ErrorCode.business, message, 0, EMPTY_MAP, null, x);
    }

    public R get() {
        if (result == null) {
            return null;
        }
        return get(result);
    }

    public boolean isSuccess() {
        return serviceError == null;
    }

    public boolean isFail() {
        return serviceError != null;
    }

    public UriProvider getServiceBaseUriProvider() {
        return serviceBaseUriProvider == null ? () -> "" : serviceBaseUriProvider;
    }

    public QueryContext<R> setServiceBaseUriProvider(@NotNull UriProvider serviceBaseUriProvider) {
        this.serviceBaseUriProvider = serviceBaseUriProvider;
        return this;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public QueryContext<R> setAuthenticationProvider(@NotNull AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        return this;
    }

    public AccountProvider getAccountProvider() {
        return accountProvider;
    }

    public QueryContext<R> setAccountProvider(@NotNull AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
        return this;
    }

    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider == null ? () -> "" : apiKeyProvider;
    }

    public QueryContext<R> setApiKeyProvider(@NotNull ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
        return this;
    }

    public String getSessionId() {
        return (String) params.get(SESSION_ID);
    }

    public QueryContext<R> setSessionId(String sessionId) {
        return set(SESSION_ID, sessionId);
    }

    public QueryContext<R> setSessionId(QueryContext<?> queryContext) {
        return set(SESSION_ID, UUID.fromString(queryContext.getSessionId()));
    }

    public String getAuthPrefix() {
        return (String) params.get(AUTH_PREFIX);
    }

    public QueryContext<R> setAuthPrefix(String authPrefix) {
        return set(AUTH_PREFIX, authPrefix);
    }

    public String getEntityName() {
        return (String) params.get(ENTITY_NAME);
    }

    public final QueryContext<R> setEntityName(String entityName) {
        return set(ENTITY_NAME, entityName);
    }

    public UUID getDraftId() {
        return (UUID) params.get(DRAFT_ID);
    }

    public QueryContext<R> setDraftId(String draftId) {
        return set(DRAFT_ID, UUID.fromString(draftId));
    }

    public QueryContext<R> setDraftId(UUID draftId) {
        return set(DRAFT_ID, draftId);
    }

    public Draft getDraft() {
        return (Draft) params.get(DRAFT);
    }

    public QueryContext<R> setDraft(Draft draft) {
        return set(DRAFT, draft);
    }

    public Docflow getDocflow() {
        return (Docflow) params.get(DOCFLOW);
    }

    public QueryContext<R> setDocflow(List<Docflow> docflows) {
        return set(DOCFLOWS, docflows);
    }

    public QueryContext<R> setDocflow(Docflow docflow) {
        return set(DOCFLOW, docflow);
    }

    @SuppressWarnings("unchecked")
    public List<Docflow> getDocflows() {
        return (List<Docflow>) params.get(DOCFLOWS);
    }

    public UUID getDocflowId() {
        UUID docflowId = (UUID) params.get(DOCFLOW_ID);
        if (docflowId == null) {
            Docflow docflow = this.getDocflow();
            if (docflow != null) {
                docflowId = docflow.getId();
            }
        }
        return docflowId;
    }

    public QueryContext<R> setDocflowId(UUID docflowId) {
        return set(DOCFLOW_ID, docflowId);
    }

    public QueryContext<R> setDocflowId(String docflowId) {
        return set(DOCFLOW_ID, UUID.fromString(docflowId));
    }

    public UUID getDocumentId() {
        return (UUID) params.get(DOCUMENT_ID);
    }

    public QueryContext<R> setDocumentId(UUID documentId) {
        return set(DOCUMENT_ID, documentId);
    }

    public QueryContext<R> setDocumentId(String documentId) {
        return set(DOCUMENT_ID, UUID.fromString(documentId));
    }

    public Document getDocument() {
        return (Document) params.get(DOCUMENT);
    }

    public QueryContext<R> setDocument(List<Document> documents) {
        return set(DOCUMENTS, documents);
    }

    public QueryContext<R> setDocument(Document document) {
        return set(DOCUMENT, document);
    }

    @SuppressWarnings("unchecked")
    public List<Document> getDocuments() {
        return (List<Document>) params.get(DOCUMENTS);
    }

    public DocumentDescription getDocumentDescription() {
        return (DocumentDescription) params.get(DOCUMENT_DESCRIPTION);
    }

    public QueryContext<R> setDocumentDescription(DocumentDescription documentDescription) {
        return set(DOCUMENT_DESCRIPTION, documentDescription);
    }

    public String getDocumentType() {
        return (String) params.get(DOCUMENT_TYPE);
    }

    public QueryContext<R> setDocumentType(String documentType) {
        return set(DOCUMENT_TYPE, documentType);
    }

    public UUID getReplyId() {
        return (UUID) params.get(REPLY_ID);
    }

    public QueryContext<R> setReplyId(String replyId) {
        return set(REPLY_ID, UUID.fromString(replyId));
    }

    public DocumentToSend getDocumentToSend() {
        return (DocumentToSend) params.get(DOCUMENT_TO_SEND);
    }

    public QueryContext<R> setDocumentToSend(DocumentToSend documentToSend) {
        return set(DOCUMENT_TO_SEND, documentToSend);
    }

    public DraftMeta getDraftMeta() {
        return (DraftMeta) params.get(DRAFT_META);
    }

    public QueryContext<R> setDraftMeta(DraftMeta draftMeta) {
        return set(DRAFT_META, draftMeta);
    }

    public boolean getDeffered() {
        Object v = params.get(DEFFERED);
        return v == null || (boolean) v;
    }

    public QueryContext<R> setDeffered(boolean deffered) {
        return set(DEFFERED, deffered);
    }

    public boolean getForce() {
        Object v = params.get(FORCE);
        return v == null || (boolean) v;
    }

    public QueryContext<R> setForce(boolean force) {
        return set(FORCE, force);
    }

    public byte[] getContentBytes() {
        return (byte[]) params.get(CONTENT_BYTES);
    }

    public QueryContext<R> setContentBytes(byte[] documentContent) {
        return set(CONTENT_BYTES, documentContent);
    }

    public String getContentString() {
        return (String) params.get(CONTENT_STRING);
    }

    public QueryContext<R> setContentString(String documentContent) {
        return set(CONTENT_STRING, documentContent);
    }

    public String getFileName() {
        return (String) params.get(FILE_NAME);
    }

    public QueryContext<R> setFileName(String fileName) {
        return set(FILE_NAME, fileName);
    }

    public DocumentContents getDocumentContents() {
        return (DocumentContents) params.get(DOCUMENT_CONTENTS);
    }

    public QueryContext<R> setDocumentContents(DocumentContents documentContents) {
        return set(DOCUMENT_CONTENTS, documentContents);
    }

    public byte[] getContent() {
        return (byte[]) params.get(CONTENT);
    }

    public QueryContext<R> setContent(byte[] content) {
        return set(CONTENT, content);
    }

    public DraftDocument getDraftDocument() {
        return (DraftDocument) params.get(DRAFT_DOCUMENT);
    }

    public UUID getSignatureId() {
        return (UUID) params.get(SIGNATURE_ID);
    }

    public QueryContext<R> setSignatureId(UUID signatureId) {
        return set(SIGNATURE_ID, signatureId);
    }

    public QueryContext<R> setSignatureId(String signatureId) {
        return set(SIGNATURE_ID, UUID.fromString(signatureId));
    }

    public String getThumbprint() {
        return (String) params.get(THUMBPRINT);
    }

    public QueryContext<R> setThumbprint(String thumbprint) {
        return set(THUMBPRINT, thumbprint);
    }

    @SuppressWarnings("unchecked")
    public List<Link> getLinks() {
        return (List<Link>) params.get(LINKS);
    }

    public QueryContext<R> setLinks(List<Link> links) {
        return set(LINKS, links);
    }

    public UUID getAccountId() {
        return (UUID) params.get(ACCOUNT_ID);
    }

    public QueryContext<R> setAccountId(String accountId) {
        return set(ACCOUNT_ID, UUID.fromString(accountId));
    }

    public QueryContext<R> setAccountId(UUID accountId) {
        return set(ACCOUNT_ID, accountId);
    }

    public Account getAccount() {
        return (Account) params.get(ACCOUNT);
    }

    public QueryContext<R> setAccount(Account account) {
        return set(ACCOUNT, account);
    }

    public AccountList getAccountList() {
        return (AccountList) params.get(ACCOUNT_LIST);
    }

    public QueryContext<R> setAccountList(AccountList accountList) {
        return set(ACCOUNT_LIST, accountList);
    }

    public CreateAccountRequest getCreateAccountRequest() {
        return (CreateAccountRequest) params.get(CREATE_ACCOUNT_REQUEST);
    }

    public QueryContext<R> setCreateAccountRequest(CreateAccountRequest createAccountRequest) {
        return set(CREATE_ACCOUNT_REQUEST, createAccountRequest);
    }

    public Signature getSignature() {
        return (Signature) params.get(SIGNATURE);
    }

    public QueryContext<R> setSignature(Signature signature) {
        return set(SIGNATURE, signature);
    }

    @SuppressWarnings("unchecked")
    public List<Signature> getSignatures() {
        return (List<Signature>) params.get(SIGNATURES);
    }

    public QueryContext<R> setSignatures(List<Signature> signatures) {
        return set(SIGNATURES, signatures);
    }

    @SuppressWarnings("unchecked")
    public List<DocumentToSend> getDocumentToSends() {
        return (List<DocumentToSend>) params.get(DOCUMENT_TO_SENDS);
    }

    public QueryContext<R> setDocumentToSends(List<DocumentToSend> replies) {
        return set(DOCUMENT_TO_SENDS, replies);
    }

    public boolean getFinished() {
        return (boolean) params.get(FINISHED);
    }

    public QueryContext<R> setFinished(boolean finished) {
        return set(FINISHED, finished);
    }

    public boolean getIncoming() {
        return (boolean) params.get(INCOMING);
    }

    public QueryContext<R> setIncoming(boolean incoming) {
        return set(INCOMING, incoming);
    }

    public Long getSkip() {
        return (Long) params.get(SKIP);
    }

    public QueryContext<R> setSkip(Long skip) {
        return set(SKIP, skip);
    }

    public Integer getTake() {
        return (Integer) params.get(TAKE);
    }

    public QueryContext<R> setTake(Integer take) {
        return set(TAKE, take);
    }


    public String getInn() {
        return (String) params.get(INN);
    }

    public QueryContext<R> setInn(String inn) {
        return set(INN, inn);
    }

    public String getKpp() {
        return (String) params.get(KPP);
    }

    public QueryContext<R> setKpp(String kpp) {
        return set(KPP, kpp);
    }

    public String getInnKpp() {
        return (String) params.get(INN_KPP);
    }

    public QueryContext<R> setInnKpp(String innKpp) {
        return set(INN_KPP, innKpp);
    }

    public Date getUpdatedFrom() {
        return (Date) params.get(UPDATED_FROM);
    }

    public QueryContext<R> setUpdatedFrom(Date updatedFrom) {
        return set(UPDATED_FROM, updatedFrom);
    }

    public Date getUpdatedTo() {
        return (Date) params.get(UPDATED_TO);
    }

    public QueryContext<R> setUpdatedTo(Date updatedTo) {
        return set(UPDATED_TO, updatedTo);
    }

    public Date getCreatedFrom() {
        return (Date) params.get(CREATED_FROM);
    }

    public QueryContext<R> setCreatedFrom(Date createdFrom) {
        return set(CREATED_FROM, createdFrom);
    }

    public Date getCreatedTo() {
        return (Date) params.get(CREATED_TO);
    }

    public QueryContext<R> setCreatedTo(Date createdTo) {
        return set(CREATED_TO, createdTo);
    }

    public String getType() {
        return (String) params.get(TYPE);
    }

    public QueryContext<R> setType(String type) {
        return set(TYPE, type);
    }

    public String getCertificate() {
        return (String) params.get(CERTIFICATE);
    }

    public QueryContext<R> setCertificate(String certificate) {
        return set(CERTIFICATE, certificate);
    }

    public CertificateList getCertificateList() {
        return (CertificateList) params.get(CERTIFICATE_LIST);
    }

    public QueryContext<R> setCertificateList(CertificateList certificateList) {
        return set(CERTIFICATE_LIST, certificateList);
    }

    public UsnServiceContractInfo getUsnServiceContractInfo() {
        return (UsnServiceContractInfo) params.get(USN_SERVICE_CONTRACT_INFO);
    }

    public QueryContext<R> setUsnServiceContractInfo(UsnServiceContractInfo usnServiceContractInfo) {
        return set(USN_SERVICE_CONTRACT_INFO, usnServiceContractInfo);
    }

    public UsnServiceContractInfoV2 getUsnServiceContractInfoV2() {
        return (UsnServiceContractInfoV2) params.get(USN_SERVICE_CONTRACT_INFO_V2);
    }

    public QueryContext<R> setUsnServiceContractInfoV2(UsnServiceContractInfoV2 usnServiceContractInfoV2) {
        return set(USN_SERVICE_CONTRACT_INFO_V2, usnServiceContractInfoV2);
    }

    public String getFromId() {
        return (String) params.get(FROM_ID);
    }

    public QueryContext<R> setFromId(String fromId) {
        return set(FROM_ID, fromId);
    }

    public UUID getCompanyId() {
        return (UUID) params.get(COMPANY_ID);
    }

    public QueryContext<R> setCompanyId(String companyId) {
        return set(COMPANY_ID, UUID.fromString(companyId));
    }

    public QueryContext<R> setCompanyId(UUID companyId) {
        return set(COMPANY_ID, companyId);
    }

    public Company getCompany() {
        return (Company) params.get(COMPANY);
    }

    public QueryContext<R> setCompany(Company company) {
        return set(COMPANY, company);
    }

    public CompanyGeneral getCompanyGeneral() {
        return (CompanyGeneral) params.get(COMPANY_GENERAL);
    }

    public QueryContext<R> setCompanyGeneral(CompanyGeneral companyGeneral) {
        return set(COMPANY_GENERAL, companyGeneral);
    }

    public String getName() {
        return (String) params.get(NAME);
    }

    public QueryContext<R> setName(String name) {
        return set(NAME, name);
    }

    public int getSize() {
        return (int) params.get(SIZE);
    }

    public QueryContext<R> setSize(int size) {
        return set(SIZE, size);
    }

    public EventsPage getEventsPage() {
        return (EventsPage) params.get(EVENTS_PAGE);
    }

    public QueryContext<R> setEventsPageSize(EventsPage eventsPage) {
        return set(EVENTS_PAGE, eventsPage);
    }

    public int getVersion() {
        return (int) params.get(VERSION);
    }

    public QueryContext<R> setVersion(int version) {
        return set(VERSION, version);
    }

    public QueryContext<R> set(String name, Object val) {
        if (val != null) {
            params.put(name, val);
        }
        else {
            params.remove(name);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) params.get(name);
    }

    public CompletableFuture<QueryContext<R>> applyAsync(Query<R> query) {
        if (isFail()) {
            return CompletableFuture.completedFuture(this);
        }

        return CompletableFuture.supplyAsync(() -> query.apply(this));
    }

    public QueryContext<R> apply(Query<R> query) {
        if (isFail()) {
            return this;
        }

        QueryContext<R> r = query.apply(this);
        if (r.isFail() && r.getServiceError().getErrorCode() == ErrorCode.server && r.getServiceError().getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            if (authenticationProvider != null) {
                authenticationProvider.raiseUnauthenticated(r.getServiceError());
            }
        }
        return r;
    }

    public ServiceException failure() {
        throw new ServiceException(getServiceError());
    }

    private String prettyErrorPrint(ServiceError se) {
        final String EOL = "\r\n";

        ErrorCode errorCode = se.getErrorCode();
        String message = se.getMessage();
        int responseCode = se.getResponseCode();
        Map<String, List<String>> responseHeaders = se.getResponseHeaders();
        String responseBody = se.getResponseBody();

        StringBuilder errorMsg = new StringBuilder("Message error: ").append(message == null ? errorCode.message() : message).append(EOL);
        if (responseCode != 0) {
            errorMsg.append("  Response code: ").append(responseCode).append(EOL);
            if (responseHeaders != null) {
                errorMsg.append("  Headers:").append(EOL);
                responseHeaders.keySet().forEach((k) -> {
                    List<String> values = responseHeaders.get(k);
                    if (values != null) {
                        StringBuilder headerLine = new StringBuilder("    ").append(k).append(": ");
                        values.forEach((v) -> {
                            headerLine.append(v).append("; ");
                        });
                        errorMsg.append(headerLine).append(EOL);
                    }
                });
            }
            if (responseBody != null) {
                String cleanText = responseBody.replaceAll("\n", " ").replaceAll("\r", "");
                errorMsg.append("  Response body: ").append(cleanText).append(EOL);
            }
        }
        return errorMsg.toString();
    }
}
