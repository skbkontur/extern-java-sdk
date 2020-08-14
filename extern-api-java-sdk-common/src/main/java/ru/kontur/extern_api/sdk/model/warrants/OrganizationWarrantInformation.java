package ru.kontur.extern_api.sdk.model.warrants;

import java.util.UUID;

/**
 * Информация о доверенности организации учетной записи Экcтерна
 */
public class OrganizationWarrantInformation {

    private UUID organizationId = null;
    private String inn = null;
    private String kpp = null;
    private String organizationName = null;
    private Warrant warrant = null;

    /**
     * ИД организации учетной записи Экcтерна
     *
     * @return organizationId
     **/
    public UUID getOrganizationId() {
        return organizationId;
    }

    /**
     * ИНН организации учетной записи Экcтерна
     *
     * @return ИНН
     **/
    public String getInn() {
        return inn;
    }

    /**
     * КПП организации учетной записи Экcтерна
     *
     * @return КПП
     **/
    public String getKpp() {
        return kpp;
    }

    /**
     * Название организации учетной записи Экcтерна
     *
     * @return название организации
     **/
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Доверенность, если она есть, относящаяся к этой организации учетной записи Экcтерна
     *
     * @return доверенность
     **/
    public Warrant getWarrant() {
        return warrant;
    }
}
