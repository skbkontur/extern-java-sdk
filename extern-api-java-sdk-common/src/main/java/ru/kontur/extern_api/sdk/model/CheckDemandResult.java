package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.List;

public class CheckDemandResult {

    private List<Link> links = null;
    private boolean hasErrors;
    private List<String> errorCodes = new ArrayList<>();

    /**
     * Возвращает список ссылок
     *
     * @return список ссылок
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список ссылок
     *
     * @param links список ссылок
     * @see Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * @return Возвращает флаг показывающий, были ли найдены ошибки в ходе проверки требования
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * Устанавливает возвращает флаг показывающий, были ли найдены ошибки в ходе проверки требования
     *
     * @param hasErrors флаг показывающий, были ли найдены ошибки в ходе проверки требования
     */
    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }


    /**
     * @return Возвращает коды найденных ошибок
     */
    public List<String> getErrorCodes() {
        return errorCodes;
    }

    /**
     * Устанавливает коды найденных ошибок
     *
     * @param errorCodes коды найденных ошибок
     */
    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
