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
 * <p>
 * Класс предназначен для отправки данных, предоставляющие информацию о личности.
 * Как правило, это личный номер тедефона, так-же может быть СНИЛС, отпечпток сертификата и т.д.
 * Используется для доверительной аутентификации, см. {@code TrustedAuthentication}
 * </p>
 *
 * @author Aleksey Sukhorukov
 */
public class Credential {

    private String name;
    private String value;

    public Credential(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * <p>Возвращает наименование параметра для идентификации пользователя. Должен возвращать одно из следующих значений:</p>
     * <ul>
     * <li>thumbprint - отпечаток сертификата пользователя</li>
     * <li>login - логин пользователя</li>
     * <li>phone - номер телефона пользователя</li>
     * <li>snils - СНИЛС пользователя</li>
     * <li>serviceUserId - идентификатор пользователя</li>
     * </ul>
     *
     * @return наименование параметра для идентификации личности
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Устанавливает наименование параметра для идентификации пользователя</p>
     *
     * @param name наименование параметра для идентификации пользователя. Может принимать одно из следующих значений:
     *             <ul>
     *             <li>thumbprint - отпечаток сертификата пользователя</li>
     *             <li>login - логин пользователя</li>
     *             <li>phone - номер телефона пользователя</li>
     *             <li>snils - СНИЛС пользователя</li>
     *             <li>serviceUserId - идентификатор пользователя</li>
     *             </ul>
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Возвращает значение параметра, устанавливающий личность пользователя</p>
     *
     * @return значение параметра, устанавливающий личность пользователя
     */
    public String getValue() {
        return value;
    }

    /**
     * Устанавливает значение параметра, устанавливающий личность пользователя
     *
     * @param value значение параметра, устанавливающий личность пользователя
     */
    public void setValue(String value) {
        this.value = value;
    }
}
