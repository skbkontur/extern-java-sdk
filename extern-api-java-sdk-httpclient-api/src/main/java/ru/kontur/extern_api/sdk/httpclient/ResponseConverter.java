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

package ru.kontur.extern_api.sdk.httpclient;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Optional;
import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.model.ErrorInfo;

final class ResponseConverter {

    private final Gson gson;

    ResponseConverter(Gson gson) {
        this.gson = gson;
    }

    <T> ApiResponse<T> toApiResponse(Response<T> response) {
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
            String string = body.string();
            ErrorInfo errorInfo = gson.fromJson(string, ErrorInfo.class);

            if (errorInfo.getId() == null) {
                // all errors from public should be ErrorInfo-like
                errorInfo.setId("not-an-error-info");
                errorInfo.setMessage(string);
            }

            return errorInfo;
        } catch (JsonSyntaxException | NullPointerException | IOException e) {
            ErrorInfo ei = new ErrorInfo();
            ei.setId("invalid-error-info");
            ei.setStatusCode(response.code());
            ei.setMessage(response.message());
            ei.setThrowable(e);
            return ei;
        }
    }

}
