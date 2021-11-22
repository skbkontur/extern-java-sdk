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

package ru.kontur.extern_api.sdk.docflows;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static ru.kontur.extern_api.sdk.common.StandardObjectsValidator.validateId;

import org.junit.jupiter.api.Assertions;
import ru.kontur.extern_api.sdk.common.StandardObjectsValidator;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.model.Content;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.DocflowPageItem;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentType;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DocflowType;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowsValidator {

    public static void validateDocflowPage(DocflowPage docflowPage, boolean withDocflowsPageItems) {
        assertNotNull("DocflowPage must not be null!", docflowPage);
        assertEquals("Skip is wrong!", 0, docflowPage.getSkip().longValue());
        assertEquals("Take is wrong!", 0, docflowPage.getTake().longValue());
        assertEquals("TotalCount is wrong!", 0, docflowPage.getTotalCount().longValue());
        if (withDocflowsPageItems) {
            StandardObjectsValidator
                .validateNotEmptyList(docflowPage.getDocflowsPageItem(), "DocflowsPageItems");
            validateDocflowPageItem(docflowPage.getDocflowsPageItem().get(0));
        } else {
            StandardObjectsValidator
                .validateEmptyList(docflowPage.getDocflowsPageItem(), "DocflowsPageItems");
        }
    }

    private static void validateDocflowPageItem(DocflowPageItem docflowPageItem) {
        assertNotNull("DocflowPageItem must not be null!", docflowPageItem);
        validateId(docflowPageItem.getId());
        assertEquals("Type is wrong!", DocflowType.FNS534_REPORT, docflowPageItem.getType());
        assertEquals("Status is wrong!", DocflowStatus.SENT, docflowPageItem.getStatus());
        assertEquals("SendDate is wrong!", StandardValues.standardDate(),
            docflowPageItem.getSendDate());
        assertEquals("LastChangeDate is wrong!", StandardValues.standardDate(),
            docflowPageItem.getLastChangeDate());

        StandardObjectsValidator.validateEmptyList(docflowPageItem.getLinks(), "Links");
    }

    public static void validateDocflow(Docflow docflow, boolean withDescription,
        boolean withDocuments, boolean withLinks) {
        assertNotNull("Docflow must not be null!", docflow);
        validateId(docflow.getId());
        assertEquals("Type is wrong!", DocflowType.STAT_LETTER, docflow.getType());
        assertEquals("Status is wrong!", DocflowStatus.ARRIVED, docflow.getStatus());
        assertEquals("SendDate is wrong!", StandardValues.standardDate(),
            docflow.getSendDate());
        assertEquals("LastChangeDate is wrong!", StandardValues.standardDate(),
            docflow.getLastChangeDate());

        if (withDescription) {
            assertNotNull("Description must not be null!", docflow.getDescription());
        }

        if (withDocuments) {
            StandardObjectsValidator.validateNotEmptyList(docflow.getDocuments(), "Documents");
            validateDocument(docflow.getDocuments().get(0), false, false, false, false);
        } else {

            assertTrue("Documents must be empty!", docflow.getDocuments().isEmpty());
        }

        if (withLinks) {
            StandardObjectsValidator.validateNotEmptyList(docflow.getLinks(), "Links");
            StandardObjectsValidator.validateLink(docflow.getLinks().get(0));
        } else {
            assertTrue("Links must be empty!", docflow.getLinks().isEmpty());
        }

    }

    public static void validateDocument(Document document, boolean withDescription,
        boolean withContent, boolean withSignature, boolean withLinks) {
        assertNotNull("Document must not be null!", document);
        validateId(document.getId());

        if (withDescription) {
            validateDocumentDescription(document.getDescription());
        } else {
            assertNull("DocumentDescription must be null!", document.getDescription());
        }

//        TODO: Поправить тест под новый DocflowDocumentContents
//        if (withContent) {
//            validateContent(document.getContent());
//        } else {
//            assertNull("Content must be null!", document.getContent());
//        }

        if (withSignature) {
            StandardObjectsValidator.validateNotEmptyList(document.getSignatures(), "Signatures");
            validateSignature(document.getSignatures().get(0), false, false);
        } else {
            StandardObjectsValidator.validateEmptyList(document.getSignatures(), "Signatures");
        }

        if (withLinks) {
            StandardObjectsValidator.validateNotEmptyList(document.getLinks(), "Links");
            StandardObjectsValidator.validateLink(document.getLinks().get(0));
        } else {
            StandardObjectsValidator.validateEmptyList(document.getLinks(), "Links");
        }
    }

    public static void validateDocumentDescription(DocflowDocumentDescription documentDescription) {
        assertNotNull("DocflowDocumentDescription must not be null!", documentDescription);
        assertEquals("Type is wrong!", DocumentType.Fns534Report, documentDescription.getType());
        assertEquals("Filename is wrong!", "string", documentDescription.getFilename());
        assertEquals("ContentType is wrong!", "string", documentDescription.getContentType());
        assertEquals("Compressed is wrong!", (Object) true, documentDescription.getCompressed());
    }

//        TODO: Поправить тест под новый DocflowDocumentContents
//    private static void validateContent(Content content) {
//        assertNotNull("Content must not be null!", content);
//        StandardObjectsValidator.validateLink(content.getDecrypted());
//        StandardObjectsValidator.validateLink(content.getEncrypted());
//    }

    public static void validateSignature(Signature signature, boolean withContentLink,
        boolean withLinks) {
        assertNotNull("Signature must not be null!", signature);
        StandardObjectsValidator.validateId(signature.getId());

        if (withContentLink) {
            Link link = signature.getContentLink();
            assertNotNull("Content must not be null", link);
            StandardObjectsValidator.validateLink(link);
        } else {
            assertNull("ContentLink must be null", signature.getContentLink());
        }

        if (withLinks) {
            StandardObjectsValidator.validateNotEmptyList(signature.getLinks(), "Links");
            StandardObjectsValidator.validateLink(signature.getLinks().get(0));
        } else {
            StandardObjectsValidator.validateEmptyList(signature.getLinks(), "Links");
        }
    }
}
