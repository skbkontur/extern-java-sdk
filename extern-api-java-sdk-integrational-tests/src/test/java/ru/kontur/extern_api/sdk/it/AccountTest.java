/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.AccountService;


class AccountTest {

    private static AccountService accountService;

    @BeforeAll
    static void setUpClass() {
        accountService = TestSuite.Load().engine.getAccountService();
    }

    @Test
    void acquireBaseUriLinks() throws Exception {
        QueryContext<List<Link>> cxt = accountService
                .acquireBaseUriAsync()
                .get().ensureSuccess();

        Assertions.assertNotNull(cxt.get());
    }

    @Test
    void acquireAccounts() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        Assertions.assertNotNull(cxt.get());
    }

    @Test
    void createAccount() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setInn("7810123451");
        createAccountRequest.setKpp("781001001");
        createAccountRequest.setOrganizationName("Рога и Копыта");

        QueryContext<Account> cxt = accountService
                .createAccountAsync(createAccountRequest)
                .get()
                .ensureSuccess();

        Assertions.assertNotNull(cxt.get());
    }


    @Test
    void acquireAccount() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        Assertions.assertNotNull(cxt.get());
        Assertions.assertNotNull(cxt.get().getAccounts());
        Assertions.assertFalse(cxt.get().getAccounts().isEmpty());

        UUID accountId = cxt.get().getAccounts().get(0).getId();

        QueryContext<Account> accCxt = accountService
                .getAccountAsync(accountId.toString())
                .get().ensureSuccess();

        Assertions.assertNotNull(accCxt.get());
    }
}
