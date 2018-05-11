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
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT_LIST;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.LINKS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.OBJECT;
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
	public QueryContext<List<Link>> acquireBaseUri(QueryContext<List<Link>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt).acquireBaseUri().getData(), LINKS);
        }
        catch (ApiException x) {
            return cxt.setServiceError(x);
        }
	}

	@Override
	public QueryContext<AccountList> acquireAccounts(QueryContext<AccountList> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt
                .setResult(
                    transport(cxt)
                        .acquireAccounts()
                        .getData(), 
                    ACCOUNT_LIST
                );
        }
        catch (ApiException x) {
            return cxt.setServiceError(x);
        }
	}

	@Override
	public QueryContext<Object> createAccount(QueryContext<Object> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt).createAccount(cxt.getCreateAccountRequest()).getData(), OBJECT);
        }
        catch (ApiException x) {
            return cxt.setServiceError(x);
        }
	}

	@Override
	public QueryContext<Account> getAccount(QueryContext<Account> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt).accountsGet(cxt.getAccountId().toString()).getData(), ACCOUNT);
        }
        catch (ApiException x) {
            return cxt.setServiceError(x);
        }
	}
    
    private AccountsApi transport(QueryContext<?> cxt) {
        configureTransport(api.getHttpClient(), cxt);
        return api;
    }
}
