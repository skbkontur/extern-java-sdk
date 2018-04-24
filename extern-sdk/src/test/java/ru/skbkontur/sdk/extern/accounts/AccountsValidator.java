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

package ru.skbkontur.sdk.extern.accounts;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;

/**
 * @author Mikhail Pavlenko
 */

public class AccountsValidator {

    public static void validateAccountList(AccountList accountList, boolean withAccounts) {
        assertNotNull("accountList must not be null!", accountList);
        assertEquals("Skip is wrong!", 0, accountList.getSkip().intValue());
        assertEquals("Take is wrong!", 0, accountList.getTake().intValue());
        assertEquals("TotalCount is wrong!", 0, accountList.getTotalCount().intValue());
        if (withAccounts) {
            StandardObjectsValidator.validateNotEmptyList(accountList.getAccounts(), "Accounts");
            validateAccount(accountList.getAccounts().get(0), false);
        } else {
            StandardObjectsValidator.validateEmptyList(accountList.getAccounts(), "Accounts");
        }
    }

    public static void validateAccount(Account account, boolean withLinks) {
        assertNotNull("Account must not be null!", account);
        StandardObjectsValidator.validateId(account.getId());
        assertEquals("Inn is wrong!", "string", account.getInn());
        assertEquals("Kpp is wrong!", "string", account.getKpp());
        assertEquals("OrganizationName is wrong!", "string", account.getOrganizationName());
        if (withLinks) {
            StandardObjectsValidator.validateNotEmptyList(account.getLinks(), "Links");
            StandardObjectsValidator.validateLink(account.getLinks().get(0));
        } else {
            StandardObjectsValidator.validateEmptyList(account.getLinks(), "Links");
        }
    }
}
