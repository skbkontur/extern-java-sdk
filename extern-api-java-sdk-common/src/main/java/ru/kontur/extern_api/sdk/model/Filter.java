/*
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
 *
 */

package ru.kontur.extern_api.sdk.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public interface Filter {

    long getSkip();

    int getTake();

    /**
     * Should not return mapping like {@code o->null}. {@link #stripNullKeys(Map)} might help/
     * @return resulting filters
     * @see #stripNullKeys(Map)
     */
    Map<String, String> asFilterMap();

    /**
     * @param map input map
     * @return copy of a given map without {@code o->null} mappings
     */
    static Map<String, String> stripNullKeys(Map<String, String> map) {
        return map.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

}
