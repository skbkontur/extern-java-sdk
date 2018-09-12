package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Типы документов.
 */
public enum DocumentType implements Urn<DocumentType> {

    /** Пространство имен для типов документов. */
    @SerializedName("urn:document")
    Namespace,

    /** Неправильный документ неизвестного типа. */
    @SerializedName("urn:document:unknown")
    Unknown,

    /** Ошибка. */
    @SerializedName("urn:document:error")
    Error,

    /** Уведомление от тех.поддержки. */
    @SerializedName("urn:document:error-notification")
    ErrorNotification,

    /** Результат запроса списка отчетов. */
    @SerializedName("urn:document:query-result")
    QueryResult,

    /** Декларация. */
    @SerializedName("urn:document:fns534-report")
    Fns534Report,

    /** Описание. */
    @SerializedName("urn:document:fns534-report-description")
    Fns534Description,

    /** Соообщение о доверенности или Соообщение о представительстве. */
    @SerializedName("urn:document:fns534-report-warrant")
    Fns534Warrant,

    /** Соообщение о доверенности или Соообщение о представительстве. */
    @SerializedName("urn:document:fns534-report-legacy-warrant")
    Fns534LegacyWarrant,

    /** ПодтверждениеДатыОтправки. */
    @SerializedName("urn:document:fns534-report-date-confirmation")
    Fns534DateConfirmation,

    /** КвитанцияОПриеме. */
    @SerializedName("urn:document:fns534-report-acceptance-result-positive")
    Fns534AcceptanceResultPositive,

    /** УведомлениеОбОтказе. */
    @SerializedName("urn:document:fns534-report-acceptance-result-negative")
    Fns534AcceptanceResultNegative,

    /** ИзвещениеОВводе. */
    @SerializedName("urn:document:fns534-report-processing-result-ok")
    Fns534ProcessingResultOk,

    /** УведомлениеОбУточнении. */
    @SerializedName("urn:document:fns534-report-processing-result-precise")
    Fns534ProcessingResultPrecise,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-report-receipt")
    Fns534Receipt,

    /** ПротоколПриема2НДФЛ. */
    @SerializedName("urn:document:fns534-report-acceptance-result-2ndfl-protocol")
    Fns534AcceptanceResult2ndflProtocol,

    /** РеестрПринятыхДокументов. */
    @SerializedName("urn:document:fns534-report-acceptance-result-2ndfl-registry")
    Fns534AcceptanceResult2ndflRegistry,

    /** Приложение. */
    @SerializedName("urn:document:fns534-report-attachment")
    Fns534Attachment,

    /** Описание приложения (для проверки). */
    @SerializedName("urn:document:fns534-report-attachment-info")
    Fns534AttachmentInfo,

    /** Запрос. */
    @SerializedName("urn:document:fns534-ion-request")
    Fns534IonRequest,
    /** ПодтверждениеДатыОтправки */

    @SerializedName("urn:document:fns534-ion-date-confirmation")
    Fns534IonDateConfirmation,
    /** Описание. */

    @SerializedName("urn:document:fns534-ion-description")
    Fns534IonDescription,
    /** КвитанцияОПриеме. */

    @SerializedName("urn:document:fns534-ion-acceptance-result-positive")
    Fns534IonAcceptanceResultPositive,
    /** УведомлениеОбОтказе. */

    @SerializedName("urn:document:fns534-ion-acceptance-result-negative")
    Fns534IonAcceptanceResultNegative,
    /** Ответ. */

    @SerializedName("urn:document:fns534-ion-response")
    Fns534IonResponse,
    /** Доверенность. */

    @SerializedName("urn:document:fns534-ion-warrant")
    Fns534IonWarrant,
    /** ИзвещениеОПол учении. */

    @SerializedName("urn:document:fns534-ion-receipt")
    Fns534IonReceipt,

    /** Письмо. */
    @SerializedName("urn:document:fns534-letter")
    Fns534Letter,

    /** Описание. */
    @SerializedName("urn:document:fns534-letter-description")
    Fns534LetterDescription,

    /** Приложение. */
    @SerializedName("urn:document:fns534-letter-attachment")
    Fns534LetterAttachment,

    /** ПДО. */
    @SerializedName("urn:document:fns534-letter-date-confirmation")
    Fns534LetterDateConfirmation,

