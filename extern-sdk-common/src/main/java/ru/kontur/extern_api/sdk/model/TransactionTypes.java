package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public class TransactionTypes {

    /**
     * Предоставление налоговых деклараций (ФНС, 534).
     */
    public enum Fns534Report implements Urn<Fns534Report> {

        /** Декларация. */
        @SerializedName("urn:transaction:fns534-report")
        Report,

        /** Подтверждение о получении отчета налоговым органом. */
        @SerializedName("urn:transaction:fns534-report-receipt-report-cu")
        ReceiptReportCu,

        /** Получено подтверждение даты отправки абонентом. */
        @SerializedName("urn:transaction:fns534-report-receipt-date-confirmation-abonent")
        ReceiptDateConfirmationAbonent,

        /** Получено подтверждение даты отправки налоговым органом. */
        @SerializedName("urn:transaction:fns534-report-receipt-date-confirmation-cu")
        ReceiptDateConfirmationCu,

        /** Отчет принят налоговым органом. */
        @SerializedName("urn:transaction:fns534-report-acceptance-result-positive")
        AcceptanceResultPositive,

        /** Отчет отклонен налоговым органом. */
        @SerializedName("urn:transaction:fns534-report-acceptance-result-negative")
        AcceptanceResultNegative,

        /** Подтверждение о принятии отчета абонентом. */
        @SerializedName("urn:transaction:fns534-report-receipt-acceptance-result-abonent")
        ReceiptAcceptanceResultAbonent,

        /** Подтверждение о принятии отчета спец. оператором. */
        @SerializedName("urn:transaction:fns534-report-receipt-acceptance-result-provider")
        ReceiptAcceptanceResultProvider,

        /** Отчет обработан успешно. */
        @SerializedName("urn:transaction:fns534-report-processing-result-ok")
        ProcessingResultOk,

        /** Отчет обработан неуспешно. */
        @SerializedName("urn:transaction:fns534-report-processing-result-precise")
        ProcessingResultPrecise,

        /** Подтверждение об обработке отчета абонентом. */
        @SerializedName("urn:transaction:fns534-report-receipt-processing-result-abonent")
        ReceiptProcessingResultAbonent,

        /** Подтверждение об обработке отчета спец. оператором. */
        @SerializedName("urn:transaction:fns534-report-receipt-processing-result-provider")
        ReceiptProcessingResultProvider,

        /** Отчет НДФЛ-2 принят. */
        @SerializedName("urn:transaction:fns534-report-acceptance-result-2ndfl")
        AcceptanceResult2ndfl

    }
}
