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

package ru.kontur.extern_api.sdk.service;

import ru.kontur.extern_api.sdk.model.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface RelatedDocumentsService {
    /**
     * Возвращает опись связанную с документом по ее Id
     * @param inventoryId ИД описи
     * @return Опись с текущим документом {@link Inventory}
     */
    CompletableFuture<Inventory> getInventory(UUID inventoryId);

    /**
     * Возвращает зашифрованное содержимое документа описи
     * @param inventoryId идентификатор описи
     * @param inventoryDocumentId идентификатор документа
     * @return массив байт соответствующий зашифрованному содержимому документа
     */
    CompletableFuture<byte[]> getEncryptedContentAsync(UUID inventoryId, UUID inventoryDocumentId);

    /**
     * Возвращает расшифрованное содержимое документа (при наличии)
     * @param inventoryId идентификатор описи
     * @param inventoryDocumentId идентификатор документа
     * @return массив байт соответствующий расшифрованному содержимому документа
     */
    CompletableFuture<byte[]> getDecryptedContentAsync(UUID inventoryId, UUID inventoryDocumentId);

    /**
     * Возвращает список подписей под документом
     * @param inventoryId идентификатор описи
     * @param inventoryDocumentId идентификатор документа
     * @return список подписей под документом
     */
    CompletableFuture<List<Signature>> getSignatures(UUID inventoryId, UUID inventoryDocumentId);

    /**
     * Возвращает тело запрошенной подписи
     * @param inventoryId идентификатор описи
     * @param inventoryDocumentId идентификатор документа
     * @param signatureId идентификатор подписи
     * @return массив байт соответствующий запрошенной подписи
     */
    CompletableFuture<byte[]> getSignatureContent(UUID inventoryId, UUID inventoryDocumentId, UUID signatureId);

    /**
     *  Возвращает связанные с текущим документом описи
     *  @param filter {@link DocflowFilter}
     *  @return список описей связанных с текущим документом {@link DocflowPage}
     */
    CompletableFuture<DocflowPage> getRelatedDocflows(DocflowFilter filter);

    /**
     * Возвращает первую 1000 связанных с текущим документом документо оборотов
     * @return список описей связанных с текущим документом {@link DocflowPage}
     */
    CompletableFuture<DocflowPage> getRelatedDocflows();

    /**
     * Возвращает первую 1000 связанных с текущим документом описей
     * @return список описей связанных с текущим документом {@link InventoriesPage}
     */
    CompletableFuture<InventoriesPage> getRelatedInventories();

    /**
     *  Возвращает связанные с текущим документом описи
     *  @param filter {@link DocflowFilter}
     *  @return список описей связанных с текущим документом {@link InventoriesPage}
     */
    CompletableFuture<InventoriesPage> getRelatedInventories(DocflowFilter filter);

    /**
     *  Создает черновик ДО связанного с текущим документом
     *  @param draftMeta {@link DraftMetaRequest}
     *  @return список описей связанных с текущим документом {@link InventoriesPage}
     */
    CompletableFuture<Draft> createRelatedDraft(DraftMetaRequest draftMeta);
}
