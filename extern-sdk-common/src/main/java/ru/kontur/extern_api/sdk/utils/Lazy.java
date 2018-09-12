/*
 * MIT License
 *
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

package ru.kontur.extern_api.sdk.utils;

import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;


/**
 * Thread safe lazy initializer.
 */
public class Lazy<T> implements Supplier<T> {

    private final Object lock;
    private final Supplier<T> supplier;

    private T instance;
    private RuntimeException error;

    private Lazy(Supplier<T> supplier) {
        this.lock = new Object();
        this.supplier = supplier;
    }

    @Override
    public T get() {

        if (error != null)
            throw error;

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = supplier.get();
                    } catch (RuntimeException e) {
                        error = new PreviouslyCaughtError(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }

    @NotNull
    public static <T> Lazy<T> of(@NotNull Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return new Lazy<>(supplier);
    }
}
