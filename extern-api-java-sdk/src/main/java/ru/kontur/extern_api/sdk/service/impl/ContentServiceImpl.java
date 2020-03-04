package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.httpclient.api.ContentApi;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.ContentService;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ContentServiceImpl implements ContentService {

    private final int DefaultMaxContentSize = 64 * 1024 * 1024;
    private final ContentApi api;
    private final AccountProvider accountProvider;

    public ContentServiceImpl(ContentApi api, AccountProvider accountProvider) {
        this.api = api;
        this.accountProvider = accountProvider;
    }

    @Override
    public CompletableFuture<byte[]> downloadAllContent(UUID contentId) {
        return api.downloadContent(accountProvider.accountId(), contentId).thenApply(responseBody -> {
            try {
                return responseBody.bytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<byte[]> downloadPartContent(UUID contentId, int from, int to) {
        String header = String.format("bytes=%d-%d", from, to);
        return api.downloadPartialContent(accountProvider.accountId(), contentId, header).thenApply(responseBody -> {
            try {
                return responseBody.bytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<byte[]> downloadPartContentByLength(UUID contentId, int from, int length) {

        String header = String.format("bytes=%d-%d", from, from + length - 1);
        return api.downloadPartialContent(accountProvider.accountId(), contentId, header).thenApply(responseBody -> {
            try {
                return responseBody.bytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }
}
