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

package ru.skbkontur.sdk.extern.common;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import java.util.List;
import java.util.UUID;
import ru.skbkontur.sdk.extern.model.Link;

/**
 * @author Mikhail Pavlenko
 */

public class StandardObjectsValidator {

    public static void validateId(UUID id) {
        assertEquals("Id is wrong!", StandardValues.ID, id.toString());
    }

    public static void validateEmptyList(List<?> list, String listName) {
        assertNotNull(listName + " must not be null!", list);
        assertTrue(listName + " must be empty!", list.isEmpty());
    }

    public static void validateNotEmptyList(List<?> list, String listName) {
        assertNotNull(listName + " must not be null!", list);
        assertFalse(listName + " must not be empty!", list.isEmpty());
    }

    public static void validateLink(Link link) {
        assertNotNull("Link must not be null!", link);
        assertEquals("Rel is wrong!", "string", link.getRel());
        assertEquals("Href is wrong!", "string", link.getHref());
        assertEquals("Name is wrong!", "string", link.getName());
        assertEquals("Title is wrong!", "string", link.getTitle());
        assertEquals("Profile is wrong!", "string", link.getProfile());
        assertTrue("Templated is wrong!", link.getTemplated());
    }
}
