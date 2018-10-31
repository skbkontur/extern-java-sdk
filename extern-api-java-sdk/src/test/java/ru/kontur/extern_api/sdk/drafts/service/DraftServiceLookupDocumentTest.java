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
package ru.kontur.extern_api.sdk.drafts.service;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.drafts.DraftsValidator;
import ru.kontur.extern_api.sdk.model.DraftDocument;

/**
 * @author Mikhail Pavlenko
 */
class DraftServiceLookupDocumentTest extends DraftServiceTestBase {


    @Test
    void testLookupDocument_DraftDocument() throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful("{\"id\": \"" + StandardValues.ID + "\"}");

        DraftDocument draftDocument = draftService
                .lookupDocumentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        DraftsValidator.validateDraftDocument(
                draftDocument,
                false,
                false,
                false);
    }

    @Test
    void testLookupDocument_DecryptedContentLink() throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful("{"
                + "\"id\": \"" + StandardValues.ID + "\","
                + "\"decrypted-content-link\": {"
                + "  \"href\": \"string\","
                + "  \"rel\": \"string\","
                + "  \"name\": \"string\","
                + "  \"title\": \"string\","
                + "  \"profile\": \"string\","
                + "  \"templated\": true"
                + "}}");

        DraftDocument draftDocument = draftService
                .lookupDocumentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        DraftsValidator.validateDraftDocument(
                draftDocument,
                true,
                false,
                false);
    }

    @Test
    void testLookupDocument_EncryptedContentLink() throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful("{"
                + "\"id\": \"" + StandardValues.ID + "\","
                + "\"encrypted-content-link\": {"
                + "  \"href\": \"string\","
                + "  \"rel\": \"string\","
                + "  \"name\": \"string\","
                + "  \"title\": \"string\","
                + "  \"profile\": \"string\","
                + "  \"templated\": true"
                + "}}");

        DraftDocument draftDocument = draftService
                .lookupDocumentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        DraftsValidator.validateDraftDocument(
                draftDocument,
                false,
                true,
                false);

    }

    @Test
    void testLookupDocument_SignatureContentLink() throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful("{"
                + "\"id\": \"" + StandardValues.ID + "\","
                + "\"signature-content-link\": {"
                + "  \"href\": \"string\","
                + "  \"rel\": \"string\","
                + "  \"name\": \"string\","
                + "  \"title\": \"string\","
                + "  \"profile\": \"string\","
                + "  \"templated\": true"
                + "}}");

        DraftDocument draftDocument = draftService
                .lookupDocumentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        DraftsValidator.validateDraftDocument(
                draftDocument,
                false,
                false,
                true);
    }

    @Test
    void testLookupDocument_DocumentDescription()
            throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful("{"
                + "\"id\": \"" + StandardValues.ID + "\","
                + "\"description\": {"
                + "  \"type\": \"urn:docflow:fns534-report\","
                + "  \"filename\": \"string\","
                + "  \"content-type\": \"string\""
                + "}}");

        DraftDocument draftDocument = draftService
                .lookupDocumentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        DraftsValidator.validateDocumentDescription(draftDocument.getDescription());
    }
}
