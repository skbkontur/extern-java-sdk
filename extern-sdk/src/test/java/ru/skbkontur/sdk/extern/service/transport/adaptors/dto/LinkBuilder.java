/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.lang.reflect.Field;

/**
 *
 * @author AlexS
 */
class LinkBuilder {

    private String href = null;
    private String rel = null;
    private String name = null;
    private String title = null;
    private String profile = null;
    private Boolean templated = null;

    public void setHref(String href) {
        this.href = href;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setTemplated(Boolean templated) {
        this.templated = templated;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Link buildToDto() {
        ru.skbkontur.sdk.extern.service.transport.swagger.model.Link link = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Link();
        setProperty(link, "href", href);
        setProperty(link, "rel", rel);
        setProperty(link, "name", name);
        setProperty(link, "title", title);
        setProperty(link, "profile", profile);
        setProperty(link, "templated", templated);
        return link;
    }
    
    public ru.skbkontur.sdk.extern.model.Link buildFromDto() {
        ru.skbkontur.sdk.extern.model.Link link = new ru.skbkontur.sdk.extern.model.Link();
        setProperty(link, "href", href);
        setProperty(link, "rel", rel);
        setProperty(link, "name", name);
        setProperty(link, "title", title);
        setProperty(link, "profile", profile);
        setProperty(link, "templated", templated);
        return link;
    }
    
    private void setProperty(Object link, String name, Object value) {
        try {
            Field field = link.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(link, value);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException x) {
            System.out.println(x.getMessage());
        }
    }
}
