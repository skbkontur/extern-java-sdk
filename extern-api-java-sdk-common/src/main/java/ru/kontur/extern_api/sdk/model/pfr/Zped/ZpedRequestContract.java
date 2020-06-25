package ru.kontur.extern_api.sdk.model.pfr.Zped;

import ru.kontur.extern_api.sdk.model.BuildDocumentContract;

/**
 * Класс предназначен для передачи информации необходимой для формирования заявление на подключение к системе электронного документооборота ПФР (ЗПЭД)
 */
public class ZpedRequestContract implements BuildDocumentContract {
    private Boolean ForUl;
    private InsurerUlDto InsurerUl;
    private InsurerIpDto InsurerIp;
    private RepresentativeDto Representative;

    public ZpedRequestContract(InsurerUlDto insurerUl, RepresentativeDto representative) {
        ForUl = true;
        InsurerUl = insurerUl;
        Representative = representative;
    }

    public ZpedRequestContract(InsurerIpDto insurerIp, RepresentativeDto representative) {
        InsurerIp = insurerIp;
        Representative = representative;
    }

    /**
     * @return true, если ЗПЭД для ЮЛ, null или false - для ИП
     */
    public Boolean getForUl() {
        return ForUl;
    }

    /**
     * @param forUl true, если ЗПЭД для ЮЛ, null или false - для ИП
     */
    public void setForUl(Boolean forUl) {
        ForUl = forUl;
    }

    /**
     * @return информация о страхователе (ЮЛ)
     */
    public InsurerUlDto getInsurerUl() {
        return InsurerUl;
    }

    /**
     * @param insurerUl информация о страхователе (ЮЛ)
     */
    public void setInsurerUl(InsurerUlDto insurerUl) {
        InsurerUl = insurerUl;
    }

    /**
     * @return информация о страхователе (ИП)
     */
    public InsurerIpDto getInsurerIp() {
        return InsurerIp;
    }

    /**
     * @param insurerIp информация о страхователе (ИП)
     */
    public void setInsurerIp(InsurerIpDto insurerIp) {
        InsurerIp = insurerIp;
    }

    /**
     * @return информация о представителе
     */
    public RepresentativeDto getRepresentative() {
        return Representative;
    }

    /**
     * @param representative информация о представителе
     */
    public void setRepresentative(RepresentativeDto representative) {
        Representative = representative;
    }
}