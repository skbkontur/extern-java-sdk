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

package ru.kontur.extern_api.sdk.pfr_report;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderData;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderMetaRequest;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;


@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Drafts builder service should be able to")
class PfrReportDraftsBuilderServiceIT {

    private static ExternEngine engine;
    private static CryptoUtils cryptoUtils;
    private static DraftsBuilderCreator draftsBuilderCreator;
    private static PfrReportDraftsBuilderService draftsBuilderService;

    private static PfrReportDraftsBuilder draftsBuilder;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        draftsBuilderCreator = new DraftsBuilderCreator();

        draftsBuilderService = engine.getDraftsBuilderService().pfrReport();
        draftsBuilder = draftsBuilderCreator.createPfrReportDraftsBuilder(
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
    @DisplayName("get drafts builder")
    void get() {
        PfrReportDraftsBuilder receivedDraftsBuilder =
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
                draftsBuilder.getMeta().getBuilderData().getRegistrationNumber(),
                receivedDraftsBuilder.getMeta().getBuilderData().getRegistrationNumber()
        );
        assertEquals(
                draftsBuilder.getMeta().getBuilderData().getUpfrCode(),
                receivedDraftsBuilder.getMeta().getBuilderData().getUpfrCode()
        );
    }

    @Test
    @DisplayName("delete drafts builder")
    void delete() {
        PfrReportDraftsBuilder newDraftsBuilder =
                draftsBuilderCreator.createPfrReportDraftsBuilder(engine, cryptoUtils);

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
        PfrReportDraftsBuilderMeta meta =
                draftsBuilderService
                        .getMetaAsync(draftsBuilder.getId())
                        .join();

        assertEquals(
                draftsBuilder.getMeta().getSender().getInn(),
                meta.getSender().getInn()
        );

        assertEquals(
                draftsBuilder.getMeta().getBuilderData().getRegistrationNumber(),
                meta.getBuilderData().getRegistrationNumber()
        );
        assertEquals(
                draftsBuilder.getMeta().getBuilderData().getUpfrCode(),
                meta.getBuilderData().getUpfrCode()
        );
    }

    @Test
    @DisplayName("update meta drafts builder")
    void updateMeta() {
        // Arrange
        PfrReportDraftsBuilder newDraftsBuilder =
                draftsBuilderCreator.createPfrReportDraftsBuilder(engine, cryptoUtils);

        PfrReportDraftsBuilderMetaRequest newMeta = new PfrReportDraftsBuilderMetaRequest();

        String newUpfrCode = "777-777";
        String newRegistrationNumber = "777-777-123456";
        PfrReportDraftsBuilderData data = new PfrReportDraftsBuilderData();
        data.setUpfrCode(newUpfrCode);
        data.setRegistrationNumber(newRegistrationNumber);

        // Act
        newMeta.setBuilderData(data);

        // Assert
        PfrReportDraftsBuilderMeta actualMeta =
                draftsBuilderService
                        .updateMetaAsync(newDraftsBuilder.getId(), newMeta)
                        .join();

        assertEquals(newUpfrCode, actualMeta.getBuilderData().getUpfrCode());
        assertEquals(newRegistrationNumber, actualMeta.getBuilderData().getRegistrationNumber());
    }
}
