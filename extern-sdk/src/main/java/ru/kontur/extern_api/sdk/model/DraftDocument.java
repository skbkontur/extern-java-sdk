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

package ru.kontur.extern_api.sdk.model;

import java.util.UUID;


/**
 * @author AlexS
 */
public class DraftDocument {

    private UUID id = null;
    private Link decryptedContentLink = null;
    private Link encryptedContentLink = null;
    private Link signatureContentLink = null;
    private DocumentDescription documentDescription = null;

    /**
     * Get id
     *
     * @return id
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Get decryptedContentLink
     *
     * @return decryptedContentLink
     */
    public Link getDecryptedContentLink() {
        return decryptedContentLink;
    }

    public void setDecryptedContentLink(Link decryptedContentLink) {
        this.decryptedContentLink = decryptedContentLink;
    }

    /**
     * Get encryptedContentLink
     *
     * @return encryptedContentLink
     */
    public Link getEncryptedContentLink() {
        return encryptedContentLink;
    }

    public void setEncryptedContentLink(Link encryptedContentLink) {
        this.encryptedContentLink = encryptedContentLink;
    }

    /**
     * Get signatureContentLink
     *
     * @return signatureContentLink
     */
    public Link getSignatureContentLink() {
        return signatureContentLink;
    }

    public void setSignatureContentLink(Link signatureContentLink) {
        this.signatureContentLink = signatureContentLink;
    }

    /**
     * Get meta
     *
     * @return meta
     */
    public DocumentDescription getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(DocumentDescription documentDescription) {
        this.documentDescription = documentDescription;
    }
}
