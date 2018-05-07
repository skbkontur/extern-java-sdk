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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend;

import java.io.IOException;
import java.util.UUID;


/**
 * @author AlexS
 */
public class SignatureToSendAdapter extends TypeAdapter<SignatureToSend> {

    private static final java.util.Base64.Encoder ENCODER = java.util.Base64.getEncoder();
    private static final java.util.Base64.Decoder DECODER = java.util.Base64.getDecoder();

    @Override
    public void write(JsonWriter out, SignatureToSend s) throws IOException {
        if (s == null) {
            out.nullValue();
        } else {
            out.beginObject();
            if (s.getId() != null) {
                out.name("id").value(s.getId().toString());
            }
            if (s.getContentData() != null) {
                out.name("content-data").value(ENCODER.encodeToString(s.getContentData()));
            }
            out.endObject();
        }
    }

    @Override
    public SignatureToSend read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.BEGIN_OBJECT) {
            return null;
        }

        SignatureToSend s = new SignatureToSend();

        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    s.setId(UUID.fromString(in.nextString()));
                    break;
                case "content-data":
                    s.setContentData(DECODER.decode(in.nextString()));
                    break;
            }
        }

        return s;
    }

}
