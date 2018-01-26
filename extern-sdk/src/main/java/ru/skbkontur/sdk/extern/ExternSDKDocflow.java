/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.util.ArrayList;
import java.util.List;
import ru.skbkontur.sdk.extern.rest.api.DocflowsApiWrap;
import ru.skbkontur.sdk.extern.rest.api.QueryContext;
import ru.skbkontur.sdk.extern.rest.swagger.api.DocflowsApi;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.Document;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentMeta;
import ru.skbkontur.sdk.extern.rest.swagger.model.Link;
import ru.skbkontur.sdk.extern.rest.swagger.model.Reply;

/**
 *
 * @author AlexS
 */
public class ExternSDKDocflow extends ExternSDKBase {
	private static final String EN_DF = "Документооборот";
	private static final String EN_DOC = "Документ";
	
	private final DocflowsApiWrap api;

	public ExternSDKDocflow(ExternSDK externSDK) {
		super(externSDK);
		this.api = new DocflowsApiWrap(new DocflowsApi());
		this.setApiClient(api.getApiClient());
		this.configureApiClient();
	}

	/**
	 * lookup a docflow instance by the docflow identifier
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}
	 *
	 * @param docflowId String a docflow identifier
	 * @return Docflow an instance
	 * @throws ExternSDKException a business error
	 */
	public Docflow getDocflow(String docflowId) throws ExternSDKException {
		return invokeApply(api::getDocflow,newCxt(EN_DF,Docflow.class).setDocflowId(docflowId).setEntityId(docflowId)).get();
	}
	
	/**
	 * returns a document list of the docflow
	 * @param docflowId String a docflow identifier
	 * @return List&lt;ocument&gt; a document list
	 * @throws ExternSDKException a business error
	 */
	public List<Document> getDocuments(String docflowId) throws ExternSDKException {
		return invokeApply(api::getDocuments,newCxtForList(EN_DF, Document.class)).get();
	}
	
	public Document getDocument(String docflowId, String documentId) throws ExternSDKException {
		return invokeApply(api::getDocument,newCxt(EN_DOC,Document.class).setDocflowId(docflowId).setDocumentId(documentId).setEntityId(docflowId)).get();
	}
	
	public DocumentMeta getDocumentMeta(String docflowId, String documentId) throws ExternSDKException {
		return invokeApply(api::getDocumentMeta,newCxt(EN_DOC, DocumentMeta.class).setDocflowId(docflowId).setDocumentId(documentId).setEntityId(docflowId)).get();
	}

	public List<Link> getSignatures(String docflowId, String documentId) throws ExternSDKException {
		return invokeApply(api::getSignatures,newCxtForList(EN_DOC, Link.class).setDocflowId(docflowId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	public Reply getReplyDocument(String docflowId, String documentId) throws ExternSDKException {
		return invokeApply(api::getReplyDocument,newCxt(EN_DOC, Reply.class).setDocflowId(docflowId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	public Docflow sendReplyDocument(String docflowId, String documentId) throws ExternSDKException {
		return invokeApply(api::sendReplyDocument,newCxt(EN_DOC, Docflow.class).setDocflowId(docflowId).setDocumentId(documentId).setEntityId(documentId)).get();
	}
}
