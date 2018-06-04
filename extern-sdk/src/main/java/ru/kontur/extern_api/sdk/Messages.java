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

package ru.kontur.extern_api.sdk;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * @author AlexS
 */
public class Messages {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String S_AUTHORIZATION_BY_LOGIN = "S_AUTHORIZATION_BY_LOGIN";
    public static final String S_SERVER_ERROR = "S_SERVER_ERROR";
    public static final String S_ENTITY_NOT_FOUND = "S_ENTITY_NOT_FOUND";
    public static final String C_CONFIG_NOT_FOUND = "C_CONFIG_NOT_FOUND";
    public static final String C_CONFIG_LOAD = "C_CONFIG_LOAD";
    public static final String C_CRYPTO_ERROR = "C_CRYPTO_ERROR";
    public static final String C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER = "C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER";
    public static final String C_CRYPTO_ERROR_INIT = "C_CRYPTO_ERROR_INIT";
    public static final String C_CRYPTO_ERROR_KEY_NOT_FOUND = "C_CRYPTO_ERROR_KEY_NOT_FOUND";
    public static final String C_RESOURCE_NOT_FOUND = "C_RESOURCE_NOT_FOUND";
    public static final String C_NO_SIGNATURE = "C_NO_SIGNATURE";
    public static final String C_NO_DECRYPT = "C_NO_DECRYPT";
    public static final String C_NO_USER_AGENT_PROVIDER = "C_NO_USER_AGENT_PROVIDER";

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("Messages", Locale.getDefault());

    public static String get(String key) {
        try {
            return MESSAGES.getString(key);
        } catch (RuntimeException x) {
            return MessageFormat.format("Resourse key [{0}] not found.", key);
        }
    }

    public static String get(String key, Object... p) {
        return MessageFormat.format(get(key), p);
    }
}
