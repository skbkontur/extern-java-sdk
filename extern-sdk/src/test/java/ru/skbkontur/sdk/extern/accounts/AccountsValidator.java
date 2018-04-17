package ru.skbkontur.sdk.extern.accounts;

import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

class AccountsValidator {
    static void validateAccountList(AccountList accountList, boolean withAccounts) {
        assertNotNull("accountList must not be null!", accountList);
        assertEquals("Skip is wrong!", 0, accountList.getSkip().intValue());
        assertEquals("Take is wrong!", 0, accountList.getTake().intValue());
        assertEquals("TotalCount is wrong!", 0, accountList.getTotalCount().intValue());
        if(withAccounts){
            StandardObjectsValidator.validateNotEmptyList(accountList.getAccounts(), "Accounts");
            validateAccount(accountList.getAccounts().get(0), false);
        } else {
            StandardObjectsValidator.validateEmptyList(accountList.getAccounts(), "Accounts");
        }
    }

    static void validateAccount(Account account, boolean withLinks) {
        assertNotNull("Account must not be null!", account);
        StandardObjectsValidator.validateId(account.getId());
        assertEquals("Inn is wrong!", "string", account.getInn());
        assertEquals("Kpp is wrong!", "string", account.getKpp());
        assertEquals("OrganizationName is wrong!", "string", account.getOrganizationName());
        if(withLinks){
            StandardObjectsValidator.validateNotEmptyList(account.getLinks(), "Links");
            StandardObjectsValidator.validateLink(account.getLinks().get(0));
        } else {
            StandardObjectsValidator.validateEmptyList(account.getLinks(), "Links");
        }
    }
}
