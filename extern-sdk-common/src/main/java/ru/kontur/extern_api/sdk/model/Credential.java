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

/**
 * @author AlexS
 *
 * Класс предназначен для отправки данных, предоставляющие информацию о личности.
 * Как правило, это личный номер тедефона, так-же может быть СНИЛС и т.д.
 * Используется для доверительной аутентификации, см. {@code TrustedAuthentication}
 */
public class Credential {

    private String name;
    private String value;

    public Credential(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Возвращает наименование параметра для идентификации личности
     * @return наименование параметра для идентификации личности
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает наименование параметра для идентификации личности
     * @param name наименование параметра для идентификации личности
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает значение параметра, устанавливающий личность пользователя
     * @return значение параметра, устанавливающий личность пользователя
     */
    public String getValue() {
        return value;
    }

    /**
     * Устанавливает значение параметра, устанавливающий личность пользователя
     * @param value значение параметра, устанавливающий личность пользователя
     */
    public void setValue(String value) {
        this.value = value;
    }
}
