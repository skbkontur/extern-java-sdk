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

package ru.kontur.extern_api.sdk.model.builders.fns.inventory;

import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentData;

public class FnsInventoryDraftsBuilderDocumentData extends
        DraftsBuilderDocumentData {

    private String claimItemNumber;
    private String scannedDocumentDate;
    private String scannedDocumentNumber;
    private DraftsBuilderDocumentType type;

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
     * Возвращает дату документа
     *
     * @return дата документа
     */
    public String getScannedDocumentDate() {
        return scannedDocumentDate;
    }

    /**
     * Устанавливает дату документа
     *
     * @param scannedDocumentDate дата документа
     */
    public void setScannedDocumentDate(String scannedDocumentDate) {
        this.scannedDocumentDate = scannedDocumentDate;
    }

    /**
     * Возвращает номер документа
     *
     * @return номер документа
     */
    public String getScannedDocumentNumber() {
        return scannedDocumentNumber;
    }

    /**
     * Устанавливает номер документа
     *
     * @param scannedDocumentNumber номер документа
     */
    public void setScannedDocumentNumber(String scannedDocumentNumber) {
        this.scannedDocumentNumber = scannedDocumentNumber;
    }

    /**
     * Возвращает тип документа
     *
     * @return тип документа
     */
    public DraftsBuilderDocumentType getType() {
        return type;
    }

    /**
     * Устанавливает тип документа
     *
     * @param type тип документа
     */
    public void setType(DraftsBuilderDocumentType type) {
        this.type = type;
    }

    private enum DraftsBuilderDocumentType {
        Formalized,
        Scanned,
        Warrant
    }
}
