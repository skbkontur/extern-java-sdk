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

package ru.kontur.extern_api.sdk.provider;

import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * <p>
 * Интерфейс предоставляет аутентификатор. Каждый запрос, отправляемый к сервисам СКБ Контур,
 * должен сопровождаться идентификатором аутентификационной сессии. Аутентифицироваться можно:
 * </p>
 * <ul>
 *     <li>по логину и паролю, для этого в SDK есть класс AuthenticationProviderByPass;</li>
 *     <li>с помощью механизмов доверительной аутентификации, для этого в SDK реализован класс TrustedAuthentication;</li>
 *     <li>с помощью сертификата личного ключа, для этого в SDK предназначен класс CertificateAuthenticationProvider.</li>
 * </ul>
 *
 * @author Aleksey Sukhorukov
 */
public interface AuthenticationProvider {

    QueryContext<String> sessionId();

    String authPrefix();

    AuthenticationProvider httpClient(HttpClient httpClient);
    
    void addAuthenticationListener(AuthenticationListener authListener);

    void removeAuthenticationListener(AuthenticationListener authListener);

    void raiseUnauthenticated(ServiceError x);

    String userIP();
}
