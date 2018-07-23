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

/**
 * @author AlexS
 *
 * Класс содержит данные, описывающие документ, для отправки.
 * Используется в сервисе {@code DraftService}
 */
public class DocumentDescription {

    private String type = null;
    private String filename = null;
    @SerializedName("content-type")
    private String contentType = null;

    public DocumentDescription type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Возвращает тип документа. Могут быть следующие типы документов для отправки:
     * <li>urn:document:fns534-report - декларация для ФНС</li>
     * <li>urn:document:fns534-report-warrant - доверенность</li>
     * <li>urn:document:fns534-report-attachment - приложение к декларации для ФНС</li>
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип документа. Могут быть следующие типы документов для отправки:
     * <li>urn:document:fns534-report - декларация для ФНС</li>
     * <li>urn:document:fns534-report-warrant - доверенность</li>
     * <li>urn:document:fns534-report-attachment - приложение к декларации для ФНС</li>
     * @param type тип документа
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Устанавливает имя файла декларации.
     * Метод пытается угадать тип отправляемого документа по его имени файла
     * @param filename имя файла декларации
     * @return {@link DocumentDescription}
     */
    public DocumentDescription filename(String filename) {
        setFilename(filename);
        return this;
    }

    /**
     * Возвращает имя файла декларации
     * @return filename имя файла декларации
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Устанавливает имя файла декларации.
     * Метод пытается угадать тип отправляемого документа по его имени файла
     * @param filename имя файла декларации
     */
    public void setFilename(String filename) {
        this.filename = filename;
        // пытаемся угадать тип
        this.type = Type.guessType(filename).value;
    }

    /**
     * Устанавливает тип контента (content-type) документа
     * @param contentType тип контента (content-type) документа
     * @return {@link DocumentDescription}
     */
    public DocumentDescription contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Возвращает тип контента (content-type) документа
     * @return contentType тип контента (content-type) документа
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Устанавливает тип контента (content-type) документа
     * @param contentType тип контента (content-type) документа
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Содержит доступное перечисления типов документов
     */
    private enum Type {
        /** декларация ФНС */
        DEKL("urn:document:fns534-report", "^(?i)NO_(\\w+)_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        /** доверенность для ФНС */
        DOV("urn:document:fns534-report-warrant", "^(?i)ON_DOV_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        /** приложение к декларации ФНС */
        APP("urn:document:fns534-report-attachment", ""),
        /** неизвестный тип */
        UNKNOWN(null, "");

        private final String value;
        private final String pattern;

        Type(String value, String pattern) {
            this.value = value;
            this.pattern = pattern;
        }

        private static Type guessType(String fileName) {
            if (fileName == null) {
                return UNKNOWN;
            }

            if (fileName.matches(DOV.pattern)) {
                return DOV;
            } else if (fileName.matches(DEKL.pattern)) {
                return DEKL;
            } else {
                return APP;
            }
        }
    }
}
