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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.drafts.testBase.DraftServiceTestBase;

/**
 * @author Mikhail Pavlenko
 */
class DraftServiceGetSignatureContentTest extends DraftServiceTestBase {


    @Test
    void testGetSignatureContent_Empty() throws ExecutionException, InterruptedException {

        serverPleaseGetSuccessful(GSON.toJson(new byte[0]));

        byte[] signatureContent = draftService
                .getSignatureContentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        assertTrue(new String(signatureContent).isEmpty());
    }

    @Test
    void testGetSignatureContent() throws ExecutionException, InterruptedException {

        byte[] content = new byte[]{1, 2, 3};

        serverPleaseGetSuccessful(GSON.toJson(content));

        byte[] signatureContent = draftService
                .getSignatureContentAsync(StandardValues.GUID, StandardValues.GUID)
                .get()
                .ensureSuccess()
                .get();

        assertEquals(new String(signatureContent), new String(content));
    }

    @ParameterizedTest
    @ValueSource(ints = { 400, 401, 403, 404, 500})
    void testGetSignatureContentWithError(int code)
            throws ExecutionException, InterruptedException {

        serverPleaseGetError(code);

        QueryContext signatureContentCxt = draftService
                .getSignatureContentAsync(StandardValues.GUID, StandardValues.GUID)
                .get();

        assertTrue(signatureContentCxt.isFail());
        assertEquals(code, signatureContentCxt.getServiceError().getResponseCode());
    }
}
