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
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentMeta;
import ru.skbkontur.sdk.extern.rest.swagger.model.Link;
import ru.skbkontur.sdk.extern.rest.swagger.model.Reply;

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
		return ctx.setResult(api.docflowsGetDocflow(ctx.getAccountId(), ctx.getDocflowId()));
	}

	public QueryContext<List<Document>> getDocuments(QueryContext<List<Document>> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocuments(ctx.getAccountId(), ctx.getDocflowId()));
	}

	public QueryContext<Document> getDocument(QueryContext<Document> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocument(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<DocumentMeta> getDocumentMeta(QueryContext<DocumentMeta> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentMeta(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<List<Link>> getSignatures(QueryContext<List<Link>> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetDocumentSignature(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentId()));
	}

	public QueryContext<Reply> getReplyDocument(QueryContext<Reply> ctx) throws ApiException {
		return ctx.setResult(api.docflowsGetReplyDocument(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentType(), ctx.getReplyId()));
	}
	
	public QueryContext<Docflow> sendReplyDocument(QueryContext<Docflow> ctx) throws ApiException {
		return ctx.setResult(api.docflowsSendReplyDocument(ctx.getAccountId(), ctx.getDocflowId(), ctx.getDocumentType(), ctx.getReplyId(), ctx.getDocumentToSend()));
	}

}
