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

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;

@DisplayName("retrofit response")
class ResponseConverterTest {

    @Test
    @DisplayName("`Ok` should be parsed to api response")
    void retrofitOk() {

        LibapiResponseConverter apiUtils = new LibapiResponseConverter();

        Response<Object> response = Response.success(null);

        ApiResponse<Object> apiResponse = apiUtils.toApiResponse(GsonProvider.getLibapiCompatibleGson(), response);

        Assertions.assertEquals(200, apiResponse.getStatusCode());
        Assertions.assertNull(apiResponse.getData());
    }

    @Test
    @DisplayName("`ErrorInfo` should be parsed to response with error")
    void retrofitError() {

        LibapiResponseConverter apiUtils = new LibapiResponseConverter();

        ResponseBody errorBody = ResponseBody.create(MediaType.parse("application/json"), "{"
                + "  'id': 'urn:nss:nid',"
                + "  'status-code': 404,"
                + "  'message': 'string',"
                + "  'track-id': 'string',"
                + "  'trace-id': 'string',"
                + "  'properties': {}"
                + "}");

        Response<Object> response = Response.error(404, errorBody);

        ApiResponse<Object> apiResponse = apiUtils.toApiResponse(GsonProvider.getLibapiCompatibleGson(), response);

        Assertions.assertEquals(404, apiResponse.getStatusCode());
        Assertions.assertNotNull(apiResponse.getErrorInfo());
        Assertions.assertEquals("string", apiResponse.getErrorInfo().getTraceId());
    }
}
