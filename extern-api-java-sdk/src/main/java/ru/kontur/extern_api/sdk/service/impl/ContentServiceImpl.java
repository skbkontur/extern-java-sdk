package ru.kontur.extern_api.sdk.service.impl;

import okhttp3.ResponseBody;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.httpclient.api.ContentApi;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.ContentService;

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
    public CompletableFuture<ResponseBody> downloadAllContent(UUID contentId) {
        return api.downloadContent(accountProvider.accountId(), contentId);
    }

    @Override
    public CompletableFuture<ResponseBody> downloadPathContent(UUID contentId, int from, int to) {
        String header = String.format("bytes=%d-%d", from, to);
        return api.downloadContentByPart(accountProvider.accountId(), contentId, header);
    }

    @Override
    public CompletableFuture<ResponseBody> downloadPartContentByLength(UUID contentId, int from, int length) {

        String header = String.format("bytes=%d-%d", from, from + length - 1);
        return api.downloadContentByPart(accountProvider.accountId(), contentId, header);
    }
}
