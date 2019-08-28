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
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentData;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;


@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Drafts builder document service should be able to")
class PfrReportDraftsBuilderDocumentServiceIT {

    private static ExternEngine engine;
    private static DraftsBuilderDocumentCreator draftsBuilderDocumentCreator;
    private static PfrReportDraftsBuilderDocumentService draftsBuilderDocumentService;

    private static PfrReportDraftsBuilder draftsBuilder;
    private static PfrReportDraftsBuilderDocument draftsBuilderDocument;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        CryptoUtils cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        draftsBuilderDocumentCreator = new DraftsBuilderDocumentCreator();

        draftsBuilder = new DraftsBuilderCreator()
                .createPfrReportDraftsBuilder(
                        engine,
                        cryptoUtils
                );

        draftsBuilderDocument = draftsBuilderDocumentCreator
                .createPfrReportDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );

        draftsBuilderDocumentService = engine
                .getDraftsBuilderService()
                .pfrReport()
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
        PfrReportDraftsBuilderDocument[] draftsBuilderDocuments =
                draftsBuilderDocumentService
                        .getAllAsync()
                        .join();

        assertEquals(1, draftsBuilderDocuments.length);
        assertEquals(draftsBuilderDocument.getId(), draftsBuilderDocuments[0].getId());
    }

    @Test
    @DisplayName("get drafts builder document")
    void get() {
        PfrReportDraftsBuilderDocument receivedDraftsBuilderDocument =
                draftsBuilderDocumentService
                        .getAsync(draftsBuilderDocument.getId())
                        .join();

        assertEquals(
                draftsBuilderDocument.getId(),
                receivedDraftsBuilderDocument.getId()
        );
    }

    @Test
    @DisplayName("delete drafts builder document")
    void delete() {
        PfrReportDraftsBuilderDocument newDraftsBuilderDocument =
                draftsBuilderDocumentCreator.createPfrReportDraftsBuilderDocument(engine, draftsBuilder);

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
        PfrReportDraftsBuilderDocumentMeta meta =
                draftsBuilderDocumentService
                        .getMetaAsync(draftsBuilderDocument.getId())
                        .join();
        assertNotNull(meta);
    }

    @Test
    @DisplayName("update meta drafts builder document")
    void updateMeta() {
        PfrReportDraftsBuilderDocument newDraftsBuilderDocument =
                draftsBuilderDocumentCreator.createPfrReportDraftsBuilderDocument(engine, draftsBuilder);

        PfrReportDraftsBuilderDocumentMetaRequest newMeta = new PfrReportDraftsBuilderDocumentMetaRequest();

        PfrReportDraftsBuilderDocumentData data = new PfrReportDraftsBuilderDocumentData();

        newMeta.setBuilderData(data);

        PfrReportDraftsBuilderDocumentMeta actualMeta =
                draftsBuilderDocumentService
                        .updateMetaAsync(newDraftsBuilderDocument.getId(), newMeta)
                        .join();
        assertNotNull(actualMeta);
    }
}
