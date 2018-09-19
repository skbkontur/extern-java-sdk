/*
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
 *
 */

package ru.kontur.extern_api.sdk.httpclient.retrofit;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.model.ErrorInfo;

public final class ApiUtils {

    private final Gson gson;

    public ApiUtils(Gson gson) {
        this.gson = gson;
    }

    public <T> ApiResponse<T> toApiResponse(Response<T> response) {
        if (response.isSuccessful()) {
            return new ApiResponse<>(
                    response.code(),
                    response.headers().toMultimap(),
                    response.body());
        }

        ErrorInfo errorInfo = Optional
                .ofNullable(response.errorBody())
                .map(body -> errorInfoFromBody(response, body))
                .orElseGet(() -> {
                    ErrorInfo ei = new ErrorInfo();
                    ei.setStatusCode(response.code());
                    ei.setMessage(response.message());
                    return ei;
                });

        return new ApiResponse<>(
                response.code(),
                response.headers().toMultimap(),
                errorInfo
        );
    }


    private <T> ErrorInfo errorInfoFromBody(Response<T> response, ResponseBody body) {
        try {
            return gson.fromJson(body.string(), ErrorInfo.class);
        } catch (IOException e) {
            ErrorInfo ei = new ErrorInfo();
            ei.setStatusCode(response.code());
            ei.setMessage(response.message());
            ei.setThrowable(e);
            return ei;
        }
    }


    static URI changeUriQuery(URI uri, String newQuery) throws IOException {
        try {
            return new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    newQuery,
                    uri.getFragment()
            );
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    static String extendQuery(String query, String paramName, String paramValue) {
        String param = paramName + "=" + paramValue;
        return Optional
                .ofNullable(query)
                .map(s -> s + "&" + param)
                .orElse(param);

    }
}