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
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import java.util.stream.Stream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class GsonCustomConverterFactory extends Converter.Factory {

    private final GsonConverterFactory gsonConverterFactory;

    public static GsonCustomConverterFactory create(Gson gson) {
        return new GsonCustomConverterFactory(gson);
    }

    private GsonCustomConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gsonConverterFactory = GsonConverterFactory.create(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type,
            Annotation[] annotations,
            Retrofit retrofit
    ) {
        boolean rawBodyRequired = Stream
                .of(annotations)
                .map(Annotation::annotationType)
                .anyMatch(Predicate.isEqual(Raw.class));

        if (rawBodyRequired) {
            if (type.equals(String.class)) {
                return ResponseBody::string;
            }

            if (type.equals(byte[].class)) {
                return ResponseBody::bytes;
            }

            throw new IllegalStateException
                    ("@Raw annotation allowed only with `String` or `byte[]` response types");
        }

        return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit
    ) {

        boolean rawBodyRequired = Stream
                .of(parameterAnnotations)
                .map(Annotation::annotationType)
                .anyMatch(Predicate.isEqual(Raw.class));

        if (rawBodyRequired) {
            if (type.equals(String.class)) {
                return b -> RequestBody.create(MediaType.parse("text/plain"), (String) b);
            }

            if (type.equals(byte[].class)) {
                return b -> RequestBody.create(MediaType.parse("application/octet-stream"), (byte[]) b);
            }

            throw new IllegalStateException
                    ("@Raw annotation allowed only with `String` or `byte[]` request body");
        }

        return gsonConverterFactory.requestBodyConverter(
                type,
                parameterAnnotations,
                methodAnnotations,
                retrofit
        );
    }

}
