package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.TestData;

public class DraftTestPack {

    private final TestData data;
    private final DraftMetaRequest meta;
    private final ExternEngine engine;
    private final CryptoUtils cryptoUtils;

    private QueryContext<UUID> defaultDraftCxt;

    public DraftTestPack(TestData data, ExternEngine engine, CryptoUtils cryptoUtils) {

        this.cryptoUtils = cryptoUtils;
        this.engine = engine;
        this.data = data;
        meta = TestUtils.toDraftMetaRequest(data);
    }

    public void createNewEmptyDraftIfNecessary() {

        QueryContext<Draft> draft = getDraft();

        if (defaultDraftCxt == null
                || draft.getServiceError() != null
                || draft.get().getStatus().name().equals("sent")) {
            createNewEmptyDraft();
        }
    }

    private QueryContext<UUID> getCreateDraftCxt() {

        if (getDraft().get().getDocuments().size() > 0) {
            createNewEmptyDraft();
        }
        return defaultDraftCxt;
    }

    public void createNewEmptyDraft() {

        defaultDraftCxt = UncheckedSupplier.get(() -> engine.getDraftService()
                .createAsync(meta)
                .get()
                .ensureSuccess())
                .map(QueryContext.DRAFT_ID, Draft::getId);
    }

    private QueryContext<Draft> getEmptyDraft() {

        QueryContext<Draft> getDraftCxt = getDraft();

        if (getDraftCxt.get().getDocuments().size() > 0) {
            createNewEmptyDraft();
            return getDraft();
        }
        return getDraftCxt;
    }

    private QueryContext<Draft> getDraft() {

        return UncheckedSupplier.get(() -> engine.getDraftService()
                .lookupAsync(defaultDraftCxt.get())
                .get());
    }

    private QueryContext<DraftDocument> addDocument() {

        String path = data.getDocs()[0];
        DocType docType = DocType.getDocType(meta.getRecipient());
        DocumentContents documentContents = EngineUtils.with(engine)
                .createDocumentContents(path, docType);

        return UncheckedSupplier.get(() -> engine
                .getDraftService()
                .addDecryptedDocumentAsync(defaultDraftCxt.get(), documentContents)
                .get()
                .ensureSuccess());
    }

    private void signDocument(UUID documentId) {

        byte[] docContent = UncheckedSupplier.get(() -> engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        defaultDraftCxt.get(),
                        documentId)
                .get()
                .ensureSuccess()
                .get());

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), Zip.unzip(docContent));

        UncheckedSupplier.get(() -> engine.getDraftService()
                .updateSignatureAsync(
                        defaultDraftCxt.get(),
                        documentId,
                        signature)
                .get());
    }

    public UUID createDraft() {
        return getCreateDraftCxt().get();
    }

    public Draft emptyDraft() {
        return getEmptyDraft().get();
    }

    public Draft draftWithDocument() {
        addDocument();
        return getDraft().get();

    }

    public Draft newDraftWithDocument() {
        createNewEmptyDraft();
        addDocument();
        return getDraft().get();
    }

    public Draft draftWithSignedDocument() {
        DraftDocument document = addDocument().get();
        signDocument(document.getId());
        return getDraft().get();
    }

    public Pair<Draft, DraftDocument> addDocumentPack() {
        return new Pair<>(getEmptyDraft().get(), addDocument().get());
    }

    public Pair<Draft, DraftDocument> addDocumentNoFnsPack() {
        if (!(meta.getRecipient() instanceof FnsRecipient)) {
            return null;
        }

        return new Pair<>(getEmptyDraft().get(), addDocument().get());

    }
}
