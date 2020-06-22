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

package ru.kontur.extern_api.sdk.utils.builders;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;

public class DraftsBuilderDocumentFileCreator {

    public FnsInventoryDraftsBuilderDocumentFile createFnsInventoryDraftsBuilderDocumentFile(
            ExternEngine engine,
            CryptoUtils cryptoUtils,
            FnsInventoryDraftsBuilder draftsBuilder,
            FnsInventoryDraftsBuilderDocument draftsBuilderDocument
    ) {
        return createFnsInventoryDraftsBuilderDocumentFile(engine, cryptoUtils, draftsBuilder, draftsBuilderDocument, null);
    }

    public FnsInventoryDraftsBuilderDocumentFile createFnsInventoryDraftsBuilderDocumentFile(
            ExternEngine engine,
            CryptoUtils cryptoUtils,
            FnsInventoryDraftsBuilder draftsBuilder,
            FnsInventoryDraftsBuilderDocument draftsBuilderDocument,
            UUID draftsBuilderDocumentFileId
    ) {
        final String fileName = "AnyFileName.pdf";

        FnsInventoryDraftsBuilderDocumentFileContents contents = new FnsInventoryDraftsBuilderDocumentFileContents();
        FnsInventoryDraftsBuilderDocumentFileMetaRequest meta = new FnsInventoryDraftsBuilderDocumentFileMetaRequest();

        String scannedContent = getScannedContent();

        byte[] signature = cryptoUtils.sign(
                engine.getConfiguration().getThumbprint(),
                Base64.getDecoder().decode(scannedContent)
        );

        contents.setBase64Content(scannedContent);
        contents.setBase64SignatureContent(Base64.getEncoder().encodeToString(signature));

        meta.setFileName(fileName);
        contents.setMeta(meta);

        FnsInventoryDraftsBuilderDocumentFileService fileService = engine
                .getDraftsBuilderService()
                .fnsInventory()
                .getDocumentService(draftsBuilder.getId())
                .getFileService(draftsBuilderDocument.getId());

        return draftsBuilderDocumentFileId == null
                ? fileService.createAsync(contents).join()
                : fileService.updateAsync(draftsBuilderDocumentFileId, contents).join();
    }

    public String getScannedContent() {
        return getTestDataFileContent("docs/Scanned.pdf");
    }

    public PfrReportDraftsBuilderDocumentFile createPfrReportDraftsBuilderDocumentFile(
            ExternEngine engine,
            CryptoUtils cryptoUtils,
            PfrReportDraftsBuilder draftsBuilder,
            PfrReportDraftsBuilderDocument draftsBuilderDocument,
            boolean isDss
    ) {
        final String fileName = "SomePfrXmlReport.xml";
        PfrReportDraftsBuilderDocumentFileContents contents = new PfrReportDraftsBuilderDocumentFileContents();
        PfrReportDraftsBuilderDocumentFileMetaRequest meta = new PfrReportDraftsBuilderDocumentFileMetaRequest();

        String pfrReportContent = getTestDataFileContent(
                isDss ? "docs/pfr-dss/SomePfrReport.xml" : "docs/pfr/SomePfrReport.xml");

        if (cryptoUtils != null) {
            byte[] signature = cryptoUtils.sign(
                    engine.getConfiguration().getThumbprint(),
                    Base64.getDecoder().decode(pfrReportContent)
            );
            contents.setBase64SignatureContent(Base64.getEncoder().encodeToString(signature));
        }
        contents.setBase64Content(pfrReportContent);

        meta.setFileName(fileName);
        contents.setMeta(meta);

        return engine
                .getDraftsBuilderService()
                .pfrReport()
                .getDocumentService(draftsBuilder.getId())
                .getFileService(draftsBuilderDocument.getId())
                .createAsync(contents)
                .join();
    }


    public PfrReportDraftsBuilderDocumentFile createPfrReportV2DraftsBuilderDocumentFile(
            ExternEngine engine,
            PfrReportDraftsBuilder draftsBuilder,
            PfrReportDraftsBuilderDocument draftsBuilderDocument
    ) {
        final String fileName = "SomePfrV2Report.xml";
        PfrReportDraftsBuilderDocumentFileContents contents = new PfrReportDraftsBuilderDocumentFileContents();
        PfrReportDraftsBuilderDocumentFileMetaRequest meta = new PfrReportDraftsBuilderDocumentFileMetaRequest();

        String pfrReportContent = getTestDataFileContent("docs/pfr/SomePfrV2Report.xml");

        contents.setBase64Content(pfrReportContent);

        meta.setFileName(fileName);
        contents.setMeta(meta);

        return engine
                .getDraftsBuilderService()
                .pfrReport()
                .getDocumentService(draftsBuilder.getId())
                .getFileService(draftsBuilderDocument.getId())
                .createAsync(contents)
                .join();
    }

    public String getTestDataFileContent(String path) {
        URL contentUrl = DraftsBuilderDocumentFileCreator.class
                .getClassLoader()
                .getResource(path);

        String contentPath = new File(Objects.requireNonNull(contentUrl).getFile()).getAbsolutePath();

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(contentPath));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find scanned file in resources");
        }
    }
}
