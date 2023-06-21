package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.descriptions.*;

public enum DocflowType implements Urn<DocflowType> {

    /**
     * Предоставление налоговых деклараций (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-report")
    FNS534_REPORT(Fns534Report.class),

    /**
     * Информационное обслуживание абонентов (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-ion")
    FNS534_ION(Fns534Ion.class),

    /**
     * Письменные обращения абонентов (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-letter")
    FNS534_LETTER(Fns534Letter.class),

    /**
     * Информирования абонентов (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-cu-letter")
    FNS534_CU_LETTER(Fns534CuLetter.class),

    /**
     * Информационная рассылка (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-cu-broadcast")
    FNS534_CU_BROADCAST,

    /**
     * Представление отдельных документов в налоговые органы (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-submission")
    FNS534_SUBMISSION(Fns534Submission.class),

    /**
     * Представление отдельных документов в налоговые органы (Опись).
     */
    @SerializedName("urn:docflow:fns534-inventory")
    FNS534_INVENTORY(Fns534Inventory.class),

    /**
     * Требования (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-demand")
    FNS534_DEMAND(Fns534Demand.class),

    /**
     * Предоставление заявлений в налоговые органы (ФНС, 534).
     */
    @SerializedName("urn:docflow:fns534-application")
    FNS534_APPLICATION(Fns534Application.class),

    /**
     * Налоговая отчетность (ФНС, 705).
     */
    @SerializedName("urn:docflow:fns705-report")
    FNS705_REPORT,

    /**
     * Входящие письма (ФНС, 705).
     */
    @SerializedName("urn:docflow:fns705-letter-incoming")
    FNS705_LETTER_INCOMING,

    /**
     * Исходящие письма (ФНС, 705).
     */
    @SerializedName("urn:docflow:fns705-letter-outgoing")
    FNS705_LETTER_OUTGOING,

    @SerializedName("urn:docflow:business-registration")
    BUSINESS_REGISTRATION(BusinessRegistrationDescription.class),

    /**
     * Отчетность Росстат.
     */
    @SerializedName("urn:docflow:stat-report")
    STAT_REPORT(StatReport.class),

    /**
     * ПисьмоРеспондент.
     */
    @SerializedName("urn:docflow:stat-letter")
    STAT_LETTER(StatLetter.class),

    /**
     * письмоОрганФСГС.
     */
    @SerializedName("urn:docflow:stat-cu-letter")
    STAT_CU_LETTER,

    /**
     * рассылка.
     */
    @SerializedName("urn:docflow:stat-cu-broadcast")
    STAT_CU_BROADCAST,

    /**
     * Отчетность 4-ФСС.
     */
    @SerializedName("urn:docflow:fss-report")
    FSS_REPORT(FssReport.class),

    /**
     * Листки нетрудоспособности ФСС.
     */
    @SerializedName("urn:docflow:fss-sick-report")
    FSS_SICK_REPORT(FssSickReport.class),

    /**
     * Группированные Уведомления ФМС.
     */
    @SerializedName("urn:docflow:fms-notification-grouped")
    FMS_NOTIFICATION_GROUPED,

    /**
     * Уведомления ФМС.
     */
    @SerializedName("urn:docflow:fms-notification")
    FMS_NOTIFICATION,

    /**
     * Регистрация респондетнов в ФМС.
     */
    @SerializedName("urn:docflow:fms-registration")
    FMS_REGISTRATION,

    /**
     * Отчетность в пенсионный фонд.
     */
    @SerializedName("urn:docflow:pfr-report")
    PFR_REPORT(PfrReport.class),

    /**
     * Служебные документы, например.
     * Заявление на подключение страхователя к ЭДО ПФР (ЗПЭД).
     */
    @SerializedName("urn:docflow:pfr-ancillary")
    PFR_ANCILLARY(PfrReport.class),

    /**
     * Письма в пенсионный фонд.
     */
    @SerializedName("urn:docflow:pfr-letter")
    PFR_LETTER(PfrLetter.class),

    /**
     * Письма из пенсионного фонда.
     */
    @SerializedName("urn:docflow:pfr-cu-letter")
    PFR_CU_LETTER,

    /**
     * Уточнение платежей в пенсионный фонд.
     */
    @SerializedName("urn:docflow:pfr-ios")
    PFR_IOS(PfrIos.class),


    @SerializedName("urn:docflow:unknown")
    UNKNOWN(UnknownDescription.class);

    private final Class<? extends IDocflowDescription> type;

    DocflowType() {
        this.type = null;
    }

    DocflowType(@NotNull Class<? extends IDocflowDescription> type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public Class<? extends IDocflowDescription> getDescriptionType() {
        if (this == UNKNOWN)
            return type;
        return hasDescriptionType() ? type : UNKNOWN.getDescriptionType();
    }

    public boolean hasDescriptionType() {
        return type != null;
    }
}
