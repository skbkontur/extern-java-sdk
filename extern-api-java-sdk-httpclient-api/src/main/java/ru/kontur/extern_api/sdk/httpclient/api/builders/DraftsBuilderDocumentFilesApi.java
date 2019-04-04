/*
 * Copyright (c) 2019 SKB Kontur
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
 */

package ru.kontur.extern_api.sdk.httpclient.api.builders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMetaRequest;

public interface DraftsBuilderDocumentFilesApi<
        TDraftsBuilderDocumentFile extends DraftsBuilderDocumentFile,
        TDraftsBuilderDocumentFileContents extends DraftsBuilderDocumentFileContents,
        TDraftsBuilderDocumentFileMeta extends DraftsBuilderDocumentFileMeta,
        TDraftsBuilderDocumentFileMetaRequest extends DraftsBuilderDocumentFileMetaRequest> {

    CompletableFuture<TDraftsBuilderDocumentFile> create(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            TDraftsBuilderDocumentFileContents contents
    );

    CompletableFuture<TDraftsBuilderDocumentFile[]> getAll(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    );

    CompletableFuture<TDraftsBuilderDocumentFile> get(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    );

    CompletableFuture<TDraftsBuilderDocumentFile> update(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileContents newContents
    );

    CompletableFuture<Void> delete(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    );

    CompletableFuture<byte[]> getContent(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    );

    CompletableFuture<byte[]> getSignature(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    );

    CompletableFuture<TDraftsBuilderDocumentFileMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    );

    CompletableFuture<TDraftsBuilderDocumentFileMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileMetaRequest newMeta
    );
}
