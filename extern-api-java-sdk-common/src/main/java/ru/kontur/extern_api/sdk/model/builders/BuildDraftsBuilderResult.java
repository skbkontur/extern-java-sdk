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

package ru.kontur.extern_api.sdk.model.builders;

import java.util.Map;
import java.util.UUID;

public class BuildDraftsBuilderResult {

    private UUID[] draftIds;
    private BuildDraftsBuilderResultError[] errorDraftsBuilderDocuments;

    /**
     * Возвращает идентификаторы созданных черновиков
     *
     * @return идентификаторы созданных черновиков
     */
    public UUID[] getDraftIds() {
        return draftIds;
    }

    /**
     * Устанавливает идентификаторы созданных черновиков
     *
     * @param draftIds идентификаторы созданных черновиков
     */
    public void setDraftIds(UUID[] draftIds) {
        this.draftIds = draftIds;
    }

    /**
     * Возвращает список ошибок по документам
     *
     * @return список ошибок по документам
     */
    public BuildDraftsBuilderResultError[] getErrorDraftsBuilderDocuments() {
        return errorDraftsBuilderDocuments;
    }

    /**
     * Устанавливает список ошибок по документам
     *
     * @param errorDraftsBuilderDocuments список ошибок по документам
     */
    public void setErrorDraftsBuilderDocuments(BuildDraftsBuilderResultError[] errorDraftsBuilderDocuments) {
        this.errorDraftsBuilderDocuments = errorDraftsBuilderDocuments;
    }
}
