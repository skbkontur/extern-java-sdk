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
package ru.kontur.extern_api.sdk.typeadaptors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.FssRecipient;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.TogsRecipient;

/**
 *
 * @author alexs
 */
public class GsonRecipientAdaptor implements JsonSerializer<Recipient>, JsonDeserializer<Recipient> {

    @Override
    public JsonElement serialize(Recipient src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return JsonNull.INSTANCE;
        }
        else {
            if (src instanceof FnsRecipient) {
                return context.serialize(src, FnsRecipient.class);
            }
            else if (src instanceof TogsRecipient) {
                return context.serialize(src, TogsRecipient.class);
            }
            else if (src instanceof FssRecipient) {
                return context.serialize(src, FssRecipient.class);
            }
            else {
                return JsonNull.INSTANCE;
            }
        }
    }

    @Override
    public Recipient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String str = json.toString();
        try {
            if (str.matches(".*ifns-code.*")) {
                return context.deserialize(json, FnsRecipient.class);
            }
            else if (str.matches(".*togs-code.*")) {
                return context.deserialize(json, TogsRecipient.class);
            }
            else if (str.matches(".*fss-code.*")) {
                return context.deserialize(json, FssRecipient.class);
            }
            else {
                return null;
            }
        }
        catch (RuntimeException e) {
            throw new JsonParseException(e);
        }
    }
    
}
