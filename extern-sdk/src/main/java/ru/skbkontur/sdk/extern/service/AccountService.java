/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;


/**
 *
 * @author AlexS
 */
public interface AccountService {

	public CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync();
	public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt);


	public CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync();
	public QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<Object>> createrAccountAsync(CreateAccountRequest createAccountRequest);
	public QueryContext<Object> createrAccount(QueryContext<?> cxt);

	public CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId);
	public QueryContext<Account> getAccount(QueryContext<?> cxt);
}
