package ru.skbkontur.sdk.extern.drafts;

import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.model.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

class DraftsValidator {
    static void validateDraft(Draft draft) {
        assertNotNull("Draft must not be null!", draft);
        StandardObjectsValidator.validateId(draft.getId());
        assertEquals("Status is wrong!", "new", draft.getStatus().getValue());
    }

    static void validateDraftMeta(DraftMeta draftMeta) {
        assertNotNull("DraftMeta must not be null!", draftMeta);
        validateSender(draftMeta.getSender());
        validateRecipient((FnsRecipient) draftMeta.getRecipient());
        validatePayer(draftMeta.getPayer());
    }

    static void validateSender(Sender sender) {
        assertNotNull("Sender must not be null!", sender);
        assertEquals("Inn is wrong!", "string", sender.getInn());
        assertEquals("Kpp is wrong!", "string", sender.getKpp());
        assertEquals("Certificate is wrong!", "string", sender.getCertificate());
        assertEquals("Ipaddress is wrong!", "string", sender.getIpaddress());
    }

    static void validateRecipient(FnsRecipient recipient) {
        assertNotNull("Recipient must not be null!", recipient);
        assertEquals("IfnsCode is wrong!", "string", recipient.getIfnsCode());
    }

    static void validatePayer(Organization payer) {
        assertNotNull("Payer must not be null!", payer);
        assertEquals("Inn is wrong!", "string", payer.getInn());
        assertEquals("Kpp is wrong!", "string", payer.getKpp());
    }

    static void validateDraftDocument(DraftDocument draftDocument, boolean withDecryptedContentLink, boolean withEncryptedContentLink, boolean withSignatureContentLink) {
        assertNotNull("DraftDocument  must not be null!", draftDocument);
        StandardObjectsValidator.validateId(draftDocument.getId());

        if (withDecryptedContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getDecryptedContentLink());
        } else {
            assertNull("DecryptedContentLink must be null!", draftDocument.getDecryptedContentLink());
        }

        if (withEncryptedContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getEncryptedContentLink());
        } else {
            assertNull("EncryptedContentLink must be null!", draftDocument.getEncryptedContentLink());
        }

        if (withSignatureContentLink) {
            StandardObjectsValidator.validateLink(draftDocument.getSignatureContentLink());
        } else {
            assertNull("SignatureContentLink must be null!", draftDocument.getSignatureContentLink());
        }
    }
}
