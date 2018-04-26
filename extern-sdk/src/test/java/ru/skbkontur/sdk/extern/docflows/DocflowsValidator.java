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

package ru.skbkontur.sdk.extern.docflows;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static ru.skbkontur.sdk.extern.common.StandardObjectsValidator.validateId;

import org.joda.time.DateTime;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.model.Content;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.DocflowPage;
import ru.skbkontur.sdk.extern.model.DocflowPageItem;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.model.Signature;

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
        assertEquals("Type is wrong!", "urn:nss:nid", docflowPageItem.getType());
        assertEquals("Status is wrong!", "urn:nss:nid", docflowPageItem.getStatus());
        assertEquals("SendDate is wrong!", new DateTime(StandardValues.DATE),
            docflowPageItem.getSendDate());
        assertEquals("LastChangeDate is wrong!", new DateTime(StandardValues.DATE),
            docflowPageItem.getLastChangeDate());

        StandardObjectsValidator.validateEmptyList(docflowPageItem.getLinks(), "Links");
    }

    public static void validateDocflow(Docflow docflow, boolean withDescription,
        boolean withDocuments, boolean withLinks) {
        assertNotNull("Docflow must not be null!", docflow);
        validateId(docflow.getId());
        assertEquals("Type is wrong!", "urn:nss:nid", docflow.getType());
        assertEquals("Status is wrong!", "urn:nss:nid", docflow.getStatus());
        assertEquals("SendDate is wrong!", new DateTime(StandardValues.DATE),
            docflow.getSendDate());
        assertEquals("LastChangeDate is wrong!", new DateTime(StandardValues.DATE),
            docflow.getLastChangeDate());

        if (withDescription) {
            assertNotNull("Description must not be null!", docflow.getDescription());
        }

        if (withDocuments) {
            StandardObjectsValidator.validateNotEmptyList(docflow.getDocuments(), "Documents");
            validateDocument(docflow.getDocuments().get(0), false, false, false, false);
        } else {
            assertNull("Documents must be null!", docflow.getDocuments());
        }

        if (withLinks) {
            StandardObjectsValidator.validateNotEmptyList(docflow.getLinks(), "Links");
            StandardObjectsValidator.validateLink(docflow.getLinks().get(0));
        } else {
            assertNull("Links must be null!", docflow.getLinks());
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

        if (withContent) {
            validateContent(document.getContent());
        } else {
            assertNull("Content must be null!", document.getContent());
        }

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

    public static void validateDocumentDescription(DocumentDescription documentDescription) {
        assertNotNull("DocumentDescription must not be null!", documentDescription);
        assertEquals("Type is wrong!", "urn:nss:nid", documentDescription.getType());
        assertEquals("Filename is wrong!", "string", documentDescription.getFilename());
        assertEquals("ContentType is wrong!", "string", documentDescription.getContentType());
    }

    private static void validateContent(Content content) {
        assertNotNull("Content must not be null!", content);
        StandardObjectsValidator.validateLink(content.getDecrypted());
        StandardObjectsValidator.validateLink(content.getEncrypted());
    }

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
