/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AlexS
 */
public class CheckResultData {

	private Map<String, List<CheckError>> documentsErrors = null;
	private List<CheckError> commonErrors = null;

	public CheckResultData documentsErrors(Map<String, List<CheckError>> documentsErrors) {
		this.documentsErrors = documentsErrors;
		return this;
	}

	/**
	 * Get documentsErrors
	 *
	 * @return documentsErrors
	 *
	 */
	public Map<String, List<CheckError>> getDocumentsErrors() {
		return documentsErrors;
	}

	public void setDocumentsErrors(Map<String, List<CheckError>> documentsErrors) {
		this.documentsErrors = documentsErrors;
	}

	public CheckResultData commonErrors(List<CheckError> commonErrors) {
		this.commonErrors = commonErrors;
		return this;
	}

	public CheckResultData addCommonErrorsItem(CheckError commonErrorsItem) {
		if (this.commonErrors == null) {
			this.commonErrors = new ArrayList<>();
		}
		this.commonErrors.add(commonErrorsItem);
		return this;
	}

	/**
	 * Get commonErrors
	 *
	 * @return commonErrors
	 *
	 */
	public List<CheckError> getCommonErrors() {
		return commonErrors;
	}

	public void setCommonErrors(List<CheckError> commonErrors) {
		this.commonErrors = commonErrors;
	}
}