    /** УведомлениеОбОтказе. */
    @SerializedName("urn:document:fns534-letter-decline-notice")
    Fns534LetterDeclineNotice,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-letter-receipt")
    Fns534LetterReceipt,

    /** Доверенность. */
    @SerializedName("urn:document:fns534-letter-warrant")
    Fns534LetterWarrant,

    /** Письмо/рассылка. */
    @SerializedName("urn:document:fns534-cu-letter")
    Fns534CuLetter,

    /** Описание. */
    @SerializedName("urn:document:fns534-cu-letter-description")
    Fns534CuLetterDescription,

    /** Приложение. */
    @SerializedName("urn:document:fns534-cu-letter-attachment")
    Fns534CuLetterAttachment,

    /** ПодтверждениеДатыОтправки. */
    @SerializedName("urn:document:fns534-cu-letter-date-confirmation")
    Fns534CuLetterDateConfirmation,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-cu-letter-receipt")
    Fns534CuLetterReceipt,


    /* Рассылка. */
    @SerializedName("urn:document:fns534-cu-broadcast")
    Fns534CuBroadcast,

    /** Описание. */
    @SerializedName("urn:document:fns534-cu-broadcast-description")
    Fns534CuBroadcastDescription,

    /** Приложение. */
    @SerializedName("urn:document:fns534-cu-broadcast-attachment")
    Fns534CuBroadcastAttachment,

    /** Приложение. */
    @SerializedName("urn:document:fns534-cu-broadcast-date-confirmation")
    Fns534CuBroadcastDateConfirmation,

    /** Представление. */
    @SerializedName("urn:document:fns534-submission-message")
    Fns534SubmissionMessage,

    /** Описание. */
    @SerializedName("urn:document:fns534-submission-description")
    Fns534SubmissionDescription,

    /** Приложение. */
    @SerializedName("urn:document:fns534-submission-attachment")
    Fns534SubmissionAttachment,

    /** Доверенность. */
    @SerializedName("urn:document:fns534-submission-warrant")
    Fns534SubmissionWarrant,

    /** ПодтвержениеДатыОтправки. */
    @SerializedName("urn:document:fns534-submission-date-confirmation")
    Fns534SubmissionDateConfirmation,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-submission-acceptance-result-positive")
    Fns534SubmissionAcceptanceResultPositive,

    /** УведомлениОботказе. */
    @SerializedName("urn:document:fns534-submission-acceptance-result-negative")
    Fns534SubmissionAcceptanceResultNegative,

    /** КвитанцияОприеме. */
    @SerializedName("urn:document:fns534-submission-receipt")
    Fns534SubmissionReceipt,

    /** Документ */
    @SerializedName("urn:document:fns534-demand")
    Fns534Demand,

    /** Расшифрованный документ для генерации Описи. */
    @SerializedName("urn:document:fns534-demand-decrypted")
    Fns534DemandDecrypted,

    /** Описание. */
    @SerializedName("urn:document:fns534-demand-description")
    Fns534DemandDescription,

    /** Приложение. */
    @SerializedName("urn:document:fns534-demand-attachment")
    Fns534DemandAttachment,

    /** ПодтвержениеДатыОтправки. */
    @SerializedName("urn:document:fns534-demand-date-confirmation")
    Fns534DemandDateConfirmation,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-demand-acceptance-result-positive")
    Fns534DemandAcceptanceResultPositive,

    /** УведомлениОботказе. */
    @SerializedName("urn:document:fns534-demand-acceptance-result-negative")
    Fns534DemandAcceptanceResultNegative,

    /** КвитанцияОприеме. */
    @SerializedName("urn:document:fns534-demand-receipt")
    Fns534DemandReceipt,

    /** Заявление о ввозе товаров и уплате косвенных налогов. */
    @SerializedName("urn:document:fns534-application")
    Fns534Application,

    /** Описание. */
    @SerializedName("urn:document:fns534-application-description")
    Fns534ApplicationDescription,

    /** Доверенность. */
    @SerializedName("urn:document:fns534-application-warrant")
    Fns534ApplicationWarrant,

    /** ПодтверждениеДатыОтправки. */
    @SerializedName("urn:document:fns534-application-date-confirmation")
    Fns534ApplicationDateConfirmation,

    /** УведомлениеОбОтказе. */
    @SerializedName("urn:document:fns534-application-acceptance-result-negative")
    Fns534ApplicationAcceptanceResultNegative,

