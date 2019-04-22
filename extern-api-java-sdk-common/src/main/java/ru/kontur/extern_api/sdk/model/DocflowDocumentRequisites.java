package ru.kontur.extern_api.sdk.model;

/**
 * Класс содержит значемые реквизиты документа
 */
public class DocflowDocumentRequisites extends DocflowDocumentRequisitesBase {

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass());
    }
}