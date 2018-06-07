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

package ru.kontur.extern_api.sdk.service;

import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Link;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.provider.Providers;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * @author AlexS
 */
public interface AccountService extends Providers {

    CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync();

    QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt);

    CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync();

    QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Account>> createAccountAsync(CreateAccountRequest createAccountRequest);

    QueryContext<Account> createAccount(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId);

    QueryContext<Account> getAccount(QueryContext<?> cxt);
}
