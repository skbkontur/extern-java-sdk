package ru.kontur.extern_api.sdk.model;

/**
 * <p>
 * Класс содержит информацию о налоговой инспекции, в которую необходимо отправить документ для регистрации
 * бизнеса
 * </p>
 *
 * @author Alexandr Volkov
 */
public class RegistrationFnsRecipient implements Recipient {

    private String registrationIfnsCode;

    public RegistrationFnsRecipient() {
    }

    public RegistrationFnsRecipient(String registrationIfnsCode) {
        this.registrationIfnsCode = registrationIfnsCode;
    }

    /**
     * <p>Возвращает код налоговой инспекции.</p>
     *
     * @return код налоговой инспекции
     */
    public String getRegistrationIfnsCode() {
        return registrationIfnsCode;
    }

    /**
     * <p>Устанавливает код налоговой инспекции.</p>
     *
     * @param registrationIfnsCode код налоговой инспекции
     */
    public void setRegistrationIfnsCode(String registrationIfnsCode) {
        this.registrationIfnsCode = registrationIfnsCode;
    }
}
