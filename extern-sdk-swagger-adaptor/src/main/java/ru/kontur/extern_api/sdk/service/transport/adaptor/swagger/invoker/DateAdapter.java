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

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author AlexS
 */
public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    /**
     * Serialize
     *
     * @param src       Date
     * @param typeOfSrc Type
     * @param context   Json Serialization Context
     * @return Json Element
     */
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        } else {
            return new JsonPrimitive(formatDatetime(src));
        }
    }

    /**
     * Deserialize
     *
     * @param json    Json element
     * @param date    Type
     * @param context Json Serialization Context
     * @return Date
     * @throws JsonParseException if fail to parse
     */
    @Override
    public Date deserialize(JsonElement json, Type date, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsJsonPrimitive().getAsString();
        try {
            return parseDateOrDatetime(str);
        } catch (RuntimeException e) {
            throw new JsonParseException(e);
        }
    }
    
    /**
     * Format the given Date object into string (Datetime format).
     *
     * @param date Date object
     * @return Formatted datetime in string representation
     */
    public String formatDatetime(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public Date parseDateOrDatetime(String str) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(str);
        }
        catch (ParseException x) {
            return null;
        }
    }
}
