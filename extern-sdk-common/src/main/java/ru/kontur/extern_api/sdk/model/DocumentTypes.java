package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public class DocumentTypes {

    /**
     * Предоставление налоговых деклараций (ФНС, 534).
     */
    public enum Fns534Report implements Urn<Fns534Report> {

        /** Декларация. */
        @SerializedName("urn:document:fns534-report")
        Report,

        /** Описание. */
        @SerializedName("urn:document:fns534-report-description")
        Description,

        /** Соообщение о доверенности или Соообщение о представительстве. */
        @SerializedName("urn:document:fns534-report-warrant")
        Warrant,

        /** Соообщение о доверенности или Соообщение о представительстве. */
        @SerializedName("urn:document:fns534-report-legacy-warrant")
        LegacyWarrant,

        /** ПодтверждениеДатыОтправки. */
        @SerializedName("urn:document:fns534-report-date-confirmation")
        DateConfirmation,

        /** КвитанцияОПриеме. */
        @SerializedName("urn:document:fns534-report-acceptance-result-positive")
        AcceptanceResultPositive,

        /** УведомлениеОбОтказе. */
        @SerializedName("urn:document:fns534-report-acceptance-result-negative")
        AcceptanceResultNegative,

        /** ИзвещениеОВводе. */
        @SerializedName("urn:document:fns534-report-processing-result-ok")
        ProcessingResultOk,

        /** УведомлениеОбУточнении. */
        @SerializedName("urn:document:fns534-report-processing-result-precise")
        ProcessingResultPrecise,

        /** ИзвещениеОПолучении. */
        @SerializedName("urn:document:fns534-report-receipt")
        Receipt,

        /** ПротоколПриема2НДФЛ. */
        @SerializedName("urn:document:fns534-report-acceptance-result-2ndfl-protocol")
        AcceptanceResult2ndflProtocol,

        /** РеестрПринятыхДокументов. */
        @SerializedName("urn:document:fns534-report-acceptance-result-2ndfl-registry")
        AcceptanceResult2ndflRegistry,

        /** Приложение. */
        @SerializedName("urn:document:fns534-report-attachment")
        Attachment,

        /** Описание приложения (для проверки). */
        @SerializedName("urn:document:fns534-report-attachment-info")
        AttachmentInfo;

    }

}
