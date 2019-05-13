package ru.kontur.extern_api.sdk.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class Awaiter {

    public static <T> CompletableFuture<T> waitForCondition(
            @NotNull Supplier<CompletableFuture<T>> supplier,
            Predicate<T> predicate,
            int delayTimeOutMs
    ) {
        return waitForCondition(supplier, predicate, delayTimeOutMs, 60 * 1000);
    }

    public static <T> CompletableFuture<T> waitForCondition(
            @NotNull Supplier<CompletableFuture<T>> supplier,
            Predicate<T> predicate,
            int delayTimeOutMs,
            int timeoutMs
    ) {
        return supplier.get().thenCompose(result -> {
            if (timeoutMs <= 0) {
                throw new TimeoutError();
            }

            if (predicate.test(result)) {
                return CompletableFuture.completedFuture(result);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(delayTimeOutMs);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return waitForCondition(supplier, predicate, delayTimeOutMs, timeoutMs - delayTimeOutMs);
        });
    }
}
