/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.it.utils.AbstractTest;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.CreateAccountRequest;
import ru.kontur.extern_api.sdk.model.Link;

/**
 * @author AlexS
 */
class AccountTest extends AbstractTest {

    private static final String INN = "7810654318";
    private static final String KPP = "781001001";
    private static final String ORG_NAME = "Рога и Копыта";

    @BeforeAll
    static void setUpClass() {
        AbstractTest.initEngine();
    }

    @BeforeEach
    void setUp() {
        accountService = engine.getAccountService();
    }

    @Test
    void acquireBaseUriLinks() throws Exception {
        QueryContext<List<Link>> cxt = accountService
                .acquireBaseUriAsync()
                .get()
                .ensureSuccess();

        assertFalse(cxt.get().isEmpty());
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

    private void checkFields(Account account, String inn, String kpp, String orgName){

        assertEquals(account.getInn(), inn );
        assertEquals(account.getKpp(), kpp);
        assertEquals(account.getOrganizationName(), orgName);
        assertFalse(account.getLinks().isEmpty());
    }
}
