package ru.kontur.extern_api.sdk.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class ConditionAwaiter {

    public static <T> CompletableFuture<T> waitForCondition(@NotNull Supplier<CompletableFuture<T>> supplier,
            Predicate<T> predicate, int delayTimeOut) {
        return supplier.get().thenCompose(result -> {
            if (predicate.test(result)) {
                return CompletableFuture.completedFuture(result);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(delayTimeOut);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return waitForCondition(supplier, predicate, delayTimeOut);
        });
    }
}
