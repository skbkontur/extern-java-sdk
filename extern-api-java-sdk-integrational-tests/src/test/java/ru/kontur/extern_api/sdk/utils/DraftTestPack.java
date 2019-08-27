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

    private QueryContext<DraftDocument> addDocuments() {

        DocType docType = DocType.getDocType(meta.getRecipient());
        DocumentContents mainDocument = EngineUtils.with(engine)
                .createDocumentContents(data.getDocs()[0], docType);

        if (docType == DocType.PFR) {
            DocumentContents descriptionXml = EngineUtils.with(engine)
                    .createDocumentContents(data.getDocs()[1], docType);
            descriptionXml.getDescription().setFilename("Описание отчетности.xml");
            UncheckedSupplier.get(() -> engine
                    .getDraftService()
                    .addDecryptedDocumentAsync(defaultDraftCxt.get(), descriptionXml)
                    .get()
                    .ensureSuccess());

            DocumentContents someAttachment = EngineUtils.with(engine)
                    .createDocumentContents(data.getDocs()[2], docType);
            UncheckedSupplier.get(() -> engine
                    .getDraftService()
                    .addDecryptedDocumentAsync(defaultDraftCxt.get(), someAttachment)
                    .get()
                    .ensureSuccess());

            return UncheckedSupplier.get(() -> engine
                    .getDraftService()
                    .addDecryptedDocumentAsync(defaultDraftCxt.get(), mainDocument)
                    .get()
                    .ensureSuccess());
        }

        return UncheckedSupplier.get(() -> engine
                .getDraftService()
                .addDecryptedDocumentAsync(defaultDraftCxt.get(), mainDocument)
                .get()
                .ensureSuccess());
    }

    private void signDocument(UUID documentId) {

        byte[] docContent = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        defaultDraftCxt.get(),
                        documentId
                )
                .join()
                .getOrThrow();

        if (Zip.isZip(docContent)) {
            docContent = Zip.unzip(docContent);
        }

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), docContent);

        UncheckedSupplier.get(() -> engine.getDraftService()
                .updateSignatureAsync(
                        defaultDraftCxt.get(),
                        documentId,
                        signature
                )
                .get());
    }

    public UUID createDraft() {
        return getCreateDraftCxt().get();
    }

    public Draft emptyDraft() {
        return getEmptyDraft().get();
    }

    public Draft draftWithDocument() {
        addDocuments();
        return getDraft().get();

    }

    public Draft newDraftWithDocument() {
        createNewEmptyDraft();
        addDocuments();
        return getDraft().get();
    }

    public Draft draftWithSignedDocument() {
        DraftDocument document = addDocuments().get();
        signDocument(document.getId());
        return getDraft().get();
    }

    public Pair<Draft, DraftDocument> addDocumentPack() {
        return new Pair<>(getEmptyDraft().get(), addDocuments().get());
    }

    public Pair<Draft, DraftDocument> addDocumentFnsPack() {
        if (meta.getRecipient() instanceof FnsRecipient) {
            return new Pair<>(getEmptyDraft().get(), addDocuments().get());
        }
        return null;

    }

}
