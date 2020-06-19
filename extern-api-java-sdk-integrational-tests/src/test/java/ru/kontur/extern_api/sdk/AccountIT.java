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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.ExternUserRole;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;

@Execution(ExecutionMode.SAME_THREAD)
class AccountIT {

    private static AccountService accountService;

    @BeforeAll
    static void setUpClass() {
        Configuration config = TestConfig.LoadConfigFromEnvironment();
        ExternEngine engine =TestSuite.LoadManually((cfg, builder) -> builder
                .buildAuthentication(cfg.getAuthBaseUri(), authBuilder -> authBuilder
                        .passwordAuthentication(config.getLoginSecond(), config.getPassSecond())
                )
                .doNotUseCryptoProvider()
                .doNotSetupAccount()
                .build(Level.BODY)
        ).engine;
        accountService = engine.getAccountService();
    }

  /*
    private static final String INN = "7810654318";
    private static final String KPP = "781001001";
    private static final String ORG_NAME = "Рога и Копыта";
    */

    private static final String INN = "5447822240";
    private static final String KPP = "156145230";
    private static final String ORG_NAME = "Рога123 и Копыта123";

  /*  private static final String INN = "0270006356";
    private static final String KPP = "014243797";
    private static final String ORG_NAME = "Рога789 и Копыта789";
   */

    @Test
    void acquireAccounts() {
        QueryContext<AccountList> cxt = accountService
                .getAccountsAsync(0, 100)
                .join()
                .ensureSuccess();

        assertFalse(cxt.getOrThrow().getAccounts().isEmpty());
    }

    @Test
    void createAccount() {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .inn(INN)
                .kpp(KPP)
                .organizationName(ORG_NAME);

        QueryContext<Account> cxt = accountService
                .createAccountAsync(createAccountRequest)
                .join()
                .ensureSuccess();

        checkFields(cxt.getOrThrow(), INN, KPP, ORG_NAME);
    }


    @Test
    void acquireAccount() {
        QueryContext<AccountList> cxt = accountService
                .getAccountsAsync(0, 100)
                .join()
                .ensureSuccess();

        assertFalse(cxt.getOrThrow().getAccounts().isEmpty());

        Account account = cxt.getOrThrow().getAccounts().get(0);

        QueryContext<Account> accCxt = accountService
                .getAccountAsync(account.getId().toString())
                .join()
                .ensureSuccess();

        checkFields(accCxt.getOrThrow(), account.getInn(), account.getKpp(), account.getOrganizationName());
    }

    @Test
    void deleteAccount() {

        CreateAccountRequest createAccountRequest = new CreateAccountRequest()
                .inn(INN)
                .kpp(KPP)
                .organizationName(ORG_NAME);

        QueryContext<Account> createCxt = accountService
                .createAccountAsync(createAccountRequest)
                .join()
                .ensureSuccess();

        QueryContext<?> deleteCxt = accountService
                .deleteAccountAsync(createCxt.getAccount().getId())
                .join()
                .ensureSuccess();

        assertNull(deleteCxt.getServiceError());

        ApiException apiException = Assertions.assertThrows(
                ApiException.class,
                () -> accountService.getAccountAsync(createCxt.getOrThrow().getId()).get().getOrThrow()
        );

        Assertions.assertEquals(404, apiException.getCode());
    }


    private void checkFields(Account account, String inn, String kpp, String orgName) {
        assertEquals(account.getInn(), inn);
        assertEquals(account.getKpp(), kpp);
        assertEquals(account.getOrganizationName(), orgName);
        assertFalse(account.getLinks().isEmpty());
        assertEquals(account.getProductName(), "extern");
        assertEquals(account.getRole(), ExternUserRole.ADMIN);
    }
}
