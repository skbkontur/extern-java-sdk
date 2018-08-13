/*
 * The MIT License
 *
 * Copyright 2018 alexs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import java.util.List;
import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT_LIST;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.LINKS;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.AccountsApi;

/**
 *
 * @author alexs
 */
public class AccountsAdaptorImpl extends BaseAdaptor implements AccountsAdaptor {

    private final AccountsApi api;

    public AccountsAdaptorImpl() {
        this.api = new AccountsApi();
    }

    @Override
    public HttpClient getHttpClient() {
        return api.getHttpClient();
    }

    @Override
    public void setHttpClient(Supplier<HttpClient> httpClient) {
        super.httpClientSupplier = httpClient;
    }
    
	@Override
	public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<List<Link>>(cxt, cxt.getEntityName()).setResult(transport(cxt).acquireBaseUri().getData(), LINKS);
        }
        catch (ApiException x) {
            return new QueryContext<List<Link>>(cxt, cxt.getEntityName()).setServiceError(x);
        }
	}

	@Override
	public QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<AccountList>(cxt, cxt.getEntityName())
                .setResult(
                    transport(cxt)
                        .acquireAccounts()
                        .getData(), 
                    ACCOUNT_LIST
                );
        }
        catch (ApiException x) {
            return new QueryContext<AccountList>(cxt, cxt.getEntityName()).setServiceError(x);
        }
	}

	@Override
	public QueryContext<Account> createAccount(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Account>(cxt, cxt.getEntityName()).setResult(transport(cxt).createAccount(cxt.getCreateAccountRequest()).getData(), ACCOUNT);
        }
        catch (ApiException x) {
            return new QueryContext<Account>(cxt, cxt.getEntityName()).setServiceError(x);
        }
	}

	@Override
	public QueryContext<Account> getAccount(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Account>(cxt, cxt.getEntityName()).setResult(transport(cxt).accountsGet(cxt.getAccountProvider().accountId().toString()).getData(), ACCOUNT);
        }
        catch (ApiException x) {
            return new QueryContext<Account>(cxt, cxt.getEntityName()).setServiceError(x);
        }
	}
    
    private AccountsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
