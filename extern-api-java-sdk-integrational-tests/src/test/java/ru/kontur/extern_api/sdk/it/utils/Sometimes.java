package ru.kontur.extern_api.sdk.it.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Sometimes {

    public final int howOften;

    public Sometimes(int howOften) {
        this.howOften = howOften;
        counter = new AtomicInteger();
    }

    private final AtomicInteger counter;

    public boolean maybeNow(Runnable runnable) {

        int calls = counter.incrementAndGet();
        if (calls != howOften) {
            return false;
        }

        counter.addAndGet(-howOften);
        runnable.run();
        return true;
    }
}
