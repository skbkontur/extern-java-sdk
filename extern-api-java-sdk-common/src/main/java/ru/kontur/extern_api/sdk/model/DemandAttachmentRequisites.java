package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Класс содержит значемые реквизиты файла требований ФНС - документа типа urn:document:fns534-demand-attachment.
 */
public class DemandAttachmentRequisites extends DocflowDocumentRequisitesBase {

    private Date demandDate = null;
    private String demandNumber = null;
    private List<String> demandInnList = new ArrayList<>();

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date recognizedDate) {
        demandDate = recognizedDate;
    }

    public List<String> getDemandInnList() {
        return demandInnList;
    }

    public void setDemandInnList(List<String> demandInnList) {
        this.demandInnList = demandInnList;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        DemandAttachmentRequisites that = (DemandAttachmentRequisites) object;
        return java.util.Objects.equals(demandDate, that.demandDate) &&
                java.util.Objects.equals(demandNumber, that.demandNumber) &&
                java.util.Objects.equals(demandInnList, that.demandInnList);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), demandDate, demandNumber, demandInnList);
    }
}