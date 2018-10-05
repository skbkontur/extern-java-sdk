/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.test.utils.AbstractTest;

/**
 * @author AlexS
 */
public class AccountTest extends AbstractTest {

    @BeforeClass
    public static void setUpClass() {
        AbstractTest.initEngine();
    }

    @Before
    public void setUp() {
        accountService = engine.getAccountService();
    }

    @Test
    public void acquireBaseUriLinks() throws Exception {
        QueryContext<List<Link>> cxt = accountService
                .acquireBaseUriAsync()
                .get().ensureSuccess();

        assertNotNull(cxt.get());
    }

    @Test
    public void acquireAccounts() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        assertNotNull(cxt.get());
    }

    @Test
    public void createAccount() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setInn("7810123451");
        createAccountRequest.setKpp("781001001");
        createAccountRequest.setOrganizationName("Рога и Копыта");

        QueryContext<Account> cxt = accountService
                .createAccountAsync(createAccountRequest)
                .get()
                .ensureSuccess();

        assertNotNull(cxt.get());
    }


    @Test
    public void acquireAccount() throws Exception {
        QueryContext<AccountList> cxt = accountService
                .acquireAccountsAsync()
                .get()
                .ensureSuccess();

        assertNotNull("AccountList is null.", cxt.get());
        assertNotNull("AccountList.List<Account> is null.", cxt.get().getAccounts());
        Assert.assertFalse("AccountList.List<Account> is empty.",
                cxt.get().getAccounts().isEmpty());

        UUID accountId = cxt.get().getAccounts().get(0).getId();

        QueryContext<Account> accCxt = accountService
                .getAccountAsync(accountId.toString())
                .get().ensureSuccess();

        assertNotNull("Account is null.", accCxt.get());
    }
}
