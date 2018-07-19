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
 */

package ru.kontur.extern_api.sdk;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Pavlenko
 */

public class ServiceLogger {

    @Nullable
    private static ServiceLogger instance;

    @NotNull
    private final Logger logger = LogManager.getLogger(ServiceLogger.class);

    private ServiceLogger() {
    }

    public static ServiceLogger getInstance() {
        if (instance == null) {
            instance = new ServiceLogger();
        }
        return instance;
    }

    public void logHttpRequest(
        // input parameters
        String httpRequestUri, String httpMethod, Map<String, Object> queryParams, Object body,
        Map<String, String> headerParams, Map<String, Object> formParams, Type type,
        // output parameters
        int statusCode, Map<String, List<String>> headers, Object data) {

        String httpRequestLog = String.format("START LOGGING HTTP REQUEST\n"
                + "REQUEST\n"
                + "httpRequestUri: %s\n"
                + "httpMethod: %s\n"
                + "queryParams: %s\n"
                + "body: %s\n"
                + "headerParams: %s\n"
                + "formParams: %s\n"
                + "type: %s\n"
                + "RESPONSE\n"
                + "statusCode: %s\n"
                + "headers: %s\n"
                + "data: %s\n"
                + "END LOGGING HTTP REQUEST",
            httpRequestUri, httpMethod, getParamsAsString(queryParams), body,
            getParamsAsString(headerParams), getParamsAsString(formParams), type.getTypeName(),
            statusCode, getParamsAsString(headers), data);

        logger.log(Level.INFO, httpRequestLog);
    }

    @NotNull
    private <A, B> String getParamsAsString(@Nullable Map<A, B> queryParams) {
        if (queryParams == null) {
            return "";
        }
        List<String> listOfEntry = new ArrayList<>();
        for (Entry<A, B> entry : queryParams.entrySet()) {
            listOfEntry.add(entry.getKey() + "->" + entry.getValue());
        }

        return String.join(", ", listOfEntry);
    }


}
