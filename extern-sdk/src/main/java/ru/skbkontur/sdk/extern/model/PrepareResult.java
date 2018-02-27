/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;

/**
 *
 * @author AlexS
 */
public class PrepareResult {
	
  public enum Status {
    CHECKPROTOCOLHASERRORS("checkProtocolHasErrors"),
    
    CHECKPROTOCOLHASONLYWARNINGS("checkProtocolHasOnlyWarnings"),
    
    ENCRYPTIONFAILED("encryptionFailed"),
    
    OK("ok");

    private final String value;

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static Status fromValue(String text) {
      for (Status b : Status.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
	}
		
  private CheckResultData checkResult = null;
  private List<Link> links = null;
  private Status status = null;

	public CheckResultData getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(CheckResultData checkResult) {
		this.checkResult = checkResult;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public Status getStatus() {
		return status;
	}
	
  public void setStatus(Status status) {
    this.status = status;
  }

}
