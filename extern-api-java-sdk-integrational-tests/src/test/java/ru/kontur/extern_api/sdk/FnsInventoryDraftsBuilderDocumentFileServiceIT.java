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

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

@DisplayName("Drafts builder document file service should be able to")
class FnsInventoryDraftsBuilderDocumentFileServiceIT {

    private static ExternEngine engine;
    private static CryptoUtils cryptoUtils;
    private static DraftsBuilderDocumentFileCreator draftsBuilderDocumentFileCreator;
    private static FnsInventoryDraftsBuilderDocumentFileService draftsBuilderDocumentFileService;

    private static FnsInventoryDraftsBuilder draftsBuilder;
    private static FnsInventoryDraftsBuilderDocument draftsBuilderDocument;
    private static FnsInventoryDraftsBuilderDocumentFile draftsBuilderDocumentFile;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        draftsBuilderDocumentFileCreator = new DraftsBuilderDocumentFileCreator();

        draftsBuilder = new DraftsBuilderCreator()
                .createFnsInventoryDraftsBuilder(
                        engine,
                        cryptoUtils
                );

        draftsBuilderDocument = new DraftsBuilderDocumentCreator()
                .createFnsInventoryDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );

        draftsBuilderDocumentFile = draftsBuilderDocumentFileCreator
                .createFnsInventoryDraftsBuilderDocumentFile(
                        engine,
                        cryptoUtils,
                        draftsBuilder,
                        draftsBuilderDocument
                );

        draftsBuilderDocumentFileService = engine
                .getDraftsBuilderService()
                .fnsInventory()
                .getDocumentService(draftsBuilder.getId())
                .getFileService(draftsBuilderDocument.getId());
    }

    @Test
    @DisplayName("get drafts builder document file")
    void create() {
        assertNotNull(draftsBuilderDocumentFile);
    }

    @Test
    @DisplayName("get all drafts builder document files")
    void getAll() {
        FnsInventoryDraftsBuilderDocumentFile[] draftsBuilderDocumentFiles =
                draftsBuilderDocumentFileService
                        .getAllAsync()
                        .join();

        assertEquals(1, draftsBuilderDocumentFiles.length);
        assertEquals(draftsBuilderDocumentFile.getId(), draftsBuilderDocumentFiles[0].getId());
    }

    @Test
    @DisplayName("get drafts builder document file")
    void get() {
        FnsInventoryDraftsBuilderDocumentFile receivedDraftsBuilderDocumentFile =
                draftsBuilderDocumentFileService
                        .getAsync(draftsBuilderDocumentFile.getId())
                        .join();

        assertEquals(
                draftsBuilderDocumentFile.getId(),
                receivedDraftsBuilderDocumentFile.getId()
        );

        assertEquals(
                draftsBuilderDocumentFile.getMeta().getFileName(),
                receivedDraftsBuilderDocumentFile.getMeta().getFileName()
        );
    }

    @Test
    @DisplayName("update drafts builder document file")
    void update() {
        final String newFileName = "NewFileName.pdf";

        FnsInventoryDraftsBuilderDocumentFile newDraftsBuilderDocumentFile =
                draftsBuilderDocumentFileCreator
                        .createFnsInventoryDraftsBuilderDocumentFile(
                                engine,
                                cryptoUtils,
                                draftsBuilder,
                                draftsBuilderDocument
                        );

        FnsInventoryDraftsBuilderDocumentFileContents newContents = new FnsInventoryDraftsBuilderDocumentFileContents();
        newContents.setBase64Content(draftsBuilderDocumentFileCreator.getScannedContent());

        FnsInventoryDraftsBuilderDocumentFileMetaRequest meta = new FnsInventoryDraftsBuilderDocumentFileMetaRequest();
        meta.setFileName(newFileName);
        newContents.setMeta(meta);

        FnsInventoryDraftsBuilderDocumentFile updatedFile =
                draftsBuilderDocumentFileService
                        .updateAsync(
                                newDraftsBuilderDocumentFile.getId(),
                                newContents
                        )
                        .join();

        assertEquals(
                newDraftsBuilderDocumentFile.getId(),
                updatedFile.getId()
        );

        assertEquals(
                newFileName,
                updatedFile.getMeta().getFileName()
        );

        assertNull(updatedFile.getSignatureContentLink());
    }

    @Test
    @DisplayName("delete drafts builder document file")
    void delete() {
        FnsInventoryDraftsBuilderDocumentFile newDraftsBuilderDocumentFile =
                draftsBuilderDocumentFileCreator
                        .createFnsInventoryDraftsBuilderDocumentFile(
                                engine,
                                cryptoUtils,
                                draftsBuilder,
                                draftsBuilderDocument
                        );

        draftsBuilderDocumentFileService
                .deleteAsync(newDraftsBuilderDocumentFile.getId())
                .join();

        CompletionException exception = Assertions.assertThrows(
                CompletionException.class,
                () -> draftsBuilderDocumentFileService.getAsync(newDraftsBuilderDocumentFile.getId()).join()
        );

        ApiException apiException = (ApiException) exception.getCause();
        assertEquals(404, apiException.getCode());
    }

    @Test
    @DisplayName("get content drafts builder document file")
    void getContent() {
        byte[] content = draftsBuilderDocumentFileService
                .getContentAsync(draftsBuilderDocumentFile.getId())
                .join();

        assertNotNull(content);
    }

    @Test
    @DisplayName("get signature drafts builder document file")
    void getSignature() {
        byte[] content = draftsBuilderDocumentFileService
                .getSignatureAsync(draftsBuilderDocumentFile.getId())
                .join();

        assertNotNull(content);
    }

    @Test
    @DisplayName("get meta drafts builder document file")
    void getMeta() {
        FnsInventoryDraftsBuilderDocumentFileMeta meta =
                draftsBuilderDocumentFileService
                        .getMetaAsync(draftsBuilderDocumentFile.getId())
                        .join();

        assertEquals(
                draftsBuilderDocumentFile.getMeta().getFileName(),
                meta.getFileName()
        );
    }

    @Test
    @DisplayName("update meta drafts builder document file")
    void updateMeta() {
        final String newFileName = "NewFileName.pdf";

        FnsInventoryDraftsBuilderDocumentFile newDraftsBuilderDocumentFile =
                draftsBuilderDocumentFileCreator
                        .createFnsInventoryDraftsBuilderDocumentFile(
                                engine,
                                cryptoUtils,
                                draftsBuilder,
                                draftsBuilderDocument
                        );

        FnsInventoryDraftsBuilderDocumentFileMetaRequest newMeta =
                new FnsInventoryDraftsBuilderDocumentFileMetaRequest();

        newMeta.setFileName(newFileName);

        FnsInventoryDraftsBuilderDocumentFileMeta actualMeta =
                draftsBuilderDocumentFileService
                        .updateMetaAsync(newDraftsBuilderDocumentFile.getId(), newMeta)
                        .join();

        assertEquals(
                newFileName,
                actualMeta.getFileName()
        );
    }
}
