/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.util.Map;
import java.util.UUID;
import ru.skbkontur.sdk.extern.rest.swagger.api.DraftsApi;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class ExternSDKDraft extends ExternSDKBase {

	private final DraftsApi api;

	public ExternSDKDraft(ExternSDK externSDK) {
		super(externSDK);
		this.api = new DraftsApi();
		this.configureApiClient(api.getApiClient());
	}

	/**
	 * Create new a draft
	 *
	 * POST /v1/{billingAccountId}/drafts
	 *
	 * @param clientInfo DraftMeta
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String, Object> createDraft(DraftMeta clientInfo) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.draftsCreate(getBillingAccountId(), clientInfo, getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, 0);
			}
		}
		// never too be
		return null;
	}

	/**
	 * lookup a draft by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String, Object> getDraft(String draftId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.draftsGetDraft(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, 0);
			}
		}
		// never too be
		return null;
	}

	/**
	 * Delete a draft
	 *
	 * DELETE /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * @throws ExternSDKException a business error
	 */
	public void deleteDraft(String draftId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				api.draftsDeleteDraft(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey());
				break;
			}
			catch (ApiException x) {
				errorHandler(x, i, "Черновик", draftId);
			}
		}
	}

	/**
	 * lookup a draft meta by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftMeta getDraftMeta(String draftId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return jsonToDTO((Map) api.draftsGetMeta(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey()), DraftMeta.class);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Дополнение к черновику", draftId);
			}
		}
		// never too be
		return null;
	}

	/**
	 * update a draft meta
	 *
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param clientInfo DraftMeta
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftMeta updateDraftMeta(String draftId, DraftMeta clientInfo) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return jsonToDTO((Map) api.draftsUpdateDraftMeta(getBillingAccountId(), UUID.fromString(draftId), clientInfo, getAccessToken(), getApiKey()), DraftMeta.class);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Дополнение к черновику", draftId);
			}
		}
		// never too be
		return null;
	}

	/**
	 * Operate CHECK
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/check
	 * 
	 * @param draftId String draft identifier
	 * @param deffered boolean
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String,Object> check(String draftId, boolean deffered) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.draftsCheck(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey(), deffered);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Черновик", draftId);
			}
		}
		// never too be
		return null;
	}
	

	/**
	 * Operate PREPARE
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/prepare
	 * 
	 * @param draftId String draft identifier
	 * @param deffered boolean
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String,Object> prepare(String draftId, boolean deffered) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.draftsPrepare(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey(), deffered);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Черновик", draftId);
			}
		}
		// never too be
		return null;
	}
	
	/**
	 * Send the draft
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/send
	 * 
	 * @param draftId a draft identifier
	 * @throws ExternSDKException a business error
	 */
	public void send(String draftId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				api.draftsSend(getBillingAccountId(), UUID.fromString(draftId), getAccessToken(), getApiKey(), true, true);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Черновик", draftId);
			}
		}
	}
}
