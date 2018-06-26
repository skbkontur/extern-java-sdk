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

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;


/**
 * @author AlexS
 */
public class LinkDto {

    public ru.kontur.extern_api.sdk.model.Link fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.Link dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.Link link = new ru.kontur.extern_api.sdk.model.Link();
        link.setHref(dto.getHref());
        link.setName(dto.getName());
        link.setProfile(dto.getProfile());
        link.setRel(dto.getRel());
        link.setTemplated(dto.getTemplated());
        link.setTitle(dto.getTitle());

        return link;
    }

    public ru.kontur.extern_api.sdk.model.Link fromDto(Map<String, Object> dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.Link link = new ru.kontur.extern_api.sdk.model.Link();
        link.setHref((String) dto.get("href"));
        link.setName((String) dto.get("name"));
        link.setProfile((String) dto.get("profile"));
        link.setRel((String) dto.get("rel"));
        link.setTemplated((Boolean) dto.get("templated"));
        link.setTitle((String) dto.get("title"));

        return link;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Link toDto(ru.kontur.extern_api.sdk.model.Link link) {

        if (link == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Link dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Link();
        setPrivateFieldValue(dto, "href", link.getHref());
        setPrivateFieldValue(dto, "name", link.getName());
        setPrivateFieldValue(dto, "profile", link.getProfile());
        setPrivateFieldValue(dto, "rel", link.getRel());
        setPrivateFieldValue(dto, "templated", link.getTemplated());
        setPrivateFieldValue(dto, "title", link.getTitle());

        return dto;
    }

    private void setPrivateFieldValue(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ignore) {
        }
    }
}
