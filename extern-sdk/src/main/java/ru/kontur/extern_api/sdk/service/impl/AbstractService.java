/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.provider.ProviderHolderParent;
import ru.kontur.extern_api.sdk.service.ServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

public class AbstractService implements ProviderHolderParent<ServicesFactory> {

    private final ServicesFactory servicesFactory;

    public AbstractService(ServicesFactory servicesFactory) {
        this.servicesFactory = servicesFactory;
    }

    protected <T> QueryContext<T> createQueryContext(String entityName) {
        return createQueryContext(new QueryContext<>(), entityName);
    }

    protected <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
        QueryContext<T> cxt = new QueryContext<T>(parent, entityName)
                .setServiceBaseUriProvider(getServiceBaseUriProvider())
                .setAccountProvider(getAccountProvider())
                .setAuthenticationProvider(getAuthenticationProvider())
                .setApiKeyProvider(getApiKeyProvider());

        return acquireSessionId(cxt);
    }

    private <T> QueryContext<T> acquireSessionId(QueryContext<T> cxt) {
        String sessionId = cxt.getSessionId();
        if (!YAStringUtils.isNullOrEmpty(sessionId)) {
            return cxt;
        }

        if (getAuthenticationProvider() == null) {
            return cxt.setServiceError(ServiceError.ErrorCode.unknownAuth);
        }

        QueryContext<String> authQuery = getAuthenticationProvider().sessionId();
        if (authQuery.isFail()) {
            return cxt.setServiceError(authQuery);
        }

        return cxt
                .setSessionId(authQuery.get())
                .setAuthPrefix(getAuthenticationProvider().authPrefix());
    }

    @Override
    public ServicesFactory getChildProviderHolder() {
        return servicesFactory;
    }
}
