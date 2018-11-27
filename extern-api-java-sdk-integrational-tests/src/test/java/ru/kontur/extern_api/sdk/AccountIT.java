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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.service.AccountService;


class AccountIT {

    private static AccountService accountService;

    private static final String INN = "7810654318";
    private static final String KPP = "781001001";
    private static final String ORG_NAME = "Рога и Копыта";

    @BeforeAll
    static void setUpClass() {
        Configuration config = TestConfig.LoadConfigFromEnvironment();

        ExternEngine engine = ExternEngineBuilder
                .createExternEngine(config.getServiceBaseUri())
                .apiKey(config.getApiKey())
                .buildAuthentication(config.getAuthBaseUri(), builder -> builder.
                        passwordAuthentication(config.getLogin(), config.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(config.getAccountId())
                .build(Level.BODY);

        accountService = engine.getAccountService();
    }

    @Test
    void acquireAccounts() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        assertFalse(cxt.get().getAccounts().isEmpty());
    }

    @Test
    void createAccount() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .inn(INN)
                .kpp(KPP)
                .organizationName(ORG_NAME);

        QueryContext<Account> cxt = accountService
                .createAccountAsync(createAccountRequest)
                .get()
                .ensureSuccess();

        checkFields(cxt.get(), INN, KPP, ORG_NAME);
    }


    @Test
    void acquireAccount() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        assertFalse(cxt.get().getAccounts().isEmpty());

        Account account = cxt.get().getAccounts().get(0);

        QueryContext<Account> accCxt = accountService
                .getAccountAsync(account.getId().toString())
                .get()
                .ensureSuccess();

        checkFields(accCxt.get(), account.getInn(), account.getKpp(), account.getOrganizationName());
    }

    private void checkFields(Account account, String inn, String kpp, String orgName) {

        assertEquals(account.getInn(), inn);
        assertEquals(account.getKpp(), kpp);
        assertEquals(account.getOrganizationName(), orgName);
        assertFalse(account.getLinks().isEmpty());
    }
}