    /** КвитанцияОПриеме. */
    @SerializedName("urn:document:fns534-application-acceptance-result-positive")
    Fns534ApplicationAcceptanceResultPositive,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:fns534-application-receipt")
    Fns534ApplicationReceipt,

    /** CообщениеОПростОтметки. */
    @SerializedName("urn:document:fns534-application-processing-result-positive")
    Fns534ApplicationProcessingResultPositive,

    /** УведомлениеОбОтказеОтметки. */
    @SerializedName("urn:document:fns534-application-processing-result-negative")
    Fns534ApplicationProcessingResultNegative,

    /** УведомлениеРезультатПроверки(deprecated). */
    @SerializedName("urn:document:fns534-application-check-protocol")
    Fns534ApplicationCheckProtocol,

    /** ОтзывЗаявления(deprecated). */
    @SerializedName("urn:document:fns534-application-revocation")
    Fns534ApplicationRevocation,

    /** УведомлениеРезультатПроверкиТС(deprecated). */
    @SerializedName("urn:document:fns534-application-union-check-protocol")
    Fns534ApplicationUnionCheckProtocol,

    /** отчет */
    @SerializedName("urn:document:stat-report")
    StatReport,

    /** приложениеПисьма. */
    @SerializedName("urn:document:stat-report-report-attachment")
    StatReportAttachment,

    /** описаниеОтчета. */
    @SerializedName("urn:document:stat-report-description")
    StatReportDescription,

    /** извещениеОПолучении. */
    @SerializedName("urn:document:stat-report-date-confirmation")
    StatReportDateConfirmation,

    /** подтверждениеОператора. */
    @SerializedName("urn:document:stat-report-receipt")
    StatReportReceipt,

    /** уведомление успех (deprecated). */
    @SerializedName("urn:document:stat-report-protocol-positive")
    StatReportProtocolPositive,

    /** уведомление отказ (deprecated). */
    @SerializedName("urn:document:stat-report-protocol-negative")
    StatReportProtocolNegative,

    /** уведомлениеОПриемеВОбработку. */
    @SerializedName("urn:document:stat-report-protocol-v2-positive")
    StatReportProtocolV2Positive,

    /** уведомлениеОНесоответствииФормату. */
    @SerializedName("urn:document:stat-report-protocol-v2-negative")
    StatReportProtocolV2Negative,

    /** уведомлениеОбУточнении. */
    @SerializedName("urn:document:stat-report-protocol-v2-precise")
    StatReportProtocolV2Precise,

    /** уведомлениеОбОтклонении. */
    @SerializedName("urn:document:stat-report-protocol-v2-reject")
    StatReportProtocolV2Reject,

    /** Письмо. */
    @SerializedName("urn:document:stat-letter")
    StatLetter,

    /** Описание. */
    @SerializedName("urn:document:stat-letter-description")
    StatLetterDescription,

    /** Приложение. */
    @SerializedName("urn:document:stat-letter-attachment")
    StatLetterAttachment,

    /** ПодтверждениеОператора. */
    @SerializedName("urn:document:stat-letter-confirmation")
    StatLetterConfirmation,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:stat-letter-receipt")
    StatLetterReceipt,

    /** Письмо. */
    @SerializedName("urn:document:stat-cu-letter")
    StatCuLetter,

    /** Описание. */
    @SerializedName("urn:document:stat-cu-letter-description")
    StatCuLetterDescription,

    /** Приложение. */
    @SerializedName("urn:document:stat-cu-letter-attachment")
    StatCuLetterAttachment,

    /** ПодтверждениеОператора. */
    @SerializedName("urn:document:stat-cu-letter-confirmation")
    StatCuLetterConfirmation,

    /** ИзвещениеОПолучении. */
    @SerializedName("urn:document:stat-cu-letter-receipt")
    StatCuLetterReceipt,

    /** рассылка. */
    @SerializedName("urn:document:stat-cu-broadcast-letter")
    StatCuBroadcastLetter,

    /** Описание. */
    @SerializedName("urn:document:stat-cu-broadcast-description")
    StatCuBroadcastDescription,

    /** Приложение. */
    @SerializedName("urn:document:stat-cu-broadcast-attachment")
    StatCuBroadcastAttachment,

    /** ПодтверждениеОператора. */
    @SerializedName("urn:document:stat-cu-broadcast-confirmation")
    StatCuBroadcastConfirmation,
}
