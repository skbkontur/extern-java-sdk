package ru.kontur.extern_api.sdk.model;

/**
 * <p>
 * Класс содержит данные, описывающие документ, после отправки.
 * Используется в сервисе {@code DraftService}
 * </p>
 * @author Andrey Manakov
 */
public class DocflowDocumentDescription {

    private String type = null;
    private String filename = null;
    private String contentType = null;
    private Boolean compressed = null;
    public DocflowDocumentDescription type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Возвращает информацию о том сжат ли зашифрованный контент.
     * @return compressed
     */
    public Boolean getCompressed() {
        return compressed;
    }

    /**
     * Устанавливает сжатый ли зашифрованный контент.
     * @param compressed сжатый ли зашифрованный контент
     */
    public void setCompressed(Boolean compressed) {
        this.compressed = compressed;
    }

    /**
     * Возвращает тип документа. Могут быть следующие типы документов для отправки:
     * <ul>
     *   <li>urn:document:fns534-report - декларация для ФНС</li>
     *   <li>urn:document:fns534-report-warrant - доверенность</li>
     *   <li>urn:document:fns534-report-attachment - приложение к декларации для ФНС</li>
     * </ul>
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип документа. Могут быть следующие типы документов для отправки:
     * @param type тип документа
     * <ul>
     *   <li>urn:document:fns534-report - декларация для ФНС</li>
     *   <li>urn:document:fns534-report-warrant - доверенность</li>
     *   <li>urn:document:fns534-report-attachment - приложение к декларации для ФНС</li>
     * </ul>
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Устанавливает имя файла декларации.
     * Метод пытается угадать тип отправляемого документа по его имени файла
     * @param filename имя файла декларации
     * @return {@link DocumentDescription}
     */
    public DocflowDocumentDescription filename(String filename) {
        setFilename(filename);
        return this;
    }

    /**
     * Возвращает имя файла декларации
     * @return filename имя файла декларации
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Устанавливает имя файла декларации.
     * Метод пытается угадать тип отправляемого документа по его имени файла
     * @param filename имя файла декларации
     */
    public void setFilename(String filename) {
        this.filename = filename;
        // пытаемся угадать тип
        // this.type = Type.guessType(filename).value;
    }

    /**
     * Устанавливает тип контента (content-type) документа
     * @param contentType тип контента (content-type) документа
     * @return {@link DocumentDescription}
     */
    public DocflowDocumentDescription contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Возвращает тип контента (content-type) документа
     * @return contentType тип контента (content-type) документа
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Устанавливает тип контента (content-type) документа
     * @param contentType тип контента (content-type) документа
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Содержит доступное перечисления типов документов
     */
    private enum Type {
        /** декларация ФНС */
        DEKL("urn:document:fns534-report", "^(?i)NO_(\\w+)_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        /** доверенность для ФНС */
        DOV("urn:document:fns534-report-warrant", "^(?i)ON_DOV_(\\d{4})_(\\d{4})_(\\d{12,19})_(\\d{8})_([\\w-]{36})\\.xml$"),
        /** приложение к декларации ФНС */
        APP("urn:document:fns534-report-attachment", ""),
        /** неизвестный тип */
        UNKNOWN(null, "");

        private final String value;
        private final String pattern;

        Type(String value, String pattern) {
            this.value = value;
            this.pattern = pattern;
        }

        private static Type guessType(String fileName) {
            if (fileName == null) {
                return UNKNOWN;
            }

            if (fileName.matches(DOV.pattern)) {
                return DOV;
            } else if (fileName.matches(DEKL.pattern)) {
                return DEKL;
            } else {
                return APP;
            }
        }
    }
}