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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.drafts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;

class DraftServiceCloudSignTest extends DraftServiceTestBase {

    @Test
    void signInitiationShouldParseAnswer() throws ExecutionException, InterruptedException {

        serverPlease()
                .when(request().withMethod("POST"), exactly(1))
                .respond(response().withBody("{ "
                        + "\"links\": [] , "
                        + "\"documents-to-sign\": [], "
                        + "\"request-id\": \"123\" }"
                ));

        SignInitiation signInitiation = draftService
                .cloudSignInitAsync(StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        assertTrue(signInitiation.getLinks().isEmpty());
        assertTrue(signInitiation.getDocumentsToSign().isEmpty());
        assertEquals("123", signInitiation.getRequestId());
    }

    @Test
    void signConfirmShouldParseAnswer() throws ExecutionException, InterruptedException {

        serverPlease()
                .when(request()
                                .withMethod("POST")
                                .withQueryStringParameter("code", "123")
                                .withQueryStringParameter("requestId", "321"),
                        exactly(1))
                .respond(response().withBody("{ "
                        + "\"signed-documents\": [] }"
                ));

        SignedDraft signedDraft = draftService
                .cloudSignConfirmAsync(StandardValues.GUID, "321", "123")
                .get()
                .ensureSuccess()
                .get();

        assertTrue(signedDraft.getSignedDocuments().isEmpty());
    }

    @Test
    void cloudSignMethodWithSupplierShouldSignDraft() throws Exception {

        UUID draftId = UUID.randomUUID();

        serverPlease()
                .when(request().withPath(".*/cloud-sign$"), exactly(1))
                .respond(response().withBody("{ "
                        + "\"links\": [] , "
                        + "\"documents-to-sign\": [], "
                        + "\"request-id\": \"123\" }"
                ));

        serverPlease()
                .when(
                        request()
                                .withPath(".*/cloud-sign-confirm$")
                                .withQueryStringParameter("code", "1234"),
                        exactly(1))
                .respond(response().withBody("{ \"signed-documents\": [] }"));

        SignedDraft signedDraft = draftService
                .cloudSignAsync(draftId, cxt -> "1234")
                .get()
                .ensureSuccess()
                .get();

        assertTrue(signedDraft.getSignedDocuments().isEmpty());
    }
}
