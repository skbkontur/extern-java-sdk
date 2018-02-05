/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import java.util.List;
import ru.skbkontur.sdk.extern.rest.swagger.api.DocflowsApi;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiClient;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.Document;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentDescription;
import ru.skbkontur.sdk.extern.rest.swagger.model.Reply;
import ru.skbkontur.sdk.extern.rest.swagger.model.Signature;

/**
 *
 * @author AlexS
 */
public class DocflowsApiWrap extends ApiWrap {
	private final DocflowsApi api;
	
	public DocflowsApiWrap(DocflowsApi api) {
		this.api = api;
	}

	public ApiClient getApiClient() {
		return api.getApiClient();
	}
	
	public QueryContext<Docflow> getDocflow(QueryContext<Docflow> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocflowAsync(ctx.getAccountId(), ctx.getDocflowId()));
	}

	public QueryContext<List<Document>> getDocuments(QueryContext<List<Document>> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentsAsync(ctx.getAccountId(), ctx.getDocflowId()));
	}

	public QueryContext<Document> getDocument(QueryContext<Document> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentAsync(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<DocumentDescription> getDocumentDescription(QueryContext<DocumentDescription> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentDescriptionAsync(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<List<Signature>> getSignatures(QueryContext<List<Signature>> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentSignaturesAsync(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<Reply> getReplyDocument(QueryContext<Reply> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetReplyDocumentAsync(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentType(), ctx.getReplyId()));
	}
	
	public QueryContext<Docflow> sendReplyDocument(QueryContext<Docflow> ctx) throws ApiException {
		return ctx.setResult(api.docflowsSendReplyDocumentAsync(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentType(), ctx.getReplyId(), ctx.getDocumentToSend()));
	}

}
