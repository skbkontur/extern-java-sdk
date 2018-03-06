/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HttpsURLConnection;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentContents;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.DocumentToSend;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.invoker.DateAdapter;
import ru.skbkontur.sdk.extern.service.transport.invoker.DateTimeTypeAdapter;
import ru.skbkontur.sdk.extern.service.transport.invoker.LocalDateTypeAdapter;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.ApiKeyAuth;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.Authentication;
import ru.skbkontur.sdk.extern.model.DraftMeta;
import ru.skbkontur.sdk.extern.model.Reply;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.service.transport.invoker.DocumentToSendAdapter;
import ru.skbkontur.sdk.extern.service.transport.invoker.SignatureToSendAdapter;

/**
 *
 * @author AlexS
 *
 * Api Query Context
 * @param <R> some return type
 */
public class QueryContext<R> implements Serializable {

	private final Map<String, Object> params;

	public static final String SESSION_ID = "sessionId";
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
	public static final String DRAFT_DOCUMENT = "draftDocument";
	public static final String DOCUMENT_DESCRIPTION = "documentDescription";
	public static final String SIGNATURE_ID = "signatureId";
	public static final String SIGNATURES = "signatures";
	public static final String SIGNATURE = "signature";
	public static final String REPLY = "reply";
	public static final String REPLIES = "replies";
	public static final String NOTHING = "nothing";
	public static final String OBJECT = "object";

	private String result;

	private ServiceError serviceError;

	private ApiClient apiClient;

	private AuthenticationProvider authenticationProvider;

	private AccountProvider accountProvider;
	
	private ApiKeyProvider apiKeyProvider;
	
	public QueryContext() {
		this.params = new ConcurrentHashMap<>();
		this.apiClient = new ApiClient();
		this.serviceError = null;
		this.result = null;
	}

	public QueryContext(String entityName) {
		this();
		this.setEntityName(entityName);
	}
	
	public QueryContext(QueryContext<?> parent, String entityName) {
		this.params = new ConcurrentHashMap<>();
		this.apiClient = new ApiClient();
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

	public R get() {
		if(result==null) {
			return null;
		}
		return (R)get(result);
	}

	public boolean isSuccess() {
		return serviceError == null;
	}

	public boolean isFail() {
		return serviceError != null;
	}

	public QueryContext<R> setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
		if (serviceBaseUriProvider != null)
			this.apiClient.setBasePath(serviceBaseUriProvider.getServiceBaseUri());
		return this;
	}

