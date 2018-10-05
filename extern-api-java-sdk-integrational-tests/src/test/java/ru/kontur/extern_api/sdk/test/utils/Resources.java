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

package ru.kontur.extern_api.sdk.test.utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import ru.kontur.extern_api.sdk.GsonProvider;

public class Resources {

    public static String getPath(String first, String... more) {
        first = first.replace('\\', '/');

        if (!first.startsWith("/")) {
            first = "/" + first;
        }

        StringBuilder sb = new StringBuilder(first);
        Arrays.stream(more).map(e -> e.replace('\\', '/')).forEach(s -> sb.append("/").append(s));
        return sb.toString();
    }

    public static <T> T loadFromJson(String path, Class<T> type, Gson gson) {
        try (InputStream is = Resources.class.getResourceAsStream(path)) {
            Objects.requireNonNull(is, "Resource not found: " + path);
            return gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T loadFromJson(String path, Class<T> type) {
        return loadFromJson(path, type, GsonProvider.getGson());
    }

}
