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
import ru.kontur.extern_api.sdk.utils.CryptoUtils;

public class DraftsBuilderDocumentFileCreator {

    public FnsInventoryDraftsBuilderDocumentFile createFnsInventoryDraftsBuilderDocumentFile(
            ExternEngine engine,
            CryptoUtils cryptoUtils,
            FnsInventoryDraftsBuilder draftsBuilder,
            FnsInventoryDraftsBuilderDocument draftsBuilderDocument
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

        return engine
                .getDraftsBuilderService()
                .fnsInventory()
                .getDocumentService(draftsBuilder.getId())
                .getFileService(draftsBuilderDocument.getId())
                .createAsync(contents)
                .join();
    }

    public String getScannedContent() {
        URL contentUrl = DraftsBuilderDocumentFileCreator.class
                .getClassLoader()
                .getResource("docs/Scanned.pdf");

        String contentPath = new File(Objects.requireNonNull(contentUrl).getFile()).getAbsolutePath();

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(contentPath));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find scanned file in resources");
        }
    }

    public PfrReportDraftsBuilderDocumentFile createPfrReportDraftsBuilderDocumentFile(
            ExternEngine engine,
            CryptoUtils cryptoUtils,
            PfrReportDraftsBuilder draftsBuilder,
            PfrReportDraftsBuilderDocument draftsBuilderDocument
    ) {
        final String fileName = "SomePfrXmlReport.xml";
        PfrReportDraftsBuilderDocumentFileContents contents = new PfrReportDraftsBuilderDocumentFileContents();
        PfrReportDraftsBuilderDocumentFileMetaRequest meta = new PfrReportDraftsBuilderDocumentFileMetaRequest();

        String scannedContent = getPfrReportContent();

        byte[] signature = cryptoUtils.sign(
                engine.getConfiguration().getThumbprint(),
                Base64.getDecoder().decode(scannedContent)
        );

        contents.setBase64Content(scannedContent);
        contents.setBase64SignatureContent(Base64.getEncoder().encodeToString(signature));

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

    public String getPfrReportContent() {
        URL contentUrl = DraftsBuilderDocumentFileCreator.class
                .getClassLoader()
                // Почему русские пути не сработали? TODO
                //.getResource("docs/pfr/ПФР_000-004-872962_333444_СЗВ-М_20190414_F6C22442-5774-41AC-81E6-BA30C229D5A4.xml");
                .getResource("docs/pfr/SomePfrReport.xml");
                //.getResource("docs/ON_DOCNPNO_6653000832665325934_6653000832665325934_0087.xml");

        String contentPath = new File(Objects.requireNonNull(contentUrl).getFile()).getAbsolutePath();

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(contentPath));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find scanned file in resources");
        }
    }
}
