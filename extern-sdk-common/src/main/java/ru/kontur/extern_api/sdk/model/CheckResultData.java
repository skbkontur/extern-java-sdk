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

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author AlexS
 *
 * Класс предназначен для получения информации о результатах проверки черновика перед отправкой.
 * Используется в методах: {@code DraftService.check} и {@code DraftService.prepare}
 * @see CheckError
 */
public class CheckResultData {

    @SerializedName("documents-errors")
    private Map<String, List<CheckError>> documentsErrors = null;
    @SerializedName("common-errors")
    private List<CheckError> commonErrors = null;

    public CheckResultData documentsErrors(Map<String, List<CheckError>> documentsErrors) {
        this.documentsErrors = documentsErrors;
        return this;
    }

    /**
     * Возвращает карту для списков ошибок по документам (например ошибки ФЛК)
     * @return documentsErrors карта для списков ошибок
     * @see CheckError
     */
    public Map<String, List<CheckError>> getDocumentsErrors() {
        return documentsErrors;
    }

    /**
     * Устанавливает карту для списков ошибок по документам (например ошибки ФЛК)
     * @param documentsErrors карту для списков ошибок по документам
     */
    public void setDocumentsErrors(Map<String, List<CheckError>> documentsErrors) {
        this.documentsErrors = documentsErrors;
    }

    /**
     * Устанавливает список ошибок по результату проверки черновика
     * @param commonErrors список ошибок по результату проверки черновика
     * @return {@link CheckResultData}
     */
    public CheckResultData commonErrors(List<CheckError> commonErrors) {
        this.commonErrors = commonErrors;
        return this;
    }

    /**
     * Дщбавляет ошибку в список
     * @param commonErrorsItem ошибка {@link CheckError}
     * @return {@link CheckResultData}
     */
    public CheckResultData addCommonErrorsItem(CheckError commonErrorsItem) {
        if (this.commonErrors == null) {
            this.commonErrors = new ArrayList<>();
        }
        this.commonErrors.add(commonErrorsItem);
        return this;
    }

    /**
     * Возвращает список ошибок по результату проверки черновика
     * @return commonErrors список ошибок
     * @see CheckError
     */
    public List<CheckError> getCommonErrors() {
        return commonErrors;
    }

    /**
     * Устанавливает список ошибок по результату проверки черновика
     * @param commonErrors список ошибок
     */
    public void setCommonErrors(List<CheckError> commonErrors) {
        this.commonErrors = commonErrors;
    }
}
