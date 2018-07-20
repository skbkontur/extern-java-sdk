/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk.model;

/**
 * @author AlexS
 *
 * Класс содержит описание проблемы, который может вернуть метод {@code DraftService.check}
 */
public class CheckError {

    private String description = null;
    private String source = null;
    private String level = null;
    private String type = null;
    private String tags = null;
    private String id = null;

    /**
     * Устанавливает описание проблемы
     * @param description описание проблемы
     * @return {@link CheckError}
     */
    public CheckError description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Возвращает описание проблемы
     * @return description описание проблемы
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание проблемы
     * @param description описание проблемы
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Устанавливает источник проблемы
     * @param source источник проблемы
     * @return {@link CheckError}
     */
    public CheckError source(String source) {
        this.source = source;
        return this;
    }

    /**
     * Возвращает источник проблемы
     * @return source источник проблемы
     */
    public String getSource() {
        return source;
    }

    /**
     * Устанавливает источник проблемы
     * @param source источник проблемы
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Устанавливает уровень серьезности проблемы
     * @param level <p>уровень серьезности проблемы
     * <li>error - ошибка</li>
     * <li>warning - предупреждение</li>
     * </p>
     * @return {@link CheckError}
     */
    public CheckError level(String level) {
        this.level = level;
        return this;
    }

    /**
     * Возвращает уровень серьезности проблемы
     * @return level <p>уровень серьезности проблемы
     * <li>error - ошибка</li>
     * <li>warning - предупреждение</li>
     * </p>
     */
    public String getLevel() {
        return level;
    }

    /**
     * Устанавливает уровень серьезности проблемы
     * @param level <p>уровень серьезности проблемы
     * <li>error - ошибка</li>
     * <li>warning - предупреждение</li>
     * </p>
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Устанавливает тип проблемы
     * @param type тип проблемы
     * @return {@link CheckError}
     */
    public CheckError type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Возвращает  тип проблемы
     * @return type  тип проблемы
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип проблемы
     * @param type тип проблемы
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Устанавливает метки для проблемы
     * @param tags метки для проблемы
     * @return {@link CheckError}
     */
    public CheckError tags(String tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Возвращает метки для проблемы
     * @return tags метки для проблемы
     */
    public String getTags() {
        return tags;
    }

    /**
     * Устанавливает метки для проблемы
     * @param tags метки для проблемы
     * @param tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Устанавливает идентификатор проблемы
     * @param id идентификатор проблемы
     * @return {@link CheckError}
     */
    public CheckError id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Возврашает идентификатор проблемы
     * @return id идентификатор проблемы
     */
    public String getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор проблемы
     * @param id идентификатор проблемы
     */
    public void setId(String id) {
        this.id = id;
    }
}
