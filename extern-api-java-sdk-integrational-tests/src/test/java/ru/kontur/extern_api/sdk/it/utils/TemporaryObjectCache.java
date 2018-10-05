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

package ru.kontur.extern_api.sdk.it.utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.utils.UncheckedFunction;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;

public class TemporaryObjectCache<T> implements ObjectCache<T> {

    private final Gson serializer;
    private final Path cacheRoot;
    private final Type typeOfT;
    private final String prefix;
    private final String suffix;
    private final Function<T, String> keyMapper;

    public TemporaryObjectCache(
            String tmpSubdirectory,
            Gson serializer,
            Function<T, String> keyMapper,
            Class<T> tClass) throws IOException {
        this(
                tClass.getSimpleName().toLowerCase() + "-",
                ".json",
                createTmpSubdirectory(tmpSubdirectory),
                serializer,
                keyMapper,
                tClass
        );
    }

    public TemporaryObjectCache(
            String prefix,
            String suffix,
            String tmpSubdirectory,
            Gson serializer,
            Function<T, String> keyMapper,
            Class<T> classOfT) throws IOException {
        this(prefix, suffix, createTmpSubdirectory(tmpSubdirectory), serializer, keyMapper, classOfT);
    }

    public TemporaryObjectCache(
            String prefix,
            String suffix,
            Path cacheRoot,
            Gson serializer,
            Function<T, String> keyMapper,
            Type typeOfT) {
        this.serializer = serializer;
        this.cacheRoot = cacheRoot;
        this.typeOfT = typeOfT;
        this.prefix = prefix;
        this.suffix = suffix;
        this.keyMapper = keyMapper;
    }

    @Override
    public T get(String key) {
        return get(cacheRoot.resolve(filename(key)));
    }

    @Override
    public T put(String key, T t) {
        return put(cacheRoot.resolve(filename(key)), t);
    }

    @Override
    public boolean isCached(String key) {
        return isCached(cacheRoot.resolve(filename(key)));
    }

    @Override
    public T getOrPut(String key, Supplier<T> tSupplier) {
        Path path = cacheRoot.resolve(filename(key));

        if (isCached(path)) {
            return get(path);
        }

        return put(path, tSupplier.get());
    }

    @Override
    public @Nullable Function<T, String> getKeyMapper() {
        return keyMapper;
    }

    private T get(Path path) {
        return UncheckedFunction
                .calm(Files::readAllBytes)
                .andThen(String::new)
                .andThen(s -> serializer.<T>fromJson(s, typeOfT))
                .apply(path);
    }

    private T put(Path path, T t) {
        UncheckedRunnable.run(() -> Files.write(path, serializer.toJson(t).getBytes()));
        return t;
    }

    private boolean isCached(Path path) {
        return Files.exists(path);
    }


    private String filename(String key) {
        return prefix + key + suffix;
    }

    private static Path createTmpSubdirectory(String tmpSubdirectory) throws IOException {
        String tmp = Objects.requireNonNull(
                System.getProperty("java.io.tmpdir"),
                "\"java.io.tmpdir\" required"
        );
        Path cacheRoot = Paths.get(tmp, tmpSubdirectory);
        if (!Files.exists(cacheRoot) || !Files.isDirectory(cacheRoot)) {
            Files.createDirectory(cacheRoot);
        }
        return cacheRoot;
    }
}
