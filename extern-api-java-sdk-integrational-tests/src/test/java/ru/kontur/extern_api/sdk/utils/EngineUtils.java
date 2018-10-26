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

package ru.kontur.extern_api.sdk.utils;

import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.DocumentContents;

public class EngineUtils {

    private final ExternEngine engine;

    public final CryptoUtils crypto;

    private EngineUtils(ExternEngine engine) {
        this.engine = engine;
        this.crypto = CryptoUtils.with(engine.getCryptoProvider());
    }

    public DocumentContents createDocumentContents(String path, DocType docType) {

        DocumentContents documentContents = TestUtils.loadDocumentContents(path, docType);

        byte[] signature = crypto.sign(
                engine.getConfiguration().getThumbprint(),
                Base64.getDecoder().decode(documentContents.getBase64Content())
        );

        if (signature != null) {
            documentContents.setSignature(Base64.getEncoder().encodeToString(signature));
        }

        return documentContents;
    }

    public <T> CompletableFuture<QueryContext<T>> addDecryptedDocument(
            QueryContext<T> current,
            String path,
            DocType docType) {

        DocumentContents documentContents = createDocumentContents(path, docType);
        return engine
                .getDraftService()
                .addDecryptedDocumentAsync(current.getDraftId(), documentContents)
                .thenApply(cxt -> current);
    }


    @NotNull
    public static EngineUtils with(@NotNull ExternEngine engine) {
        return new EngineUtils(Objects.requireNonNull(engine));
    }
}
