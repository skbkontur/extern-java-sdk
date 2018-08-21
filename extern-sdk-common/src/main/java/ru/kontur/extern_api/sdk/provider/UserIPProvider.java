/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Aleksey Sukhorukov
 */

package ru.kontur.extern_api.sdk.provider;

import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * Интерфейс предоставляет IPV4 адрес компьютера отправителя. По умолчанию в <b>ExternEngine</b> будет установлен провайдер,
 * который возвращает IP адрес локального компьютера. Для замены провайдера необходимо воспользоваться методом {@code ExternEngine.setUserIPProvider}. При отправке черновка Контур Экстерн производит валидацию значения IP адреса. IP адрес не должен быть локальным, см. <a href="https://tools.ietf.org/html/rfc5735#page-6">RFC 5735</a>.
 * @author Aleksey Sukhorukov
 */
public interface UserIPProvider {
    QueryContext<String> userIP();
}
