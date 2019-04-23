/*
 * Copyright (c) 2019 SKB Kontur
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

package ru.kontur.extern_api.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentData;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;

@DisplayName("Drafts builder document service should be able to")
class FnsInventoryDraftsBuilderDocumentServiceIT {

    private static ExternEngine engine;
    private static DraftsBuilderDocumentCreator draftsBuilderDocumentCreator;
    private static FnsInventoryDraftsBuilderDocumentService draftsBuilderDocumentService;

    private static FnsInventoryDraftsBuilder draftsBuilder;
    private static FnsInventoryDraftsBuilderDocument draftsBuilderDocument;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        CryptoUtils cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        draftsBuilderDocumentCreator = new DraftsBuilderDocumentCreator();

        draftsBuilder = new DraftsBuilderCreator()
                .createFnsInventoryDraftsBuilder(
                        engine,
                        cryptoUtils
                );

        draftsBuilderDocument = draftsBuilderDocumentCreator
                .createFnsInventoryDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );

        draftsBuilderDocumentService = engine
                .getDraftsBuilderService()
                .fnsInventory()
                .getDocumentService(draftsBuilder.getId());
    }

    @Test
    @DisplayName("get drafts builder document")
    void create() {
        assertNotNull(draftsBuilderDocument);
    }

    @Test
    @DisplayName("get all drafts builder documents")
    void getAll() {
        FnsInventoryDraftsBuilderDocument[] draftsBuilderDocuments =
                draftsBuilderDocumentService
                        .getAllAsync()
                        .join();

        assertEquals(1, draftsBuilderDocuments.length);
        assertEquals(draftsBuilderDocument.getId(), draftsBuilderDocuments[0].getId());
    }

    @Test
    @DisplayName("get drafts builder document")
    void get() {
        FnsInventoryDraftsBuilderDocument receivedDraftsBuilderDocument =
                draftsBuilderDocumentService
                        .getAsync(draftsBuilderDocument.getId())
                        .join();

        assertEquals(
                draftsBuilderDocument.getId(),
                receivedDraftsBuilderDocument.getId()
        );

        assertEquals(
                draftsBuilderDocument.getMeta().getBuilderData().getClaimItemNumber(),
                receivedDraftsBuilderDocument.getMeta().getBuilderData().getClaimItemNumber()
        );
    }

    @Test
    @DisplayName("delete drafts builder document")
    void delete() {
        FnsInventoryDraftsBuilderDocument newDraftsBuilderDocument =
                draftsBuilderDocumentCreator.createFnsInventoryDraftsBuilderDocument(engine, draftsBuilder);

        draftsBuilderDocumentService
                .deleteAsync(newDraftsBuilderDocument.getId())
                .join();

        CompletionException exception = Assertions.assertThrows(
                CompletionException.class,
                () -> draftsBuilderDocumentService.getAsync(newDraftsBuilderDocument.getId()).join()
        );

        ApiException apiException = (ApiException) exception.getCause();
        assertEquals(404, apiException.getCode());
    }

    @Test
    @DisplayName("get meta drafts builder document")
    void getMeta() {
        FnsInventoryDraftsBuilderDocumentMeta meta =
                draftsBuilderDocumentService
                        .getMetaAsync(draftsBuilderDocument.getId())
                        .join();

        assertEquals(
                draftsBuilderDocument.getMeta().getBuilderData().getClaimItemNumber(),
                meta.getBuilderData().getClaimItemNumber()
        );
    }

    @Test
    @DisplayName("update meta drafts builder document")
    void updateMeta() {
        final String newClaimItemNumber = "1.02";

        FnsInventoryDraftsBuilderDocument newDraftsBuilderDocument =
                draftsBuilderDocumentCreator.createFnsInventoryDraftsBuilderDocument(engine, draftsBuilder);

        FnsInventoryDraftsBuilderDocumentMetaRequest newMeta = new FnsInventoryDraftsBuilderDocumentMetaRequest();

        FnsInventoryDraftsBuilderDocumentData data = new FnsInventoryDraftsBuilderDocumentData();
        data.setClaimItemNumber(newClaimItemNumber);

        newMeta.setBuilderData(data);

        FnsInventoryDraftsBuilderDocumentMeta actualMeta =
                draftsBuilderDocumentService
                        .updateMetaAsync(newDraftsBuilderDocument.getId(), newMeta)
                        .join();

        assertEquals(newClaimItemNumber, actualMeta.getBuilderData().getClaimItemNumber());
    }
}
