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

import java.util.Objects;


/**
 * <p>
 *     Класс описывает ссылку на ресурс
 * </p>
 * @author Aleksey Sukhorukov
 */
public class Link {

    private String href = null;
    private String rel = null;
    private String name = null;
    private String title = null;
    private String profile = null;
    private Boolean templated = null;

    /**
     * <p>Возвращает ссылку на ресурс.</p>
     * @return ссылка на ресурс
     */
    public String getHref() {
        return href;
    }

    /**
     * <p>Устанавливает ссылку на ресурс.</p>
     * @param href ссылка на ресурс
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * <p>Возвращает тип ссылки:</p>
     *     <ul>
     *         <li>self - содержит ссылку на текущую сущность</li>
     *         <li>reply - содержит ссылку на сущность, которую необходимо создать</li>
     *         <li>content - содержит ссылку на контент сущности</li>
     *         <li>docflow - содержит ссылку на сущность документооборот</li>
     *     </ul>
     *
     * @return тип ссылки
     */
    public String getRel() {
        return rel;
    }

    /**
     * <p>Устанавливает тип ссылки.</p>
     * @param rel тип ссылки:
     *     <ul>
     *         <li>self - содержит ссылку на текущую сущность</li>
     *         <li>reply - содержит ссылку на сущность, которую необходимо создать</li>
     *         <li>content - содержит ссылку на контент сущности</li>
     *         <li>docflow - содержит ссылку на сущность документооборот</li>
     *     </ul>
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * <p>Возвращает имя ссылки.</p>
     * @return имя ссылки
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Устанавливает имя ссылки.</p>
     * @param name имя ссылки
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Возвращает загаловок ссылки.</p>
     * @return загаловок ссылки
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Возвращает загаловок ссылки.</p>
     * @param title загаловок ссылки
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Возвращает наименование профиля ссылки.</p>
     * @return наименование профиля ссылки
     */
    public String getProfile() {
        return profile;
    }

    /**
     * <p>Устанавливает наименование профиля ссылки.</p>
     * @param profile наименование профиля ссылки
     */
    public void setProfile(String profile) {
        this.profile = profile;
    }

    /**
     * <p>Возвращает признак шаблонной ссылки.</p>
     * @return признак шаблонной ссылки
     */
    public Boolean getTemplated() {
        return templated;
    }

    /**
     * <p>Устанавливает признак шаблонной ссылки.</p>
     * @param templated признак шаблонной ссылки
     */
    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href)
                && Objects.equals(this.rel, link.rel)
                && Objects.equals(this.name, link.name)
                && Objects.equals(this.title, link.title)
                && Objects.equals(this.profile, link.profile)
                && Objects.equals(this.templated, link.templated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, name, title, profile, templated);
    }

    @Override
    public String toString() {
        return "Link{rel='" + rel + '\'' + ", name='" + name + '\'' + '}';
    }
}
