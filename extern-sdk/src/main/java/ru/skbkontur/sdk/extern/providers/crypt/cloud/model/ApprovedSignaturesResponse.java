/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class ApprovedSignaturesResponse {
	@SerializedName("Signatures")
	private List<Entry> signatures;

	public List<Entry> getSignatures() {
		return signatures;
	}

	public void setSignatures(List<Entry> signatures) {
		this.signatures = signatures;
	}
}
