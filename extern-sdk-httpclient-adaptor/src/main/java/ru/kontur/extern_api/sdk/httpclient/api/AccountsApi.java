/*
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
 *
 */
package ru.kontur.extern_api.sdk.httpclient.api;

import com.google.gson.reflect.TypeToken;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;

/**
 *
 * @author alexs
 */
public class AccountsApi extends RestApi {
	
    
	@Path("/")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<List<Link>> acquireBaseUri() throws ApiException {
		return invoke("acquireBaseUri", null, new TypeToken<List<Link>>(){}.getType());
    }
    
	@Path("/v1")
    @GET
    @Consumes("application/json; charset=utf-8")
	public ApiResponse<AccountList> acquireAccounts() throws ApiException {
		return invoke("acquireAccounts", null, new TypeToken<AccountList>(){}.getType());
	}

	@Path("/v1/{accountId}")
    @GET
    @Consumes("application/json; charset=utf-8")
	public ApiResponse<Account> accountsGet(@PathParam("accountId") String accountId) throws ApiException {
		return invoke("accountsGet", null, new TypeToken<Account>(){}.getType(), accountId);
	}

	@Path("/v1")
    @POST
    @Consumes("application/json; charset=utf-8")
	public ApiResponse<Account> createAccount(CreateAccountRequest createAccountRequest) throws ApiException {
        return invoke("createAccount", createAccountRequest, new TypeToken<Account>(){}.getType());
	}

}
