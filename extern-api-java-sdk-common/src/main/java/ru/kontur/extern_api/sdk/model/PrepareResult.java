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
import java.util.List;


/**
 * <p>Класс предназначен для сохранения результа проверки перед выполнением подготовки данных к
 * отправке (шифрования, упаковки, проверка подписи и др.)</p>
 *
 * <p>см. метод DraftService.prepare</p>
 */
public class PrepareResult {

    private CheckResultData checkResult = null;
    private List<Link> links = null;
    private Status status = null;

    /**
     * Возвращает результат проверки {@link CheckResultData}
     *
     * @return результат проверки
     * @see CheckResultData
     */
    public CheckResultData getCheckResult() {
        return checkResult;
    }

    /**
     * Устанавливает результат проверки {@link CheckResultData}
     *
     * @param checkResult результат проверки
     */
    public void setCheckResult(CheckResultData checkResult) {
        this.checkResult = checkResult;
    }

    /**
     * Возвращает список ссылок на документы
     *
     * @return список ссылок на документы
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок на документы
     *
     * @param links список ссылок на документы
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * @return результат проверки
     */
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {

        @SerializedName("check-protocol-has-errors")
        CHECK_PROTOCOL_HAS_ERRORS,

        @SerializedName("check-protocol-has-only-warnings")
        CHECK_PROTOCOL_HAS_ONLY_WARNINGS,

        @SerializedName("encryption-failed")
        ENCRYPTION_FAILED,

        @SerializedName("ok")
        OK;

    }

}
