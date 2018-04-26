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

package ru.skbkontur.sdk.extern.drafts;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.model.Draft;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.model.DraftMeta;
import ru.skbkontur.sdk.extern.model.FnsRecipient;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.Sender;

/**
 * @author Mikhail Pavlenko
 */

public class DraftsValidator {

    public static void validateDraft(Draft draft) {
        assertNotNull("Draft must not be null!", draft);
        StandardObjectsValidator.validateId(draft.getId());
        assertEquals("Status is wrong!", "new", draft.getStatus().getValue());
    }

    public static void validateDraftMeta(DraftMeta draftMeta) {
        assertNotNull("DraftMeta must not be null!", draftMeta);
        validateSender(draftMeta.getSender());
        validateRecipient((FnsRecipient) draftMeta.getRecipient());
        validatePayer(draftMeta.getPayer());
    }

    private static void validateSender(Sender sender) {
        assertNotNull("Sender must not be null!", sender);
        assertEquals("Inn is wrong!", "string", sender.getInn());
        assertEquals("Kpp is wrong!", "string", sender.getKpp());
        assertEquals("Certificate is wrong!", "string", sender.getCertificate());
        assertEquals("Ipaddress is wrong!", "string", sender.getIpaddress());
    }

    private static void validateRecipient(FnsRecipient recipient) {
        assertNotNull("Recipient must not be null!", recipient);
        assertEquals("IfnsCode is wrong!", "string", recipient.getIfnsCode());
    }

    private static void validatePayer(Organization payer) {
        assertNotNull("Payer must not be null!", payer);
        assertEquals("Inn is wrong!", "string", payer.getInn());
        assertEquals("Kpp is wrong!", "string", payer.getKpp());
    }

    public static void validateDraftDocument(DraftDocument draftDocument,
        boolean withDecryptedContentLink, boolean withEncryptedContentLink,
        boolean withSignatureContentLink) {
        assertNotNull("DraftDocument  must not be null!", draftDocument);
        StandardObjectsValidator.validateId(draftDocument.getId());

        if (withDecryptedContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getDecryptedContentLink());
        } else {
            assertNull("DecryptedContentLink must be null!",
                draftDocument.getDecryptedContentLink());
        }

        if (withEncryptedContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getEncryptedContentLink());
        } else {
            assertNull("EncryptedContentLink must be null!",
                draftDocument.getEncryptedContentLink());
        }

        if (withSignatureContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getSignatureContentLink());
        } else {
            assertNull("SignatureContentLink must be null!",
                draftDocument.getSignatureContentLink());
        }
    }
}
