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

package ru.skbkontur.sdk.extern.service.impl;

import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.service.AccountService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * @author AlexS
 */
public class AccountServiceImpl extends BaseService<AccountsAdaptor> implements AccountService {

    private static final String EN_ACC = "account";

    @Override
    public CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync() {
        QueryContext<List<Link>> cxt = createQueryContext(EN_ACC);
        return cxt.applyAsync(api::acquireBaseUri);
    }

    @Override
    public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> parent) {
        QueryContext<List<Link>> cxt = createQueryContext(parent, EN_ACC);
        return cxt.apply(api::acquireBaseUri);
    }

    @Override
    public CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync() {
        QueryContext<AccountList> cxt = createQueryContext(EN_ACC);
        return cxt.applyAsync(api::acquireAccounts);
    }

    @Override
    public QueryContext<AccountList> acquireAccounts(QueryContext<?> parent) {
        QueryContext<AccountList> cxt = createQueryContext(parent, EN_ACC);
        return cxt.apply(api::acquireAccounts);
    }

    @Override
    public CompletableFuture<QueryContext<Object>> createrAccountAsync(CreateAccountRequest createAccountRequest) {
        QueryContext<Object> cxt = createQueryContext(EN_ACC);
        return cxt
                .setCreateAccountRequest(createAccountRequest)
                .applyAsync(api::createAccount);
    }

    @Override
    public QueryContext<Object> createrAccount(QueryContext<?> parent) {
        QueryContext<Object> cxt = createQueryContext(parent, EN_ACC);
        return cxt.apply(api::createAccount);
    }

    @Override
    public CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId) {
        QueryContext<Account> cxt = createQueryContext(EN_ACC);
        return cxt
                .setAccountId(accountId)
                .applyAsync(api::getAccount);
    }

    @Override
    public QueryContext<Account> getAccount(QueryContext<?> parent) {
        QueryContext<Account> cxt = createQueryContext(parent, EN_ACC);
        return cxt.apply(api::getAccount);
    }
}
