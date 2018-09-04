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

package ru.kontur.extern_api.sdk.httpclient.retrofit.api;

import java.util.concurrent.CompletableFuture;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.kontur.extern_api.sdk.model.EventsPage;


public interface EventsApi {

    /**
     * Shows docflow events for all users and accounts connected with external service (e.g.: bank)
     *
     * @param fromId Event Id offset from which data is read out (required). {@code 0_0} - zero
     * offset. next offsets will be returned
     * @param batchSize Max count of events to be returned (default - 100)
     * @return ApiResponse&lt;EventsPage&gt;
     */
    @GET("v1/events")
    CompletableFuture<Response<EventsPage>> getEvents(
            @Query("fromId") String fromId,
            @Query("batchSize") Integer batchSize
    );
}
