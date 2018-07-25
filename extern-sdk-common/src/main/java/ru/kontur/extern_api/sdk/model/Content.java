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
 * <p>
 * Класс ДТО для получения ссылок на расшифрованный и зашифрованный контенты документов документооборота.
 * Объект данного класса инкопсулированы в объект {@link Document}. Используется в методах {@code DocflowService}.
 * </p>
 */
public class Content {

    private Link decrypted = null;
    private Link encrypted = null;

    /**
     * Устанавливает ссылку на расшифрованный контент
     * @param decrypted ссылка на расшифрованный контент
     * @return {@link Content}
     */
    public Content decrypted(Link decrypted) {
        this.decrypted = decrypted;
        return this;
    }

    /**
     * Возвращает ссылку на расшифрованный контент
     * @return decrypted ссылка на расшифрованный контент
     */
    public Link getDecrypted() {
        return decrypted;
    }

    /**
     * Устанавливает ссылку на расшифрованный контент
     * @param decrypted ссылка на расшифрованный контент
     */
    public void setDecrypted(Link decrypted) {
        this.decrypted = decrypted;
    }

    /**
     * Устанавливает ссылку на зашифрованный контент
     * @param encrypted ссылка на зашифрованный контент
     * @return {@link Content}
     */
    public Content encrypted(Link encrypted) {
        this.encrypted = encrypted;
        return this;
    }

    /**
     * Возвращает ссылку на зашифрованный контент
     * @return encrypted ссылка на зашифрованный контент
     */
    public Link getEncrypted() {
        return encrypted;
    }

    /**
     * Устанавливает ссылку на зашифрованный контент
     * @param encrypted ссылка на зашифрованный контент
     */
    public void setEncrypted(Link encrypted) {
        this.encrypted = encrypted;
    }
}
