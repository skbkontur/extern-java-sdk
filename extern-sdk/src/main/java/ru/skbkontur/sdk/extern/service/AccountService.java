/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @author AlexS
 */
@SuppressWarnings("unused")
public interface AccountService {
    CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync();

    QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt);

    CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync();

    QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Object>> createAccountAsync(CreateAccountRequest createAccountRequest);

    QueryContext<Object> createAccount(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId);

    QueryContext<Account> getAccount(QueryContext<?> cxt);
}