	public QueryContext<R> setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
		return this;
	}

	public AuthenticationProvider getAuthenticationProvider() {
		return authenticationProvider;
	}

	public AccountProvider getAccountProvider() {
		return accountProvider;
	}

	public QueryContext<R> setAccountProvider(AccountProvider accountProvider) {
		this.accountProvider = accountProvider;
		return this;
	}

	public ApiKeyProvider getApiKeyProvider() {
		return apiKeyProvider;
	}

	public QueryContext<R> setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
		this.apiKeyProvider = apiKeyProvider;
		return this;
	}

	public String getSessionId() {
		return (String) params.get(SESSION_ID);
	}

	public QueryContext<R> setSessionId(QueryContext<?> queryContext) {
		return set(SESSION_ID, UUID.fromString(queryContext.getSessionId()));
	}

	public QueryContext<R> setSessionId(String sessionId) {
		return set(SESSION_ID, sessionId);
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

	public Docflow getDocflow() {
		return (Docflow) params.get(DOCFLOW);
	}

	public QueryContext<R> setDocflow(Docflow docflow) {
		return set(DOCFLOW, docflow);
	}

	public UUID getDocflowId() {
		return (UUID) params.get(DOCFLOW_ID);
	}

	public QueryContext<R> setDocflowId(String docflowId) {
		return set(DOCFLOW_ID, UUID.fromString(docflowId));
	}
	
	public QueryContext<R> setDocflowId(UUID docflowId) {
		return set(DOCFLOW_ID, docflowId);
	}
	
	public UUID getDocumentId() {
		return (UUID) params.get(DOCUMENT_ID);
	}

	public QueryContext<R> setDocumentId(String documentId) {
		return set(DOCUMENT_ID, UUID.fromString(documentId));
	}

	public QueryContext<R> setDocumentId(UUID documentId) {
		return set(DOCUMENT_ID, documentId);
	}

	public Document getDocument() {
		return (Document) params.get(DOCUMENT);
	}

	public QueryContext<R> setDocument(Document document) {
		return set(DOCUMENT, document);
	}
	
	public List<Document> getDocuments() {
		return (List<Document>) params.get(DOCUMENTS);
	}

	public QueryContext<R> setDocument(List<Document> documents) {
		return set(DOCUMENTS, documents);
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
		return v == null ? true : (boolean)v;
	}

	public QueryContext<R> setDeffered(boolean deffered) {
		return set(DEFFERED, deffered);
	}

	public boolean getForce() {
		Object v = params.get(FORCE);
		return v == null ? true : (boolean) v;
	}

	public QueryContext<R> setForce(boolean force) {
		return set(FORCE, force);
	}

	public byte[] getContentBytes() {
		return (byte[]) params.get(CONTENT_BYTES);
	}

	public String getContentString() {
		return (String) params.get(CONTENT_STRING);
	}

	public QueryContext<R> setContentBytes(byte[] documentContent) {
		return set(CONTENT_BYTES, documentContent);
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

	public DraftDocument getDraftDocument() {
		return (DraftDocument) params.get(DRAFT_DOCUMENT);
	}

	public QueryContext<R> setContent(byte[] content) {
		return set(CONTENT, content);
	}

	public UUID getSignatureId() {
		return (UUID) params.get(SIGNATURE_ID);
	}
	
	public QueryContext<R> setSignatureId(String signatureId) {
		return set(SIGNATURE_ID, UUID.fromString(signatureId));
	}
	
	public QueryContext<R> setSignatureId(UUID signatureId) {
		return set(SIGNATURE_ID, signatureId);
	}
	
	public Signature getSignature() {
		return (Signature) params.get(SIGNATURE);
	}
	
	public QueryContext<R> setSignatures(Signature signature) {
		return set(SIGNATURE, signature);
	}
	
	public List<Signature> getSignatures() {
		return (List<Signature>) params.get(SIGNATURES);
	}
	
	public QueryContext<R> setSignatures(List<Signature> signatures) {
		return set(SIGNATURES, signatures);
	}
	
	public Reply getReply() {
		return (Reply) params.get(REPLY);
	}
	
	public QueryContext<R> setReply(Reply reply) {
		return set(REPLY, reply);
	}
	
	public List<Object> getReplies() {
		return (List<Object>) params.get(REPLIES);
	}
	
	public QueryContext<R> setReply(List<Object> replies) {
		return set(REPLIES, replies);
	}
	
	public QueryContext<R> set(String name, Object val) {
		if (val != null) params.put(name, val);	else params.remove(name);
		
		return this;
	}

	public Object get(String name) {
		return params.get(name);
	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	private void acceptApiKey(String apiKey) {
		if (apiKey != null && !apiKey.isEmpty()) {
			Authentication apiKeyAuth = apiClient.getAuthentication("apiKey");
			if (apiKeyAuth != null) {
				((ApiKeyAuth) apiKeyAuth).setApiKey(apiKey);
			}
		}
	}

	public void configureApiClient() {
		apiClient.getJSON().setGson(new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(Date.class, new DateAdapter(apiClient))
			.registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
			.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
			.registerTypeAdapter(ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend.class, new SignatureToSendAdapter())
			.registerTypeAdapter(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend.class, new DocumentToSendAdapter())
			.create());
		// устанавливаем api-key
		acceptApiKey(apiKeyProvider.getApiKey());
	}

	public CompletableFuture<QueryContext<R>> applyAsync(Query query) {
		QueryContext<R> queryContext = acquireSessionId();
		if (queryContext.isSuccess()) {
			return CompletableFuture.supplyAsync(()->query.apply(this));
		}
		else
			return CompletableFuture.completedFuture(this);
	}

	public QueryContext<R> apply(Query query) {
		QueryContext<R> queryContext = acquireSessionId();
		if (queryContext.isSuccess())
			return query.apply(this);
		else
			return this;
	}
	
	public ServiceException failure() {
		throw new ServiceException(getServiceError());
	}
	
	private QueryContext<R> acquireSessionId() {
		String sessionId = getSessionId();
		if (sessionId != null && !sessionId.isEmpty()) {
			return this;
		}
		else {
			if (authenticationProvider != null) {
				QueryContext<String> authQuery = authenticationProvider.sessionId();
				if (authQuery.isFail()) {
					return setServiceError(authQuery);
				}
				else {
					return setSessionId(authQuery.get());
				}
			}
			else {
				return this.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.unknownAuth));
			}
		}
	}
}
