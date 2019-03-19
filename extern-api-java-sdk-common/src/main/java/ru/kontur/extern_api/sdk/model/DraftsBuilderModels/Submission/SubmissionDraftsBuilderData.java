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

package ru.kontur.extern_api.sdk.model.DraftsBuilderModels.Submission;

import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderData;
import ru.kontur.extern_api.sdk.model.RelatedDocumentRequest;

public class SubmissionDraftsBuilderData extends
        DraftsBuilderData {

    private String claimItemNumber;
    private RelatedDocumentRequest relatedDocument;

    /**
     * Возвращает пункт требования
     *
     * @return пункт требования
     */
    public String getClaimItemNumber() {
        return claimItemNumber;
    }

    /**
     * Устанавливает пункт требования
     *
     * @param claimItemNumber пункт требования
     */
    public void setClaimItemNumber(String claimItemNumber) {
        this.claimItemNumber = claimItemNumber;
    }

    /**
     * Возвращает связный ДО
     *
     * @return связный ДО
     */
    public RelatedDocumentRequest getRelatedDocument() {
        return relatedDocument;
    }

    /**
     * Устанавливает связный ДО
     *
     * @param relatedDocument связный ДО
     */
    public void setRelatedDocument(RelatedDocumentRequest relatedDocument) {
        this.relatedDocument = relatedDocument;
    }
}
