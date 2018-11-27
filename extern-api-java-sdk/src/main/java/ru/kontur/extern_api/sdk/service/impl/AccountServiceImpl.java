/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;
import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.join;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.AccountsApi;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.service.AccountService;


public class AccountServiceImpl implements AccountService {

    private final AccountsApi api;

    public AccountServiceImpl(AccountsApi api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync() {
        return api.getAll()
                .thenApply(contextAdaptor(QueryContext.ACCOUNT_LIST));
    }

    @Override
    @Deprecated
    public QueryContext<AccountList> acquireAccounts(QueryContext<?> parent) {
        return join(acquireAccountsAsync());
    }

    @Override
    public CompletableFuture<QueryContext<Account>> createAccountAsync(CreateAccountRequest createAccountRequest) {
        return api.create(createAccountRequest)
                .thenApply(contextAdaptor(QueryContext.ACCOUNT));
    }

    @Override
    @Deprecated
    public QueryContext<Account> createAccount(QueryContext<?> parent) {
        return join(createAccountAsync(parent.require(QueryContext.CREATE_ACCOUNT_REQUEST)));
    }

    @Override
    public CompletableFuture<QueryContext<Account>> getAccountAsync(UUID accountId) {
        return api.get(accountId)
                .thenApply(contextAdaptor(QueryContext.ACCOUNT));
    }

    @Override
    public CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId) {
        return getAccountAsync(UUID.fromString(accountId));
    }

    @Override
    @Deprecated
    public QueryContext<Account> getAccount(QueryContext<?> parent) {
        return join(getAccountAsync(parent.<UUID>require(QueryContext.ACCOUNT_ID)));
    }
}
