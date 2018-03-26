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
public class AccountList {
  private Integer skip = null;

  private Integer take = null;

  private Integer totalCount = null;

  private List<Account> accounts = null;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public Integer getTake() {
		return take;
	}

	public void setTake(Integer take) {
		this.take = take;
	}
}
