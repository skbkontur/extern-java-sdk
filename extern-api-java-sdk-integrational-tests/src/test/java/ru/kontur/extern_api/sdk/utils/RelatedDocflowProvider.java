package ru.kontur.extern_api.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.apache.commons.io.IOUtils;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.ExtendedDraftMetaRequest;
import ru.kontur.extern_api.sdk.model.Inventory;

public class RelatedDocflowProvider {

    private static final String WINDOWS_1251 = "windows-1251";
    private static final String VALID_INVENTORY_JPG_DOCUMENT = "/inventories/zapiska_valid.jpg";

    private final ExternEngine engine;
    private final DocumentDataProvider documentDataProvider;
    private final CryptoUtils cryptoUtils;

    public RelatedDocflowProvider(ExternEngine engine, String ifnsCode) {
        this.engine = engine;
        documentDataProvider = new DocumentDataProvider(ifnsCode);
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
    }

    public CompletableFuture<Docflow> sendRelatedLetter(DemandTestData testData) {
        return engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .createRelatedDraft(new ExtendedDraftMetaRequest(TestUtils.toDraftMetaRequest(testData), "Письмо"))
                .thenCompose(draft -> addLetterDocument(draft, testData))
                .thenCompose(this::sendDraft)
                .thenCompose(docflow -> waitForDocflow(docflow.getId()));
    }

    public CompletableFuture<Inventory> sendRelatedInventory(DemandTestData testData) {
        UUID childFileId = UUID.randomUUID();
        UUID baseFileId = UUID.randomUUID();

        return engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .createRelatedDraft(TestUtils.toDraftMetaRequest(testData))
                .thenCompose(draft -> addInventoriesDocument(draft, testData, baseFileId, childFileId))
                .thenCompose(draft -> addInventoriesSubmission(draft, testData, baseFileId, childFileId))
                .thenCompose(this::sendDraft)
                .thenCompose(docflow -> waitForInventory(docflow.getId(), testData));
    }

    private CompletionStage<Draft> addLetterDocument(Draft draft, DemandTestData testData) {
        byte[] docContent = getContentBytes(documentDataProvider.getLetterData(testData));
        return addAnyDocument(docContent, new DocumentDescription(), draft);
    }

    private CompletableFuture<Docflow> waitForDocflow(UUID id) {
        return Awaiter.waitForCondition(() -> engine.getDocflowService()
                        .lookupDocflowAsync(id)
                        .thenApply(QueryContext::getDocflow),
                Objects::nonNull, 2000);
    }

    private CompletableFuture<Inventory> waitForInventory(UUID id, DemandTestData testData) {
        return Awaiter.waitForCondition(
                () -> engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                        .getInventory(id),
                Objects::nonNull, 2000);
    }

    private CompletableFuture<Draft> addInventoriesDocument(Draft draft, DemandTestData testData, UUID baseFileId,
            UUID childFileId) {
        byte[] docContent = getFromResource(VALID_INVENTORY_JPG_DOCUMENT);
        DocumentDescription description = new DocumentDescription();
        description.setFilename(documentDataProvider.getAttachmentFileName(testData, baseFileId, childFileId));

        return addAnyDocument(docContent, description, draft);
    }

    private CompletableFuture<Draft> addInventoriesSubmission(Draft draft, DemandTestData testData, UUID baseFileId,
            UUID childFileId) {
        byte[] docContent = getContentBytes(documentDataProvider.getSubmissionData(testData, baseFileId, childFileId));
        DocumentDescription description = new DocumentDescription();
        return addAnyDocument(docContent, description, draft);
    }

    private byte[] getContentBytes(String content) {
        try {
            return content.getBytes(WINDOWS_1251);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Draft> addAnyDocument(byte[] docContent, DocumentDescription description, Draft draft) {
        DocumentContents contents = new DocumentContents();
        contents.setBase64Content(TestUtils.toBase64(docContent));
        contents.setSignature(getContentSignature(docContent));
        contents.setDescription(description);
        return engine.getDraftService().addDecryptedDocumentAsync(draft.getId(), contents)
                .thenApply(result -> draft);
    }

    private byte[] getFromResource(String path) {
        try (InputStream is = Resources.class.getResourceAsStream(path)) {
            Objects.requireNonNull(is, "Resource not found: " + path);
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getContentSignature(byte[] docContent) {
        return TestUtils.toBase64(cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), docContent));
    }

    private CompletableFuture<Docflow> sendDraft(Draft draft) {
        return engine.getDraftService().sendAsync(draft.getId()).thenApply(QueryContext::get);
    }

}
