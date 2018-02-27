/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class Draft {
	
  public enum Status {
    NEW("new"),
    
    CHECKED("checked"),
    
    READYTOSEND("readyToSend"),
    
    SENT("sent");

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
	
  private UUID       id;
	private Status status;
	
	public Draft() {
	}
	
	public Draft(UUID id, Status status) {
		this.id = id;
		this.status = status;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public void setStatus(String status) {
		this.status = Status.fromValue(status);
	}
}
