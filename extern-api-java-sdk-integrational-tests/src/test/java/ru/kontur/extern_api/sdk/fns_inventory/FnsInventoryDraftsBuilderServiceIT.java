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

package ru.kontur.extern_api.sdk.fns_inventory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderData;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMetaRequest;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;


@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Drafts builder service should be able to")
class FnsInventoryDraftsBuilderServiceIT {

    private static ExternEngine engine;
    private static CryptoUtils cryptoUtils;
    private static DraftsBuilderCreator draftsBuilderCreator;
    private static FnsInventoryDraftsBuilderService draftsBuilderService;

    private static FnsInventoryDraftsBuilder draftsBuilder;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        draftsBuilderCreator = new DraftsBuilderCreator();

        draftsBuilderService = engine.getDraftsBuilderService().fnsInventory();
        draftsBuilder = draftsBuilderCreator.createFnsInventoryDraftsBuilder(
                engine,
                cryptoUtils
        );
    }

    @Test
    @DisplayName("get drafts builder")
    void create() {
        assertNotNull(draftsBuilder);
    }

    @Test
    @DisplayName("create drafts builder with specified id")
    void createWithId() {
        UUID draftsBuilderId = UUID.randomUUID();
        FnsInventoryDraftsBuilder newDraftsBuilder =
                draftsBuilderCreator.createFnsInventoryDraftsBuilder(
                        engine,
                        cryptoUtils,
                        draftsBuilderId
                );

        assertEquals(newDraftsBuilder.getId(), draftsBuilderId);
    }

    @Test
    @DisplayName("get drafts builder")
    void get() {
        FnsInventoryDraftsBuilder receivedDraftsBuilder =
                draftsBuilderService
                        .getAsync(draftsBuilder.getId())
                        .join();

        assertEquals(
                draftsBuilder.getId(),
                receivedDraftsBuilder.getId()
        );

        assertEquals(
                draftsBuilder.getMeta().getSender().getInn(),
                receivedDraftsBuilder.getMeta().getSender().getInn()
        );

        assertEquals(
                draftsBuilder.getMeta().getBuilderData().getRelatedDocument().getRelatedDocflowId(),
                receivedDraftsBuilder.getMeta().getBuilderData().getRelatedDocument().getRelatedDocflowId()
        );
    }

    @Test
    @DisplayName("delete drafts builder")
    void delete() {
        FnsInventoryDraftsBuilder newDraftsBuilder =
                draftsBuilderCreator.createFnsInventoryDraftsBuilder(engine, cryptoUtils);

        draftsBuilderService
                .deleteAsync(newDraftsBuilder.getId())
                .join();

        CompletionException exception = Assertions.assertThrows(
                CompletionException.class,
                () -> draftsBuilderService.getAsync(newDraftsBuilder.getId()).join()
        );

        ApiException apiException = (ApiException) exception.getCause();
        assertEquals(404, apiException.getCode());
    }

    @Test
    @DisplayName("get meta drafts builder")
    void getMeta() {
        FnsInventoryDraftsBuilderMeta meta =
                draftsBuilderService
                        .getMetaAsync(draftsBuilder.getId())
                        .join();

        assertEquals(
                draftsBuilder.getMeta().getSender().getInn(),
                meta.getSender().getInn()
        );

        assertEquals(
                draftsBuilder.getMeta().getBuilderData().getRelatedDocument().getRelatedDocflowId(),
                meta.getBuilderData().getRelatedDocument().getRelatedDocflowId()
        );
    }

    @Test
    @DisplayName("update meta drafts builder")
    void updateMeta() {
        FnsInventoryDraftsBuilder newDraftsBuilder =
                draftsBuilderCreator.createFnsInventoryDraftsBuilder(engine, cryptoUtils);

        FnsInventoryDraftsBuilderMetaRequest newMeta = new FnsInventoryDraftsBuilderMetaRequest();

        FnsInventoryDraftsBuilderData data = new FnsInventoryDraftsBuilderData();
        data.setRelatedDocument(newDraftsBuilder.getMeta().getBuilderData().getRelatedDocument());

        newMeta.setBuilderData(data);

        FnsInventoryDraftsBuilderMeta actualMeta =
                draftsBuilderService
                        .updateMetaAsync(newDraftsBuilder.getId(), newMeta)
                        .join();
        assertEquals(
                actualMeta.getBuilderData().getRelatedDocument().getRelatedDocflowId(),
                newDraftsBuilder.getMeta().getBuilderData().getRelatedDocument().getRelatedDocflowId()
                );
        assertEquals(
                actualMeta.getBuilderData().getRelatedDocument().getRelatedDocumentId(),
                newDraftsBuilder.getMeta().getBuilderData().getRelatedDocument().getRelatedDocumentId()
        );
    }
}
